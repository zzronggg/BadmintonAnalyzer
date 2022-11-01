package com.zzrong.badminton_analyzer.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.zzrong.badminton_analyzer.BuildConfig;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.fragment.PartSelectionFragment;
import com.zzrong.badminton_analyzer.fragment.VideoDataFragment;
import com.zzrong.badminton_analyzer.func.*;
import com.zzrong.badminton_analyzer.room.*;
import com.zzrong.badminton_analyzer.viewModel.FragmentViewModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class ExoPlayer extends AppCompatActivity {
    private String vid;
    private FragmentViewModel model;
    private TabLayout tLayout;

    //database
    private AppDatabase database;
    private UserDao userDao;
    private VideoDao videoDao;
    private HistoryDao historyDao;

    //video player
    String url;
    long currentTime;
    private com.google.android.exoplayer2.ExoPlayer player;
    private StyledPlayerView playerView;
    private ArrayList<Fragment> fragList;
    private HashMap<String,String> bookmarkId;
    private HashMap<String,Integer> bookmarkPos;

    //data
    private ArrayList<Integer> scoreInfo;
    private int currentSubTab;
    private TabLayout.OnTabSelectedListener listener;
    boolean subTabCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);
    }

    protected void onStart() {
        Log.d("frag test: ", "onStart()");
        super.onStart();
        initDatabase();
        setBar();
        setViewModel();
        try {
            prepInfo();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        initFrag();
        initPlayer();
        resumePlayer();
        setMainTab();
        recordVidWatched();
    }

    protected void onResume() { //restore player state after orientation
        Log.d("frag test: ", "onResume()");
        super.onResume();

//        if (!subTabCreated){
            setSectTab();
//        }

        //restore main tab state
        tLayout.getTabAt(0).select();
        tLayout.getTabAt(model.getMainTabState().getValue()).select();

        //restore sub tab state
//        if(model.getMainTabState().getValue() == 1 && fragList != null) {
//            switchSubFrag(1, model.getSubTabState().getValue());
//        }
    }

    private void resumePlayer(){
        try {
            if(model.getPlayTime()!=null) {
                currentTime = model.getPlayTime().getValue();
                Log.d("Current: ", currentTime + "");
                player.seekTo(currentTime);
            }
        }
        catch (Exception e){
            Log.e("NullPointerException: ", "first time created");
        }
        player.setPlayWhenReady(true);
    }

    protected void onPause() {
        super.onPause();
        releasePlayer();
        model.setMainTabState(tLayout.getSelectedTabPosition());
        if(tLayout.getSelectedTabPosition() == 1){
            model.setSubTabState(currentSubTab);
        }
        Log.d("State: ","Paused");
    }

    protected void onStop() {
        super.onStop();
        Log.d("State: ","Stopped");
    }

    private void initDatabase(){
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "info").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao =  database.userDao();
//        videoDao = database.videoDao();
        historyDao = database.historyDao();
    }
    private void recordVidWatched(){
        String uName = userDao.getUser().getName();
        Boolean addVid = true;

        //如果點入的影片有在最近五筆，且非第五筆，刪除之
        try {
            History latest = historyDao.getLatest();
            Log.d("Checkpoint： ",latest.getuName() + " " + latest.getVid());
            Log.d("Checkpoint： ",uName + " " + vid);
            if (!(latest.getVid().equals(vid) && latest.getuName().equals(uName))) {
                historyDao.delDuplicated(vid, uName);
            }
            else{ addVid = false;}
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        //添加新紀錄
        if(addVid) {
            historyDao.insert(new History(vid, uName));
        }
        //只保留最新五筆
        int count = historyDao.getUserHistoryCount(uName);
        if(count > 5){
            historyDao.delRecord(count-5,uName);
        }
    }

    private void setBar(){
        MaterialToolbar bar = findViewById(R.id.bar_exo);
        bar.setNavigationOnClickListener(v -> finish());
    }

    private void prepInfo() throws JSONException {
        //1. get data from prev. activity
        Intent intent = this.getIntent();
        vid = intent.getStringExtra("id");
        bookmarkId = (HashMap<String, String>) intent.getSerializableExtra("bookmarkId");
        bookmarkPos = (HashMap<String, Integer>) intent.getSerializableExtra("bookmarkPos");
        //2. pass value to the model view
        model.setData(parseVideoData());
        model.setSect(parseSectionData());
        model.getRecyclerState();
    }
    private ArrayList<String> parseVideoData() throws JSONException {

        /**
         *  From Database ->  0:Title 1:Date
         *  From JSON -> 2： p1 score 1  3： p2 score1  4： p1 score 2  5：p2 score 2
         *  6：p1 score 3  7：p2 score3 (if exist)
         */

        scoreInfo = new ArrayList<>();
        ArrayList<String> lst = new ArrayList<>();
        String response = SampleProvider.totalInfoSample();  //replaced by GET TOTAL INFO API
        JSONObject json = new JSONObject(response);

        JSONArray games = json.getJSONArray("games");

        //尚未寫取得標題與分析日期的API
        lst.add("YONEX All England 2022");
        lst.add("2022/03/19");

        for(int i = 0; i < games.length(); i++){
            scoreInfo.add( new Integer(games.getJSONObject(i).getInt("score count")) );
            lst.add( games.getJSONObject(i).getJSONArray("top bot score").getInt(0) + "" );
            lst.add( games.getJSONObject(i).getJSONArray("top bot score").getInt(1) + "" );
        }

        if(games.length() == 2){
            lst.add("");
            lst.add("");
        }

        return lst;

    }

    private ArrayList<List<Object>> parseSectionData() throws JSONException {
        ArrayList<List<Object>> lst = new ArrayList<>();
        String response = SampleProvider.sectionInfoSample();  //replaced by GET Section INFO API
        JSONArray jsonArray = new JSONArray(response);
        int totalScore = scoreInfo.stream().mapToInt(Integer::intValue).sum();

        for (int i = 0; i < totalScore; i++){
            boolean winner = jsonArray.getJSONObject(i).getBoolean("winner");
            int top = jsonArray.getJSONObject(i).getJSONArray("top bot score").getInt(0);
            int bot = jsonArray.getJSONObject(i).getJSONArray("top bot score").getInt(1);
            lst.add(Arrays.asList(top + "：" + bot, winner));
        }

        return lst;
    }

    private void setURL(){
        String base = BuildConfig.SVR_IP;      //res location in server
        url = base + vid + "/" + vid;
        url += ".mp4";
        Log.d("URL: ",url);
    }

    //bind with View model
    private void setViewModel(){

        model = new ViewModelProvider(this).get(FragmentViewModel.class);
        //real data is from server
        model.getData();
        model.getSect();
        model.getMainTabState();
        model.getSubTabState();
        model.getPlayTime();
    }

    private void initFrag(){

        String parsableStr = SampleProvider.sectionInfoSample();

        VideoDataFragment dataFrag = VideoDataFragment.newInstance(vid, model.getData().getValue(),bookmarkId, bookmarkPos);
        PartSelectionFragment sectFrag1 = PartSelectionFragment.newInstance(vid, parsableStr, 1,0, scoreInfo.get(0));
        PartSelectionFragment sectFrag2 = PartSelectionFragment.newInstance(vid, parsableStr, 2, scoreInfo.get(0), scoreInfo.get(1));

        fragList = new ArrayList<>();
        fragList.add(dataFrag);
        fragList.add(sectFrag1);
        fragList.add(sectFrag2);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_view, fragList.get(0), "f_data");
        ft.add(R.id.frag_view, fragList.get(1), "f_sect_1");
        ft.add(R.id.frag_view, fragList.get(2), "f_sect_2");
        ft.hide(fragList.get(1));
        ft.hide(fragList.get(2));

        if(scoreInfo.size() == 3) {
            PartSelectionFragment sectFrag3 = PartSelectionFragment.newInstance(vid, parsableStr, 3,
                                            scoreInfo.get(0) + scoreInfo.get(1), scoreInfo.get(2));
            fragList.add(sectFrag3);
            ft.add(R.id.frag_view, fragList.get(3), "f_sect_3");
            ft.hide(fragList.get(3));
        }

        ft.commit();

    }

    private void setMainTab(){
        tLayout = findViewById(R.id.tab_main);
        FragmentManager fm = getSupportFragmentManager();

        //1.Set listener
        tLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchMainFrag();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.show(fragList.get(tab.getPosition()));
//                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.hide(fragList.get(tab.getPosition()));
//                ft.commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    private void switchMainFrag(){


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(model.getMainTabState().getValue() == 0){
            ft.hide(fragList.get(0));
            ft.show(fragList.get(1));
            model.setMainTabState(1);
            currentSubTab = 1;
        }
        else{
            ft.hide(fragList.get(1));
            ft.hide(fragList.get(2));
            if(scoreInfo.size() == 3) {
                ft.hide(fragList.get(3));
            }
            ft.show(fragList.get(0));
            model.setMainTabState(0);
        }

        ft.commit();
    }

    private void setSectTab(){

        List<PartSelectionFragment>sectFrags;
        if(scoreInfo.size() == 3){
            sectFrags = Arrays.asList((PartSelectionFragment)fragList.get(1),
                                     (PartSelectionFragment)fragList.get(2),
                                     (PartSelectionFragment)fragList.get(3));
        }
        else{
            sectFrags = Arrays.asList((PartSelectionFragment)fragList.get(1),
                                      (PartSelectionFragment)fragList.get(2));
        }

        for(Fragment fragSect: sectFrags){

            if(fragSect!=null) {
                View view = fragSect.getView();
                TabLayout tabLayout = view.findViewById(R.id.tab_sect);

                if(scoreInfo.size() == 3){
                    tabLayout.addTab(new TabLayout.Tab().setText("第三局"));
                }


                listener = new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        int pos = tab.getPosition();
                        switchSubFrag(currentSubTab, pos);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                };

                ((PartSelectionFragment)fragSect).setTabListener(listener);
                currentSubTab = 0;

        }

        subTabCreated = true;

//            tabLayout.getTabAt(1).select();
//            tabLayout.getTabAt(0).select();
        }
    }

    private void switchSubFrag(int from, int to){

        Fragment hiddenFrag = fragList.get(from + 1);
        Fragment poppedFrag = fragList.get(to + 1);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(hiddenFrag);
        ft.show(poppedFrag);
        ft.commit();

        currentSubTab = to;
        ((PartSelectionFragment)hiddenFrag).keepTab();
    }


    private void initPlayer(){
        setURL();
        player = new com.google.android.exoplayer2.ExoPlayer.Builder(this).build();
        playerView = findViewById(R.id.exo_viewer);
        playerView.setFullscreenButtonClickListener(
                isFullScreen -> {

                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    }
                    else{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }

                }
        );
        playerView.setPlayer(player);
        DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);
        Uri uri = Uri.parse(url);
        MediaSource src = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
        player.setMediaSource(src);
        player.prepare();
    }

    private void releasePlayer(){
        currentTime = player.getCurrentPosition();
        model.setPlayTime(currentTime);
        player.setPlayWhenReady(false);
        player.stop();
        playerView.onPause();
        player.release();
    }

    public String getVid(){
        return vid;
    }

    public FragmentViewModel getViewModel(){
        return model;
    }

    public String getUserName(){
        return userDao.getUser().getName();
    }
}