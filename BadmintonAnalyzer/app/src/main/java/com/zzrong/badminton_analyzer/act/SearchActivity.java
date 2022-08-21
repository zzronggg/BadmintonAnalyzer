package com.zzrong.badminton_analyzer.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.func.*;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel model;
    private ArrayList<VideoItem> videoItems;
    private RecyclerView recyclerView;
    private VideoItemAdapter adapter;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initialize();
        setListener();
        setViewModel();
    }

    public void initialize(){
        videoItems = new ArrayList<>();
        btn = findViewById(R.id.btn_search);
        model = new ViewModelProvider(this).get(SearchViewModel.class);
    }

    public void setListener(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoItems.clear();
                search(view);
//                fakeSearch(view);
                if(videoItems.size() == 0 ){
                    Toast toast = Toast.makeText(SearchActivity.this, "No Result, Please try another keyword that is relevant to badminton", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    public void setViewModel(){
        model.getItems().observe(this, new Observer<ArrayList<VideoItem>>() {

            @Override
            public void onChanged(ArrayList<VideoItem> videoItems) {
                Log.d("ModelViewState: ","detect change");
                createRecyclerView();
            }

        });
        createRecyclerView();
    }

    public void search(View view) {
        String kw = ((com.google.android.material.textfield.TextInputEditText)findViewById(R.id.etSearch)).getText().toString();
        Search s = new Search();

        Runnable r = new Runnable() {
            @Override
            public void run() { s.fetch(kw); }
        };

        Thread t = new Thread(r);
        t.start();
        //確認Thread結束再繼續
        try{
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.interrupt();

        for(int i = 0; i < s.getId().size(); i++){
            videoItems.add(new VideoItem(s.getId().get(i), s.getTitle().get(i), s.getThumbnail().get(i), s.getDescription().get(i)))    ;
        }
        model.setItems(videoItems);
    }

    public void fakeSearch(View view){
        model.setItems(VideoItemSample.getSample());
    }

    protected void createRecyclerView(){
        recyclerView = findViewById(R.id.searchRecycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new VideoItemAdapter(this, model.getItems().getValue());
        recyclerView.setAdapter(adapter);
    }
}