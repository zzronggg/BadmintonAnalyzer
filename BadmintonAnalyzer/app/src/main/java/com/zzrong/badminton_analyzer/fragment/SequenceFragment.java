package com.zzrong.badminton_analyzer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
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
  private boolean blueServeFirst;

    public SequenceFragment() {

    }

    public static SequenceFragment newInstance(ArrayList<String> up, ArrayList<String> down, boolean isTopServe) {
        SequenceFragment fragment = new SequenceFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("up", up);
        args.putStringArrayList("down", down);
        args.putBoolean("topServe", isTopServe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            seq_up = getArguments().getStringArrayList("up");
            seq_down = getArguments().getStringArrayList("down");
            blueServeFirst = getArguments().getBoolean("topServe");
//            Log.d("TAG", seq_up.toString());
//            Log.d("TAG", seq_down.toString());
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
        BallTypeItemAdapter adapter = new BallTypeItemAdapter(this.getContext(), seq_up, blueServeFirst);
        recyclerView_up.setAdapter(adapter);

        RecyclerView recyclerView_down = view.findViewById(R.id.down_recycler);
        recyclerView_down.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        adapter = new BallTypeItemAdapter(this.getContext(), seq_down, !blueServeFirst);
        recyclerView_down.setAdapter(adapter);

        //extend recycler width
        int n = Math.max(seq_up.size(), seq_down.size());
        final float scale = getResources().getDisplayMetrics().density; //get 1 dp scale of the device
        int dpWidthInPx  = (int) (50 * scale);


        ViewGroup.LayoutParams lp = recyclerView_up.getLayoutParams();
        lp.width = n*dpWidthInPx;
        recyclerView_up.setLayoutParams(lp);
    }

    public void updateCurrentSeq(int position, boolean targetIsBlue){
        RecyclerView recyclerView;

        //highlight label
        if(targetIsBlue){
            recyclerView = getView().findViewById(R.id.up_recycler);
            if(!blueServeFirst){  //非發球者的shot list第一個為 ＂＂
                position += 1;
            }
        }
        else{
            recyclerView = getView().findViewById(R.id.down_recycler);
            if(blueServeFirst){
                position += 1;
            }
        }

        TextView tv = recyclerView.getLayoutManager().
                findViewByPosition(position).findViewById(R.id.typeText);

        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.elegant_red));


        //scroll to item
        recyclerView = getView().findViewById(R.id.up_recycler);

        final float scale = getResources().getDisplayMetrics().density; //get 1 dp scale of the device
        int dpWidthInPx  = (int) (50 * scale);

        int panelWidth = getView().findViewById(R.id.scroll_view).getWidth();
        int recyclerWidth = recyclerView.getWidth();
        int itemPos = Math.max(0, position - 2) * dpWidthInPx; //pos - 2 : 為了讓他置中


        if(recyclerWidth > panelWidth){
            HorizontalScrollView scrollView = getView().findViewById(R.id.scroll_view);
            scrollView.smoothScrollTo(itemPos,0);
        }

    }

    @SuppressLint("ResourceAsColor")
    public void makeAllSeqBlack(){
        RecyclerView blueRecycler = getView().findViewById(R.id.up_recycler);
        BallTypeItemAdapter blueAdapter = (BallTypeItemAdapter) blueRecycler.getAdapter();
        RecyclerView redRecycler = getView().findViewById(R.id.down_recycler);
        BallTypeItemAdapter redAdapter = (BallTypeItemAdapter) redRecycler.getAdapter();


        int len = blueAdapter.getItemCount();
        for(int i = 0 ; i < len; i++) {
            TextView tv = blueRecycler.getLayoutManager().
                    findViewByPosition(i).findViewById(R.id.typeText);
            tv.setTextColor(R.color.black);
        }

        len = redAdapter.getItemCount();
        for(int i = 0 ; i < len; i++) {
            TextView tv = redRecycler.getLayoutManager().
                    findViewByPosition(i).findViewById(R.id.typeText);
            tv.setTextColor(R.color.black);
        }
    }

    public void restoreAllSeq(){
        RecyclerView blueRecycler = getView().findViewById(R.id.up_recycler);
        BallTypeItemAdapter blueAdapter = (BallTypeItemAdapter) blueRecycler.getAdapter();
        RecyclerView redRecycler = getView().findViewById(R.id.down_recycler);
        BallTypeItemAdapter redAdapter = (BallTypeItemAdapter) redRecycler.getAdapter();


        //restore color
        int len = blueAdapter.getItemCount();
        for(int i = 0 ; i < len; i++) {
            TextView tv = blueRecycler.getLayoutManager().
                    findViewByPosition(i).findViewById(R.id.typeText);

            if(i < len-3){
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
            else{
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.elegant_orange));
            }
        }

        len = redAdapter.getItemCount();
        for(int i = 0 ; i < len; i++) {
            TextView tv = redRecycler.getLayoutManager().
                    findViewByPosition(i).findViewById(R.id.typeText);

            if(i < len-3){
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
            else{
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.elegant_orange));
            }
        }

        //restore movement
        HorizontalScrollView scrollView = getView().findViewById(R.id.scroll_view);
        scrollView.smoothScrollTo(0,0);
    }


}