package com.zzrong.badminton_analyzer.activity;

import android.view.*;
import android.widget.*;
import com.zzrong.badminton_analyzer.adapter.ServerVideoAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.func.*;
import com.zzrong.badminton_analyzer.room.*;
import org.jetbrains.annotations.NotNull;
import com.zzrong.badminton_analyzer.viewModel.RctlyViewModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class RecentlyViewActivity extends AppCompatActivity {

    private AppDatabase database;
    private UserDao userDao;
    private HistoryDao historyDao;
    private VideoDao videoDao;
    private MaterialToolbar bar;
    private NavigationView nv;
    private int currentPageId;
    private ArrayList<VideoItem> allItems;
    private ArrayList<VideoItem> rctlyItems;
    private ArrayList<VideoItem> favItems;
    private Map<String,String> bookmarks; //bookmark_id to bookmark_name
    private Map<String,Integer> bookmarkPos; //bookmarkName to bookmarkPostion in Drawer
    private Map<String, ArrayList<VideoItem>> bookmarkContents; //bookmarkName to a list of VideoItems

    private RctlyViewModel model;
    private ServerVideoAdapter adapter;
    private RecyclerView recyclerView;

    private String userName;
    private String dialogMsg;
    private int nBookmark;
    private boolean getBookmarkDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_view);
        initialize();
        applyListener();
        setDrawer();
        setViewModel();
        setDrawerFooter();
        displayRctlyVid(userName, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAfterResume();
    }
    @Override
    public void onBackPressed() {
        return;
    }

    public void initialize(){

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "info").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao =  database.userDao();
        videoDao = database.videoDao();
        historyDao = database.historyDao();
        rctlyItems = new ArrayList<>();
        model = new ViewModelProvider(this).get(RctlyViewModel.class);
        nv = findViewById(R.id.nav_view);

        allItems = new ArrayList<>();
        rctlyItems = new ArrayList<>();
        favItems = new ArrayList<>();

        bookmarks = new HashMap<>();
        bookmarkPos = new HashMap<>();
        bookmarkContents = new HashMap<>();

        userName = userDao.getUser().getName();

        //透過登入畫面進入則顯示歡迎提示
        if(getIntent().getExtras() != null){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.rctly_layout), "歡迎回來， " + userName, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }




        // get total video list from server DB

    }

    public void setDrawer(){
       //initialized
       nv = findViewById(R.id.nav_view);
       nv.setItemIconTintList(null);

       MenuItem rctlyViewed = nv.getMenu().findItem(R.id.item2);
       currentPageId = R.id.item2;
       nv.setCheckedItem(rctlyViewed);

       //set bookmark
        getBookmarkDone = false;
        getBookmark();
        while(!getBookmarkDone){} // sychronization
        Collection<String> mapVals= bookmarks.values();
        SubMenu sub = nv.getMenu().getItem(3).getSubMenu();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int pos = -1;
                for(String name :mapVals){
                    Log.i("map val: ",name);
                    pos += 1;
                    sub.add(Menu.NONE, pos, Menu.NONE, name).setIcon(R.drawable.ic_outline_stop_circle).setCheckable(true);
                    bookmarkPos.put(name, pos);
                }
            }
        });
    }

    public void getBookmark(){
        nBookmark = -1;
        // get bookmark list from server
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String response = FlaskApiSender.getBookmark(userName);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("JSON: ",jsonArray.toString());

                    //update amount of bookmarks
                    nBookmark += jsonArray.length();
                    //for each bookmark
                    for(int i = 0; i < jsonArray.length(); i++){
                        //bookmark info
                        String bookmarkId = jsonArray.getJSONObject(i).getString("id");
                        String bookmarkName = jsonArray.getJSONObject(i).getString("name");
                        bookmarks.put(bookmarkId, bookmarkName);

                        //bookmark contents
                        String strContent = FlaskApiSender.getBookmarkContent(bookmarkId);
                        JSONArray arrContent = new JSONArray(strContent);
                        ArrayList<VideoItem> lst = new ArrayList<>();

                        Log.d("Array: ", strContent.toString());

                        //for each video in this bookmark
                        for(int j = 0; j < arrContent.length(); j++){
                            String videoName = arrContent.getJSONObject(j).getString("video");
                            lst.add(getVideo(videoName));
                        }
                        bookmarkContents.put(bookmarkName, lst);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                getBookmarkDone = true;
            }
        });

        t.start();
    }

    public void removeBookmark(String bookmark){

        //Check & Perform execution
        removeBookmarkDialog(bookmark);
    }

    public ArrayList<VideoItem> getFavoriteVideo(String userName){

        Boolean done = false;
        ArrayList<VideoItem> lst = new ArrayList<>();
        final Object[] finalObj = {done};

        new Thread(new Runnable() {
            @Override
            public void run() {
                String strContent = FlaskApiSender.getFavorite(userName);
                JSONArray arrContent = null;
                try {
                    arrContent = new JSONArray(strContent);
                    for(int j = 0; j < arrContent.length(); j++){
                        String videoName = arrContent.getJSONObject(j).getString("video");
                        lst.add(getVideo(videoName));
                        finalObj[0] = true;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        while(!(Boolean)finalObj[0]){}

        return lst;
    }
    public void setDrawerFooter(){
        nv.getMenu().getItem(3).getSubMenu().add(Menu.NONE, -1, Menu.NONE, "").setEnabled(false);
    }

    public void applyListener(){
        DrawerLayout dLayout = findViewById(R.id.rctly_layout);
        bar = findViewById(R.id.topAppBar);
        bar.setNavigationOnClickListener(v -> dLayout.open());

        nv = findViewById(R.id.nav_view);
        nv.bringToFront();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                //沒改變頁面則不必動作
                if(item.getItemId() == currentPageId){
                    dLayout.close();
                    return true;
                }
                else {
                    currentPageId = item.getItemId();}

                switch(item.getItemId()){
//                    case(R.id.item0):{
//                        addBookmarkDialog();
//                        return true;
//                    }
                    case(R.id.item1):{
                        bar.setTitle(R.string.all_vid);
                        displayAllVid(false); //not resume, the page did change
                        dLayout.close();
                        return true;
                    }
                    case(R.id.item2):{
                        bar.setTitle(R.string.rctly_viewed);
                        displayRctlyVid(userName,false);
                        dLayout.close();
                        return true;
                    }
                    case(R.id.item3):{
                        bar.setTitle(R.string.my_fav);
                        displayFavVid(userName,false);
                        dLayout.close();
                        return true;
                    }
                    //自訂書籤
                    default: {
                        String bookmark = item.getTitle().toString();
                        bar.setTitle(bookmark);
                        displayBookmark(bookmark, false);
                        dLayout.close();
                        return true; }
                }
            }
        });

        ImageView im_user = findViewById(R.id.footer_im_1);
        ImageView im_setting = findViewById(R.id.footer_im_2);
        ImageView im_info = findViewById(R.id.footer_im_3);

        im_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.rctly_layout),"已成功登出", Snackbar.LENGTH_SHORT);
                snackbar.show();

                userDao.deleteAll();
                finish();
            }
        });
        im_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(RecentlyViewActivity.this, "under construction", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        im_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(RecentlyViewActivity.this, "under construction", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_recently_view);
        fab.setOnClickListener(view -> clickFAB(view));

    }

    public void setViewModel(){
        //call back when content change
        model.getItems().observe(this, videoItems -> {
            Log.d("ModelViewState: ","detect change");
            createRecyclerView();
        });
        //execute now
        createRecyclerView();
    }

    protected void createRecyclerView(){
        recyclerView = findViewById(R.id.rctlyRecycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new ServerVideoAdapter(this, model.getItems().getValue());
        recyclerView.setAdapter(adapter);
    }

    //從其他頁面退回來 影片列表要反映Local DB的變動
    protected void updateAfterResume(){
        nv = findViewById(R.id.nav_view);
        MenuItem item = nv.getCheckedItem();
        switch(item.getItemId()){
            case(R.id.item1):{
                displayAllVid(true);  //resumed from other page, check the change
                return;
            }
            case(R.id.item2):{
                displayRctlyVid(userName, true);
                return;
            }
            case(R.id.item3):{
                displayFavVid(userName, true);
                return;
            }
            //bookmark clicked
            default:{
                displayBookmark(item.getTitle().toString(), true);
                return;
            }


        }
    }
    protected void displayAllVid(Boolean resumed){
        List<Video> vidLst = videoDao.getAll();
        allItems = videoEntityToVideoItem(vidLst);
        if(resumed == true){
            if(isTwoVideoListSame(model.getItems().getValue(), allItems)){
                return;
            }
        }
        model.setItems(allItems);
    }

    protected void displayRctlyVid(String username, Boolean resumed){
        List<Video> rctlyLst = historyDao.getVideos(username);
        rctlyItems = videoEntityToVideoItem(rctlyLst);
        if(resumed == true){
            if(isTwoVideoListSame(model.getItems().getValue(), rctlyItems)){
                return;
            }
        }
        model.setItems(rctlyItems);
    }

    protected void displayFavVid(String username, Boolean resumed){
        favItems = getFavoriteVideo(userName);

        if(resumed == true){
            if(isTwoVideoListSame(model.getItems().getValue(), favItems)){
                return;
            }
        }
        model.setItems(favItems);
    }

    protected void displayBookmark(String bookmark, Boolean resumed){

        ArrayList<VideoItem> vids = bookmarkContents.get(bookmark);

        if(resumed == true){
            if(isTwoVideoListSame(model.getItems().getValue(), vids)){
                return;
            }
        }
        model.setItems(vids);
    }

    private ArrayList<VideoItem> videoEntityToVideoItem(List<Video> lst){
        ArrayList<VideoItem> arrLst = new ArrayList<>();
        for(Video v: lst){
            String videoId = v.getId();
            String videoTitle = v.getTitle();
            String thumbLink = v.getPic();
            String videoDescription = null;
            arrLst.add(new VideoItem(videoId,videoTitle,thumbLink,videoDescription));
        }
        return arrLst;
    }

    protected void clickFAB(View view){
        Context current = view.getContext();
        Intent intent = new Intent(current,SearchActivity.class);
        current.startActivity(intent);
    }

    private void addBookmarkDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.add_bookmark_title));
        View viewInflated =  getLayoutInflater().inflate(R.layout.dialog_add_cat, null);
        builder.setView(viewInflated);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText et = viewInflated.findViewById(R.id.alert_dialog_et);
                dialogMsg = et.getText().toString();
                dialog.dismiss();

                dialogMsg = dialogMsg.trim().replace("\n","");//不允許換行字元

                if(dialogMsg.isEmpty()){
                    return;
                }
                else if(bookmarks.containsValue(dialogMsg)){
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.rctly_layout), "此分類名稱已存在", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String newBookmarkId = FlaskApiSender.addBookmark(userName,dialogMsg);
                        nBookmark += 1;
                        bookmarks.put(newBookmarkId, dialogMsg);
                        bookmarkPos.put(dialogMsg, nBookmark);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //remove blank item, add menu, recover blank item
                                SubMenu sub = nv.getMenu().getItem(3).getSubMenu();
                                sub.removeItem(-1);
                                sub.add(Menu.NONE, nBookmark, Menu.NONE, dialogMsg).setIcon(R.drawable.ic_outline_stop_circle).setEnabled(true);
                                sub.add(Menu.NONE, -1, Menu.NONE, "").setEnabled(false);
                            }
                        });
                    }
                }).start();


            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        final AlertDialog dialog = builder.create();

        dialog.show();

        Button positiveBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);


        positiveBtn.setTextColor(ContextCompat.getColor(this,R.color.black));
        negativeBtn.setTextColor(ContextCompat.getColor(this,R.color.icon));
    }

    private void removeBookmarkDialog(String bookmark){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.remove_bookmark_title));
        View viewInflated =  getLayoutInflater().inflate(R.layout.dialog_rmv_cat, null);
        ((TextView)viewInflated.findViewById(R.id.tv_rmv_cat)).setText("是否確定刪除\"" + bookmark + "\"分類？");
        builder.setView(viewInflated);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SubMenu sub = nv.getMenu().getItem(3).getSubMenu();
                sub.removeItem(bookmarkPos.get(bookmark));
                FlaskApiSender.removeBookmark(userName, bookmark);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.show();

        Button positiveBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);


        positiveBtn.setTextColor(ContextCompat.getColor(this,R.color.black));
        negativeBtn.setTextColor(ContextCompat.getColor(this,R.color.icon));
    }

    private Boolean isTwoVideoListSame(ArrayList<VideoItem> arr1, ArrayList<VideoItem> arr2){

        if(arr1.size()!=arr2.size()) return false;

        for(int i = 0; i < arr1.size(); i++){
            if(! arr1.get(i).getID().equals(arr2.get(i).getID())){
                return false;
            }
        }
        return true;
    }

    public VideoItem getVideo(String vid){

        Boolean done = false; //thread synchronization
        VideoItem item = null;
        final Object[] lst = {done, item};

                new Thread(new Runnable(){
            @Override
            public void run() {
                String response = FlaskApiSender.getVideo(vid);
                try {
                    JSONObject json = new JSONObject(response);
                    String id = json.getString("id");
                    String title = json.getString("title");
                    String status = json.getString("status");
                    lst[1] = new VideoItem(id,title,status);
                    lst[0] = true;

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        while(!(Boolean)lst[0]){} //wait()

        return (VideoItem) lst[1];
    }

    public String getUserName(){return userName;}
    public Map<String,Integer> getPosMap(){  //bookmark name -> menu item position
        return bookmarkPos;
    }

    public Map<String,String> getIDMap(){  //bookmark name -> bookmark id
        return bookmarks;
    }
}