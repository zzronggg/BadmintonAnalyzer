package com.zzrong.badminton_analyzer.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import com.zzrong.badminton_analyzer.viewModel.ExoViewModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class ExoPlayer extends AppCompatActivity {
    private String vid;
    private ExoViewModel model;
    private TabLayout tLayout;
    private boolean firstCreated = true;

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
    private String parsableStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);
    }

    protected void onStart() {
        Log.d("debug: ", "ExoPlayer:onStart()");
        super.onStart();
        initDatabase();
        setBar();
        setViewModel();
        try {
            prepInfo();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(!firstCreated){
            return;
        }
        else{
            firstCreated = false;
        }
        initFrag();
        initPlayer();
        resumePlayer();
        setMainTab();
        recordVidWatched();
    }

    protected void onResume() { //restore player state after orientation
        Log.d("debug: ", "ExoPlayer:onResume()");
        super.onResume();

        //restore main tab state
        if(model.getMainTabState().getValue() == 1 && tLayout.getTabAt(1) != null){
            model.setMainTabState(0);
            tLayout.getTabAt(0).select();
            tLayout.getTabAt(1).select();
        }


    }

    private void resumePlayer(){
        try {
            if(model.getPlayTime()!=null) {
                currentTime = model.getPlayTime().getValue();
//                Log.d("Current: ", currentTime + "");
                player.seekTo(currentTime);
            }
        }
        catch (Exception e){
//            Log.e("NullPointerException: ", "first time created");
        }
        player.setPlayWhenReady(true);
    }

    protected void onPause() {
        Log.d("debug: ", "ExoPlayer:onPause()");
        super.onPause();
        releasePlayer();
        if(tLayout.getSelectedTabPosition() >= 0) {
            model.setMainTabState(tLayout.getSelectedTabPosition());
        }
//        Log.d("main_position_get: ", tLayout.getSelectedTabPosition() + "");
//        Log.d("State: ","Paused");
    }

    protected void onStop() {
        super.onStop();
        Log.d("debug: ", "ExoPlayer:onStop()");
    }

    public void onBackPressed() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.onBackPressed();
        }
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

    private void prepInfo() throws JSONException, InterruptedException {
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
    private ArrayList<String> parseVideoData() throws JSONException, InterruptedException {

        /**
         *  From Database ->  0:Title 1:Date
         *  From JSON -> 2： p1 score 1  3： p2 score1  4： p1 score 2  5：p2 score 2
         *  6：p1 score 3  7：p2 score3 (if exist)
         */

        scoreInfo = new ArrayList<>();
        ArrayList<String> lst = new ArrayList<>();
//        String response = SampleProvider.totalInfoSample();  //replaced by GET TOTAL INFO API
        final String[] response = new String[1];
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
              response[0] = FlaskApiSender.getSectionInfo(vid);
            }
        });

        t.start();
        t.join();

        JSONObject json = new JSONObject(response[0]);
        JSONArray games = json.getJSONObject("total info").getJSONArray("games");

        //FakeData

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                response[0] = FlaskApiSender.getVideoInfo(vid);
            }
        });

        t.start();
        t.join();

        json = new JSONObject(response[0]);
        lst.add(json.getString("title"));
        lst.add(json.getString("time").replace("-","/"));

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

    private ArrayList<List<Object>> parseSectionData() throws JSONException, InterruptedException {
        ArrayList<List<Object>> lst = new ArrayList<>();
//        String response = SampleProvider.sectionInfoSample();  //replaced by GET Section INFO API
        String[] response = new String[1];
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                response[0] = FlaskApiSender.getSectionInfo(vid);
            }
        });

        t.start();
        t.join();

        JSONObject jsonObj = new JSONObject(response[0]);
        jsonObj = jsonObj.getJSONObject("respective scores");
        JSONArray jsonArray;
        if(scoreInfo.size() == 2) {
            JSONArray jsonArray1 = jsonObj.getJSONArray("g1");
            JSONArray jsonArray2 = jsonObj.getJSONArray("g2");
            jsonArray = ExoPlayer.mergeMultiJsonArray(jsonArray1, jsonArray2);
        }
        else{
            JSONArray jsonArray1 = jsonObj.getJSONArray("g1");
            JSONArray jsonArray2 = jsonObj.getJSONArray("g2");
            JSONArray jsonArray3 = jsonObj.getJSONArray("g3");
            jsonArray = ExoPlayer.mergeMultiJsonArray(jsonArray1, jsonArray2, jsonArray3);
        }

        parsableStr = jsonArray.toString();
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

        model = new ViewModelProvider(this).get(ExoViewModel.class);
        //real data is from server
        model.getData();
        model.getSect();
        model.getMainTabState();
        model.getPlayTime();
    }

    private void initFrag(){

//        String parsableStr = SampleProvider.sectionInfoSample();

        VideoDataFragment dataFrag = VideoDataFragment.newInstance(vid, model.getData().getValue(),bookmarkId, bookmarkPos);
        PartSelectionFragment sectFrag = PartSelectionFragment.newInstance(vid, parsableStr, scoreInfo);

        fragList = new ArrayList<>();
        fragList.add(dataFrag);
        fragList.add(sectFrag);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_view, fragList.get(0), "f_data");
        ft.add(R.id.frag_view, fragList.get(1), "f_sect");
        ft.hide(fragList.get(1));

        ft.commit();

    }

    private void setMainTab(){
        tLayout = findViewById(R.id.tab_main);
        FragmentManager fm = getSupportFragmentManager();

        if(!firstCreated){firstCreated = true;}

        tLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchMainFrag(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    private void switchMainFrag(int pos) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Log.d("fragList", fragList.size() + "");
        ft.hide(fragList.get(model.getMainTabState().getValue()));
        ft.show(fragList.get(pos));
        Log.d("main_position_set: ", pos + "");
        ft.commit();
        model.setMainTabState(pos);
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
                    else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
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

    public ExoViewModel getViewModel(){
        return model;
    }

    public String getUserName(){
        return userDao.getUser().getName();
    }
    public static JSONArray mergeMultiJsonArray(JSONArray... arrays) {
        JSONArray outArray = new JSONArray();
        for (JSONArray array : arrays)
            for (int i = 0; i < array.length(); i++)
                outArray.put(array.optJSONObject(i));
        return outArray;
    }
}