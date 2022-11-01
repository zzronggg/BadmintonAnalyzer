package com.zzrong.badminton_analyzer.fragment;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.google.android.material.tabs.TabLayout;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.activity.ExoPlayer;
import com.zzrong.badminton_analyzer.adapter.SectionItemAdapter;
import com.zzrong.badminton_analyzer.func.SampleProvider;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartSelectionFragment extends Fragment {

    String id;
    String info;
    SectionItemAdapter adapter;
    int seq;
    int start;
    int count;
    TabLayout tabLayout;
    TabLayout.OnTabSelectedListener listener;

    public static PartSelectionFragment newInstance(String vid, String parsableStr, int seq, int start, int count){
        PartSelectionFragment fragment = new PartSelectionFragment();

        Bundle args = new Bundle();
        args.putString("id", vid);
        args.putString("info", parsableStr);
        args.putInt("seq", seq);
        args.putInt("start", start);
        args.putInt("count", count);
        fragment.setArguments(args);

        return  fragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            id = getArguments().getString("id");
            info = getArguments().getString("info");
            seq = getArguments().getInt("seq");
            start = getArguments().getInt("start");
            count = getArguments().getInt("count");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_part_selection, container, false);
        tabLayout = v.findViewById(R.id.tab_sect);
        tabLayout.getTabAt(seq - 1).select();


        try {
            v = setSect(v, start, count);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return v;
    }

    public View setSect(View view, int init, int n) throws JSONException {
        RecyclerView recyclerView = view.findViewById(R.id.sectRecycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        ArrayList<List<Object>> totalItems = ((ExoPlayer)getContext()).getViewModel().getSect().getValue();

        ArrayList<List<Object>> sectItems = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(info);

        for(int i = init; i < init + n ; i ++){
            sectItems.add(totalItems.get(i));
        }

        Boolean[] state = ((ExoPlayer)getContext()).getViewModel().getRecyclerState().getValue();
//        Log.d("postest: ", state.length + "");

        if(state.length == 0)   {
            adapter = new SectionItemAdapter(getContext(), sectItems, init);
        }
        else{
            adapter = new SectionItemAdapter(getContext(), sectItems, init, state);
        }
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void setTabListener(TabLayout.OnTabSelectedListener listener){
        this.listener = listener;
        tabLayout.addOnTabSelectedListener(listener);
    }

    public void activeTabListener(boolean bool){
        if(bool){
            tabLayout.addOnTabSelectedListener(listener);
        }
        else{
            tabLayout.removeOnTabSelectedListener(listener);
        }
    }


    public void keepTab(){
        activeTabListener(false);
        tabLayout.getTabAt(seq-1).select();
        activeTabListener(true);
    }

}