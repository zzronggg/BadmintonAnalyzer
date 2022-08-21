package com.zzrong.badminton_analyzer.act;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.zzrong.badminton_analyzer.BuildConfig;
import com.zzrong.badminton_analyzer.R;

public class YouTubeActivity extends YouTubeBaseActivity {

    final private String API_KEY = BuildConfig.API_KEY;

    private Intent intent;
    private FloatingActionButton fab;
    private YouTubePlayerView playerView;
    private YouTubePlayer player;
    private String vid;
    private String title;

    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytactivity);
        initialize();
        applyListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        time = player.getCurrentTimeMillis();
        intent.putExtra("time",time);
    }

    protected void initialize(){
        playerView = findViewById(R.id.yt_player);
        intent = this.getIntent();//取得傳遞過來的資料
        vid = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        time = intent.getIntExtra("time",0);

        fab = findViewById(R.id.fab_youtube_player);
    }

    protected void applyListener(){

        playerView.initialize(API_KEY, new YouTubePlayer.OnInitializedListener(){

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;
                player.loadVideo(vid,time); //cue or load
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });


    }

}