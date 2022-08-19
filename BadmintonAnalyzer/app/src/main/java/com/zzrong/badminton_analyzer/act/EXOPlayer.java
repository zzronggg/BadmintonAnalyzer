package com.zzrong.badminton_analyzer.act;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.material.tabs.TabLayout;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.frag.PartSelectionFragment;
import com.zzrong.badminton_analyzer.frag.VideoDataFragment;
import com.zzrong.badminton_analyzer.func.FragmentViewModel;
import com.zzrong.badminton_analyzer.func.VideoItemSample;

import java.util.ArrayList;

public class EXOPlayer extends AppCompatActivity {
    private String vid;
    private FragmentViewModel model;

    //video player
    String url;
    long currentTime;
    private ExoPlayer player;
    private StyledPlayerView playerView;
    private ArrayList<Fragment> fragList;

//    final String url = "http://140.119.19.35/vid/1.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);
    }

    protected void onStart() {
        super.onStart();
        setViewModel();
        prepInfo();
        initFrag();
        setURL();
        initPlayer();
        setTab();
        player.setPlayWhenReady(true);
        Log.d("State: ","Started");
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

    private void prepInfo(){
        //1. get data from prev. activity
        Intent intent = this.getIntent();
        vid = intent.getStringExtra("id");
        //2. pass value to the model view
        model.setData(VideoItemSample.dataFragSample());
    }

    private void setURL(){
        String base = "http://140.119.19.35/vid/";      //res location in server
        url = base + vid;
        url += ".mp4";
        Log.d("URL: ",url);
    }

    //bind with View model
    private void setViewModel(){

        model = new ViewModelProvider(this).get(FragmentViewModel.class);
        //real data is from our model
        model.getData().observe(this, videoData -> setDataFrag());

    }

    private void initFrag(){
        VideoDataFragment f1 = VideoDataFragment.newInstance(model.getData().getValue());

        fragList = new ArrayList<>();
        fragList.add(f1);
        fragList.add(new PartSelectionFragment());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_view, fragList.get(0), "f_data");
        ft.add(R.id.frag_view, fragList.get(1), "f_sect");
        ft.hide(fragList.get(0));
        ft.hide(fragList.get(1));
        ft.commit();
    }

    private void setTab(){
        TabLayout tLayout = findViewById(R.id.tab_main);
        FragmentManager fm = getSupportFragmentManager();

        //1.Set listener
        tLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.show(fragList.get(tab.getPosition()));
                ft.commit();
                if(tab.getPosition() == 0) {
                    model.setData(VideoItemSample.dataFragSample());
                }
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
        //2.Add tabs
        tLayout.addTab(tLayout.newTab().setText("影片資料"),false);
        tLayout.addTab(tLayout.newTab().setText("段落選擇"),false);
        tLayout.getTabAt(0).select();
    }

    private void setDataFrag(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag("f_data");
        if(frag != null) {
            TextView tv1 = frag.getView().findViewById(R.id.tv_frag_data_5);
            TextView tv2 = frag.getView().findViewById(R.id.tv_frag_data_6);
            TextView tv3 = frag.getView().findViewById(R.id.tv_frag_data_7);
            TextView tv4 = frag.getView().findViewById(R.id.tv_frag_data_4);

            tv1.setText(model.getData().getValue().get(0));
            tv2.setText(model.getData().getValue().get(1));
            tv3.setText(model.getData().getValue().get(2));
            tv4.setText(getString(R.string.uploaded_date,model.getData().getValue().get(3)));
        }
    }

    private void initPlayer(){
        player = new ExoPlayer.Builder(this).build();
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