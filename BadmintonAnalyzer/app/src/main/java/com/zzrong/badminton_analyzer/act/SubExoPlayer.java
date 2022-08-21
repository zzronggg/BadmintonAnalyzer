package com.zzrong.badminton_analyzer.act;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
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
import com.zzrong.badminton_analyzer.frag.PredictionFragment;
import com.zzrong.badminton_analyzer.frag.StatisticFragment;
import com.zzrong.badminton_analyzer.func.*;

import java.util.ArrayList;

public class SubExoPlayer extends AppCompatActivity {
    private String vid;
    private String sect;
    private FragmentViewModel model;

    //video player
    String url;
    long currentTime;
    private com.google.android.exoplayer2.ExoPlayer player;
    private StyledPlayerView playerView;
    private ArrayList<Fragment> fragList;
    private ArrayList<String> pred;
    private ArrayList<String> stat;
    private Boolean isPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);
    }

    protected void onStart() {
        super.onStart();
        setBar();
//        setViewModel();
        prepInfo();
        initFrag();
        setURL();
        initPlayer();
        setTab();
        player.setPlayWhenReady(true);
        Log.i("Status: ","Started");
    }

    protected void onResume() {         //restore player state after orientation
        super.onResume();
        player.seekTo(currentTime);
    }

    protected void onPause() {
        super.onPause();
        releasePlayer();
        Log.d("State: ","Paused");
    }

    protected void onStop() {
        super.onStop();
        releasePlayer();
        Log.d("State: ","Stopped");
    }

    private void setBar(){
        MaterialToolbar bar = findViewById(R.id.exo_main_bar);
        bar.setNavigationOnClickListener(v -> finish());
    }
    private void prepInfo(){
        //get data from prev. activity
        Intent intent = this.getIntent();
        vid = intent.getStringExtra("id");
        sect = intent.getStringExtra("sect");
        pred = intent.getStringArrayListExtra("pred");
        stat = intent.getStringArrayListExtra("stat");

        //only generate statistic once
        isPlot=false;
    }

    private void setURL(){
        String base = BuildConfig.SVR_IP;      //res location in server
        url = base + vid + '/' + sect;
        url += ".mp4";
        Log.d("URL: ",url);
    }

    //bind with View model
    private void setViewModel(){

//        model = new ViewModelProvider(this).get(FragmentViewModel.class);
        //real data is from our model
    }

    private void initFrag(){
        PredictionFragment f1 = PredictionFragment.newInstance(pred);
        fragList = new ArrayList<>();
        fragList.add(f1);
        fragList.add(new StatisticFragment());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_view, fragList.get(0), "f_pred");
        ft.add(R.id.frag_view, fragList.get(1), "f_stat");
        ft.hide(fragList.get(0));
        ft.hide(fragList.get(1));
        ft.commit();
    }

    private void setTab(){
        TabLayout tLayout = findViewById(R.id.tab_main);
        FragmentManager fm = getSupportFragmentManager();

        if(tLayout.getTabCount()>=2) return;

        //1.Set listener
        tLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.show(fragList.get(tab.getPosition()));
                ft.commit();
                if(tab.getPosition() == 0) {}
                else{setStatFrag();}
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
        tLayout.addTab(tLayout.newTab().setText("球路預測"),false);
        tLayout.addTab(tLayout.newTab().setText("統計資料"),false);
        tLayout.getTabAt(0).select();
    }

    private void setStatFrag(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag("f_stat");
        if(frag != null && !isPlot) {
            //to-do change
//            ((TextView)frag.getView().findViewById(R.id.statText)).setText(stat.get(0));
        }
    }



    private void initPlayer(){
        player = new com.google.android.exoplayer2.ExoPlayer.Builder(this).build();
        playerView = findViewById(R.id.exo_viewer);
        playerView.setControllerOnFullScreenModeChangedListener(
                isFullScreen -> {
                    // Handle transition to/from fullscreen
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
}