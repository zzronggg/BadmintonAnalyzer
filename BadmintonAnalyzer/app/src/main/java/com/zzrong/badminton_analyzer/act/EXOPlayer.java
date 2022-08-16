package com.zzrong.badminton_analyzer.act;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.material.tabs.TabLayout;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.frag.AnalysisFragment;
import com.zzrong.badminton_analyzer.frag.PartSelectionFragment;
import com.zzrong.badminton_analyzer.frag.VideoDataFragment;
import com.zzrong.badminton_analyzer.func.FragmentViewModel;
import com.zzrong.badminton_analyzer.func.VideoItemSample;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EXOPlayer extends AppCompatActivity {
    private String vid;
    private String title;

    private FragmentViewModel model;
    private FragmentManager fm;

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
        setURL();
        initPlayer();
        initFrag();
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

    private void prepInfo(){              // get data from prev. activity
        Intent intent = this.getIntent();
        vid = intent.getStringExtra("id");
    }

    private void setURL(){
        String base = "http://140.119.19.35/vid/";      //res location in server
        url = base + vid;
        url += ".mp4";
        Log.d("URL: ",url);
    }

    //bind with View model
    private void setViewModel(){

        fm = this.getSupportFragmentManager();

        model = new ViewModelProvider(this).get(FragmentViewModel.class);
        //real data is from our model
        model.getData().observe(this, new Observer<ArrayList<String>>() {

            @Override
            public void onChanged(ArrayList<String> videoItems) {
                if(fm.findFragmentByTag("f0") != null) {
                    TextView tv1 = fm.findFragmentByTag("f0").getView().findViewById(R.id.tv_frag_data_1);
                    TextView tv2 = fm.findFragmentByTag("f0").getView().findViewById(R.id.tv_frag_data_2);
                    TextView tv3 = fm.findFragmentByTag("f0").getView().findViewById(R.id.tv_frag_data_3);
                    TextView tv4 = fm.findFragmentByTag("f0").getView().findViewById(R.id.tv_frag_data_4);
                    TextView tv5 = fm.findFragmentByTag("f0").getView().findViewById(R.id.tv_frag_data_5);

                    tv1.setText(model.getData().getValue().get(0));
                    tv2.setText(model.getData().getValue().get(1));
                    tv3.setText(model.getData().getValue().get(2));
                    tv4.setText(model.getData().getValue().get(3));
                    tv5.setText(model.getData().getValue().get(4));
                }
            }

        });

    }

    private void initFrag(){
        fragList = new ArrayList<>();
        fragList.add(new VideoDataFragment());
        fragList.add(new PartSelectionFragment());
        fragList.add(new AnalysisFragment());
    }

    public ArrayList<Fragment> getFragLst(){
        return fragList;
    }

    private void setTab(){
        TabLayout tLayout = findViewById(R.id.tab_main);
        ViewPager2 vp = findViewById(R.id.pager_main);
        MainPagerAdapter adapter = new MainPagerAdapter(this);

        //1.Set listener
        tLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
                model.setData(VideoItemSample.dataFragSample());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                model.setData(VideoItemSample.dataFragSample());
            }
        });
        //2.Add tabs
        tLayout.addTab(tLayout.newTab().setText("影片資料"));
        tLayout.addTab(tLayout.newTab().setText("段落選擇"));
        tLayout.addTab(tLayout.newTab().setText("整體分析"));

        vp.setAdapter(adapter);
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


    public class MainPagerAdapter extends FragmentStateAdapter {
        EXOPlayer context;
        public MainPagerAdapter(EXOPlayer e) {
            super(e);
            context = e;
        }
        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return context.getFragLst().get(position);
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        @Override
        public long getItemId(int position){
            return position;
        }
    }

}