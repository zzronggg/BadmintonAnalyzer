package com.zzrong.badminton_analyzer.act;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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
import com.zzrong.badminton_analyzer.frag.PartSelectionFragment;
import com.zzrong.badminton_analyzer.frag.VideoDataFragment;
import com.zzrong.badminton_analyzer.func.*;

import java.util.ArrayList;

public class ExoPlayer extends AppCompatActivity {
    private String vid;
    private FragmentViewModel model;
    private TabLayout tLayout;

    //video player
    String url;
    long currentTime;
    private com.google.android.exoplayer2.ExoPlayer player;
    private StyledPlayerView playerView;
    private ArrayList<Fragment> fragList;
    private ArrayList<String> sectItems;
    private RecyclerView recyclerView;
    private SectionItemAdapter adapter;
    private Boolean isRecyclerExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);
    }

    protected void onStart() {
        super.onStart();
        setBar();
        setViewModel();
        prepInfo();
        initFrag();
        setURL();
        initPlayer();
        setTab();
        setFakeSections();
        player.setPlayWhenReady(true);
        Log.d("State: ","Started");
    }

    protected void onResume() {         //restore player state after orientation
        super.onResume();
        if(!(model.getRecyclerState().getValue().length==0) && (recyclerView!=null))
            restoreRecyclerView(model.getRecyclerState().getValue());
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
        //1. get data from prev. activity
        Intent intent = this.getIntent();
        vid = intent.getStringExtra("id");
        //2. pass value to the model view
        model.setData(VideoItemSample.dataFragSample());
        model.setSect(VideoItemSample.sectFragSample());
        model.getRecyclerState();
        isRecyclerExist=false;
    }

    private void setURL(){
        String base = BuildConfig.SVR_IP;      //res location in server
        url = base + vid;
        url += ".mp4";
        Log.d("URL: ",url);
    }

    //bind with View model
    private void setViewModel(){

        model = new ViewModelProvider(this).get(FragmentViewModel.class);
        //real data is from our model
        model.getData().observe(this, videoData -> setDataFrag());
        model.getSect();
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
        tLayout = findViewById(R.id.tab_main);
        FragmentManager fm = getSupportFragmentManager();

        if(tLayout.getTabCount()>=2) return;
        //1.Set listener
        tLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.show(fragList.get(tab.getPosition()));
                ft.commit();
                if(tab.getPosition() == 0) {model.setData(VideoItemSample.dataFragSample());}
                else{

                    createRecyclerView(null);
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
            TextView tv4 = frag.getView().findViewById(R.id.tv_frag_data_8);

            tv1.setText(model.getData().getValue().get(0));
            tv2.setText(model.getData().getValue().get(1));
            tv3.setText(model.getData().getValue().get(2));
            tv4.setText(model.getData().getValue().get(3));
        }
    }

    private void createRecyclerView(Boolean[] b){
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag("f_sect");
        if(frag!=null&&isRecyclerExist!=true) {
            recyclerView = frag.getView().findViewById(R.id.sectRecycler);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            if(b == null)   {
                adapter = new SectionItemAdapter(this, sectItems);
            }
            else{
                adapter = new SectionItemAdapter(this, sectItems, b);
            }
            recyclerView.setAdapter(adapter);
            isRecyclerExist = true;
        }
    }

    private void restoreRecyclerView(Boolean[] state){
        Log.i("True 0: ","bool not null");
        createRecyclerView(state);
        tLayout.getTabAt(0).select();
        tLayout.getTabAt(1).select();
    }

    protected void setFakeSections(){

        sectItems = model.getSect().getValue();
        model.getRecyclerState();
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

    public String getVid(){
        return vid;
    }

    public FragmentViewModel getViewModel(){
        return model;
    }
}