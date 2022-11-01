package com.zzrong.badminton_analyzer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.room.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.zzrong.badminton_analyzer.BuildConfig;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.func.FlaskApiSender;
import com.zzrong.badminton_analyzer.func.MovableFloatingActionButton;
import com.zzrong.badminton_analyzer.room.AppDatabase;
import com.zzrong.badminton_analyzer.room.UserDao;
import com.zzrong.badminton_analyzer.room.Video;
import com.zzrong.badminton_analyzer.room.VideoDao;

public class YouTubeActivity extends YouTubeBaseActivity {

    final private String API_KEY = BuildConfig.API_KEY;

    private AppDatabase database;
    private UserDao userDao;
    private VideoDao videoDao;
    private Intent intent;
    private FloatingActionButton fab;
    private YouTubePlayerView playerView;
    private YouTubePlayer player;
    private String vid;
    private String title;
    private String pic;

    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytactivity);
        initialize();
        applyListener();
        setBtnListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            time = player.getCurrentTimeMillis();
            intent.putExtra("time", time);
        }
        catch (NullPointerException e){

        }
    }

    protected void initialize(){
        playerView = findViewById(R.id.yt_player);
        intent = this.getIntent();//取得傳遞過來的資料
        vid = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        pic = intent.getStringExtra("thumb");
        time = intent.getIntExtra("time",0);

        fab = findViewById(R.id.fab_youtube_player);

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "info").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao =  database.userDao();
        videoDao = database.videoDao();
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

    private void setBtnListener(){
        MovableFloatingActionButton fab = findViewById(R.id.fab_youtube_player);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar snackbar = Snackbar.make(findViewById(R.id.yt_layout), "開始下載...", Snackbar.LENGTH_SHORT);
                snackbar.show();

                //download video from youtube
                Runnable r = () -> FlaskApiSender.addVid(vid,title);
                Thread t = new Thread(r);
                t.start();

                //link this video with user in Server DB


                //add this video to local DB
                long millis=System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);
                videoDao.insert(new Video(vid,title,pic,date.toString()));
            }
        });
    }

}