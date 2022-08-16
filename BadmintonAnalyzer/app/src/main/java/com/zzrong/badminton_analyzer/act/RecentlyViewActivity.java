package com.zzrong.badminton_analyzer.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.func.ServerVideoAdapter;
import com.zzrong.badminton_analyzer.func.VideoItem;
import com.zzrong.badminton_analyzer.func.VideoItemSample;

import java.util.ArrayList;

public class RecentlyViewActivity extends AppCompatActivity {

    private ArrayList<VideoItem> videoItems;
    private RecyclerView recyclerView;
    private ServerVideoAdapter adapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_view);
        initialize();
        applyListener();
        setFakeVideo();
        createRecyclerView();
    }

    public void initialize(){
        fab = findViewById(R.id.fab_recently_view);
    }

    public void applyListener(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickFAB(view);
            }
        });
    }

    protected void createRecyclerView(){
        recyclerView = findViewById(R.id.resRecycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new ServerVideoAdapter(this, videoItems);
        recyclerView.setAdapter(adapter);
    }

    protected void setFakeVideo(){
        videoItems = VideoItemSample.rctlySample();
    }

    protected void clickFAB(View view){
        Context current = view.getContext();
        Intent intent = new Intent(current,SearchActivity.class);
        current.startActivity(intent);
    }
}