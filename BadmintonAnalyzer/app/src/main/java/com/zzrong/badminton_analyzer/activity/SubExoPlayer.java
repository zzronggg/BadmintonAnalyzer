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
import com.zzrong.badminton_analyzer.fragment.MovementFragment;
import com.zzrong.badminton_analyzer.fragment.SequenceFragment;
import com.zzrong.badminton_analyzer.fragment.StatisticFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SubExoPlayer extends AppCompatActivity {
    private String vid;
    private int game;
    private int score;

    //video player
    String url;
    long currentTime;
    private com.google.android.exoplayer2.ExoPlayer player;
    private StyledPlayerView playerView;
    private ArrayList<Fragment> fragList;
    private ArrayList<HashMap<String, Object>> eachFrameInfo;
    private ArrayList<String> pred_blue;
    private ArrayList<String> pred_red;
    private HashMap<String,Integer> stat_blue;
    private HashMap<String,Integer> stat_red;
    private HashMap<String,Float> move_blue;
    private HashMap<String,Float> move_red;
    private ArrayList<HashMap<String,Object>> blueSeq;
    private ArrayList<HashMap<String,Object>> redSeq;
    private Boolean blueServe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);
    }

    protected void onStart() {
        super.onStart();
        setBar();
        prepInfo();
        initFrag();
        setURL();
        initPlayer();
        setMainTab();

        player.setPlayWhenReady(true);
//        Log.d("Status: ","Started");
    }

    protected void onResume() {         //restore player state after orientation
        super.onResume();
        setPlayerMsg();
        player.seekTo(currentTime);
    }

    protected void onPause() {
        super.onPause();
        releasePlayer();
//        Log.d("State: ","Paused");
    }

    protected void onStop() {
        super.onStop();
//        releasePlayer();
//        Log.d("State: ","Stopped");
    }

    public void onBackPressed() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.onBackPressed();
        }
    }

    private void setBar(){
        MaterialToolbar bar = findViewById(R.id.bar_exo);
        bar.setNavigationOnClickListener(v -> finish());
    }
    private void prepInfo(){
        //get data from prev. activity
        Intent intent = this.getIntent();
        vid = intent.getStringExtra("id");
        game = intent.getIntExtra("game",-1);
        score = intent.getIntExtra("score",-1);
        prepFrameInfo(intent);
        prepSeq(intent);
        prepStat(intent);
        prepMove(intent);
    }

    private void prepFrameInfo(Intent intent){

        HashMap<String, ArrayList<HashMap<String,Object>>> fData =
                (HashMap<String, ArrayList<HashMap<String,Object>>>) intent.getSerializableExtra("frame info");

        /**
         * @param eachFrameInfo
         * key:{frame, moveType, ballType, isBlueServe}
         */

        eachFrameInfo = fData.get("info");
        transformFrameInfo(eachFrameInfo); //frame number to millisecond
    }

    private void transformFrameInfo(ArrayList<HashMap<String, Object>> mapArr){
        blueServe = (Boolean) mapArr.get(0).get("isBlueServe");

        for(HashMap<String, Object> map : mapArr){
            DecimalFormat df = new DecimalFormat("####0.000");
            double d =Double.valueOf(df.format((Integer)map.get("midFrame")/ 10.0))*1000 - 500;
            map.put("midFrame", (long)d );
            d =Double.valueOf(df.format((Integer)map.get("startFrame")/ 10.0))*1000 - 500;
            map.put("startFrame", (long)d );

        }

    }


    private void prepSeq(Intent intent){
        HashMap<String, ArrayList<HashMap<String,Object>>> twoPlayerData =
                (HashMap<String, ArrayList<HashMap<String,Object>>>) intent.getSerializableExtra("shot list");

        blueSeq = twoPlayerData.get("blue");
        redSeq = twoPlayerData.get("red");

        pred_blue = new ArrayList<>();
        pred_red = new ArrayList<>();

        for(HashMap<String,Object> map : blueSeq){
            pred_blue.add((String) map.get("type"));
        }

        for(HashMap<String,Object> map : redSeq){
            pred_red.add((String) map.get("type"));
        }

    }

    private void prepStat(Intent intent){
        HashMap<String,HashMap<String,Integer>> twoPlayerData =
                (HashMap<String, HashMap<String, Integer>>) intent.getSerializableExtra("type dict");

        stat_blue = twoPlayerData.get("blue");
        stat_red = twoPlayerData.get("red");
    }

    private void prepMove(Intent intent){
        HashMap<String,HashMap<String,Float>> twoPlayerData =
                (HashMap<String, HashMap<String, Float>>) intent.getSerializableExtra("move dict");
        move_blue = twoPlayerData.get("blue");
        move_red = twoPlayerData.get("red");
    }

    private void setURL(){
        String base = BuildConfig.SVR_IP;      //res location in server
        String scoreFolder = "/scores/game_" + game + "_score_" + score + "/";
        String filename = "video.mp4";
        url = base + vid + scoreFolder + filename;
        Log.d("URL: ",url);
    }


    private void initFrag(){
        SequenceFragment f1 = SequenceFragment.newInstance(pred_blue, pred_red, blueServe);
        fragList = new ArrayList<>();
        fragList.add(f1);

        MovementFragment f2 = MovementFragment.newInstance(move_blue, move_red);
        fragList.add(f2);

        StatisticFragment f3 = StatisticFragment.newInstance(stat_blue, stat_red);
        fragList.add(f3);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_view, fragList.get(0), "f_pred");
        ft.add(R.id.frag_view, fragList.get(1), "f_move");
        ft.add(R.id.frag_view, fragList.get(2), "f_stat");
        ft.hide(fragList.get(0));
        ft.hide(fragList.get(1));
        ft.hide(fragList.get(2));
        ft.commit();
    }

    private void setMainTab(){
        TabLayout tLayout = findViewById(R.id.tab_main);
        FragmentManager fm = getSupportFragmentManager();

        //1.Set listener
        tLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.show(fragList.get(tab.getPosition()));
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(fragList.get(tab.getPosition()));
                ft.commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        2.Add tabs
        tLayout.removeAllTabs();
        tLayout.addTab(tLayout.newTab().setText("球路序列"),false);
        tLayout.addTab(tLayout.newTab().setText("移動路徑"),false);
        tLayout.addTab(tLayout.newTab().setText("統計資料"),false);
        tLayout.getTabAt(2).select();
        tLayout.getTabAt(1).select();
        tLayout.getTabAt(0).select();
    }

    private void initPlayer(){
        player = new com.google.android.exoplayer2.ExoPlayer.Builder(this).build();
        playerView = findViewById(R.id.exo_viewer);
        playerView.setFullscreenButtonClickListener(
                isFullScreen -> {
                    // Handle transition to/from fullscreen
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    else{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }

                });
        playerView.setPlayer(player);

        DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);

        Uri uri = Uri.parse(url);
        MediaSource src = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uri));
        player.setMediaSource(src);
        player.prepare();
    }

    private void releasePlayer(){
        currentTime = player.getCurrentPosition();
        player.setPlayWhenReady(false);
        player.stop();
        playerView.onPause();
        player.release();
    }


    private void setPlayerMsg(){
        postFrameMsg();
        postEndMsg();
    }

    private void postFrameMsg(){

//        Log.d("TAG", player.getCurrentPosition() + "");
        for(HashMap<String, Object> frameMap : eachFrameInfo){
            int idx = (int) frameMap.get("index");
            long startTime = (long) frameMap.get("startFrame");
            long midTime = (long) frameMap.get("midFrame");
            String ball = (String) frameMap.get("ballType");
            String move = (String) frameMap.get("moveType");
            boolean isBlueServe = (Boolean) frameMap.get("isBlueServe");

            sendSeqMsg(midTime,idx, isBlueServe);
            sendMoveMsg(startTime, move, isBlueServe, game); //serve player 與 move player 相反
        }

    }

    private void sendSeqMsg(long time, int idxShotList, Boolean isBluePlayerServed){

        player.createMessage((messageType, payload) -> updateSeqRecycler(time, idxShotList, isBluePlayerServed))
                .setPosition(time)
                .setLooper(this.getMainLooper())
                .send();
    }

    private void updateSeqRecycler(long time, int idx, Boolean isBluePlayerServed){
        SequenceFragment seqFrag = (SequenceFragment) fragList.get(0);
        seqFrag.makeAllSeqBlack();

        /*
        blue serve    -----  idx of this shot in shot list
        blue recycler:  0 -> 2 -> ...  ( idx % 2 == 0 )
        red recycler:   1 -> 3 -> ...  ( idx % 2 == 1 )

        red serve
        blue recycler:  1 -> 3 -> ...
        red recycler:   0 -> 2 -> ...
         */

        int pos;

        pos = idx / 2;
        seqFrag.updateCurrentSeq(pos, isBluePlayerServed);

        //repeatedly register event
        sendSeqMsg(time, idx, isBluePlayerServed);
    }


    private void sendMoveMsg(long time, String moveType, Boolean bluePlayerServeFirst, int game){

        player.createMessage((messageType, payload) -> updateMovePanel(time, moveType, bluePlayerServeFirst, game))
                .setPosition(time)
                .setLooper(this.getMainLooper())
                .send();
    }

    private void updateMovePanel(long time, String moveType, Boolean isBluePlayerServed, int game){
        MovementFragment moveFrag = (MovementFragment) fragList.get(1);
        moveFrag.getBlueFrag().showAllView(false);
        moveFrag.getRedFrag().showAllView(false);

        if(isBluePlayerServed) {
            moveFrag.getRedFrag().setCurrentTimeData(moveType);
        }
        else{
            moveFrag.getBlueFrag().setCurrentTimeData(moveType);
        }

        //repeatedly register event
        sendMoveMsg(time, moveType, isBluePlayerServed, game);
    }

    private void testFunc(String moveType){
        MovementFragment moveFrag = (MovementFragment) fragList.get(1);
        moveFrag.getBlueFrag().showAllView(false);
        moveFrag.getRedFrag().showAllView(false);
        moveFrag.getBlueFrag().setCurrentTimeData(moveType);

        //
        postFrameMsg();
    }

    private void postEndMsg(){
        long time = player.getDuration();
        player.createMessage(
                        (messageType, payload) -> showGlobalData())
                .setPosition(time)
                .setLooper(this.getMainLooper())
                .send();


    }

    private void showGlobalData(){
//        Log.d("TAG", "Show Global");
        //1.sequence frag
        SequenceFragment seqFrag = (SequenceFragment) fragList.get(0);
        seqFrag.restoreAllSeq();
//        seqFrag

        //2.movement frag
        MovementFragment moveFrag = (MovementFragment) fragList.get(1);
        moveFrag.getBlueFrag().showAllView(true);
        moveFrag.getRedFrag().showAllView(true);

        //3.

        //register again
        postEndMsg();
    }



}