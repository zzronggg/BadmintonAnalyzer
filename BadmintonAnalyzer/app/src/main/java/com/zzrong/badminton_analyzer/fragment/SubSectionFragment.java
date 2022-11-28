package com.zzrong.badminton_analyzer.fragment;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.activity.ExoPlayer;
import com.zzrong.badminton_analyzer.adapter.SectionItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class SubSectionFragment extends Fragment {

    private String data;
    private int[] dataRange;
    private SectionItemAdapter adapter;

    public SubSectionFragment() {

    }

    public static SubSectionFragment newInstance(String parsableStr, int[] posInfo) {
        SubSectionFragment fragment = new SubSectionFragment();
        Bundle args = new Bundle();
        args.putString("data", parsableStr);
        args.putIntArray("pos", posInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug: ", "SubSection:onCreate()");
        if (getArguments() != null) {
            data = getArguments().getString("data");
            dataRange = getArguments().getIntArray("pos");
        }
    }

    public void onResume(){
        super.onResume();
        Log.d("debug: ", "SubSection:onResume()");
        createSectionItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_section, container, false);
    }

    public void createSectionItem(){
        ArrayList<List<Object>> total = ((ExoPlayer)getContext()).getViewModel().getSect().getValue();
        ArrayList<List<Object>> sectList = new ArrayList<>(total.subList(dataRange[0], dataRange[1]+1));

        adapter = new SectionItemAdapter(this, getContext(), sectList, dataRange[0]);

        RecyclerView recyclerView = getView().findViewById(R.id.sectRecycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    public String getData(){
        return data;
    }
}