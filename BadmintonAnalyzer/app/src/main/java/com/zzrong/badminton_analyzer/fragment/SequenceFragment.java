package com.zzrong.badminton_analyzer.fragment;

import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.adapter.BallTypeItemAdapter;

import java.util.ArrayList;

public class SequenceFragment extends Fragment {

  private ArrayList<String> seq_up;
  private ArrayList<String> seq_down;

    public SequenceFragment() {

    }

    public static SequenceFragment newInstance(ArrayList<String> up, ArrayList<String> down) {
        SequenceFragment fragment = new SequenceFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("up", up);
        args.putStringArrayList("down", down);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            seq_up = getArguments().getStringArrayList("up");
            seq_down = getArguments().getStringArrayList("down");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sequence, container, false);
        setRecyclerView(v);
        return v;
    }

    private void setRecyclerView(View view){
        RecyclerView recyclerView_up = view.findViewById(R.id.up_recycler);
        recyclerView_up.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        BallTypeItemAdapter adapter = new BallTypeItemAdapter(this.getContext(), seq_up);
        recyclerView_up.setAdapter(adapter);

        RecyclerView recyclerView_down = view.findViewById(R.id.down_recycler);
        recyclerView_down.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        adapter = new BallTypeItemAdapter(this.getContext(), seq_down);
        recyclerView_down.setAdapter(adapter);

        //extend recycler width
        int n = Math.max(seq_up.size(), seq_down.size());
        final float scale = getResources().getDisplayMetrics().density; //get 1 dp scale of the device
        int dpWidthInPx  = (int) (50 * scale);
        ViewGroup.LayoutParams lp = recyclerView_up.getLayoutParams();
        lp.width = n*dpWidthInPx;
        recyclerView_up.setLayoutParams(lp);

        //test : change text color by time&position
        int pos = 3;

    }


}