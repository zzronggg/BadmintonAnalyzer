package com.zzrong.badminton_analyzer.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zzrong.badminton_analyzer.R;

import java.util.HashMap;

public class SubMovementFragment extends Fragment {
    private boolean isTop;
    private HashMap<String, Float> data;
    HashMap<String, View[]> map;

    public SubMovementFragment() {
    }

    public static SubMovementFragment newInstance(HashMap<String, Float> map, boolean isTopPlayer) {
        SubMovementFragment fragment = new SubMovementFragment();
        Bundle args = new Bundle();
        args.putBoolean("top", isTopPlayer);
        args.putSerializable("data", map);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isTop = getArguments().getBoolean("top");
            data = (HashMap<String, Float>) getArguments().getSerializable("data");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        ViewGroup parent = getView().findViewById(R.id.sub_movement_view);
        parent.addView(setGraph());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_movement, container, false);
    }

    public View setGraph(){
        View graphView = getLayoutInflater().inflate(R.layout.panel_movement, null, false);
        View[] noMove = new View[]{graphView.findViewById(R.id.tv_no_move), graphView.findViewById(R.id.iv_no_move)};
        View[] up1 = new View[]{graphView.findViewById(R.id.tv_up_arrow_1), graphView.findViewById(R.id.iv_up_arrow_1)};
        View[] up2 = new View[]{graphView.findViewById(R.id.tv_up_arrow_2), graphView.findViewById(R.id.iv_up_arrow_2)};
        View[] down1 = new View[]{graphView.findViewById(R.id.tv_down_arrow_1), graphView.findViewById(R.id.iv_down_arrow_1)};
        View[] down2 = new View[]{graphView.findViewById(R.id.tv_down_arrow_2), graphView.findViewById(R.id.iv_down_arrow_2)};
        View[] left1 = new View[]{graphView.findViewById(R.id.tv_left_arrow_1), graphView.findViewById(R.id.iv_left_arrow_1)};
        View[] left2 = new View[]{graphView.findViewById(R.id.tv_left_arrow_2), graphView.findViewById(R.id.iv_left_arrow_2)};
        View[] right1 = new View[]{graphView.findViewById(R.id.tv_right_arrow_1), graphView.findViewById(R.id.iv_right_arrow_1)};
        View[] right2 = new View[]{graphView.findViewById(R.id.tv_right_arrow_2), graphView.findViewById(R.id.iv_right_arrow_2)};
        View[] upRight1 = new View[]{graphView.findViewById(R.id.tv_up_right_arrow_1), graphView.findViewById(R.id.iv_up_right_arrow_1)};
        View[] upRight2 = new View[]{graphView.findViewById(R.id.tv_up_right_arrow_2), graphView.findViewById(R.id.iv_up_right_arrow_2)};
        View[] upLeft1 = new View[]{graphView.findViewById(R.id.tv_up_left_arrow_1), graphView.findViewById(R.id.iv_up_left_arrow_1)};
        View[] upLeft2 = new View[]{graphView.findViewById(R.id.tv_up_left_arrow_2), graphView.findViewById(R.id.iv_up_left_arrow_2)};
        View[] downRight1 = new View[]{graphView.findViewById(R.id.tv_down_right_arrow_1), graphView.findViewById(R.id.iv_down_right_arrow_1)};
        View[] downRight2 = new View[]{graphView.findViewById(R.id.tv_down_right_arrow_2), graphView.findViewById(R.id.iv_down_right_arrow_2)};
        View[] downLeft1 = new View[]{graphView.findViewById(R.id.tv_down_left_arrow_1), graphView.findViewById(R.id.iv_down_left_arrow_1)};
        View[] downLeft2 = new View[]{graphView.findViewById(R.id.tv_down_left_arrow_2), graphView.findViewById(R.id.iv_down_left_arrow_2)};

        map = new HashMap<>();
        map.put("NM", noMove);
        map.put("LSF", up1);
        map.put("LLF", up2);
        map.put("LSB", down1);
        map.put("LLB", down2);
        map.put("TSL", left1);
        map.put("TLL", left2);
        map.put("TSR", right1);
        map.put("TLR", right2);
        map.put("DSFR", upRight1);
        map.put("DLFR", upRight2);
        map.put("DSFL", upLeft1);
        map.put("DLFL", upLeft2);
        map.put("DSBR", downRight1);
        map.put("DLBR", downRight2);
        map.put("DSBL", downLeft1);
        map.put("DLBL", downLeft2);

        loadDataIntoView();

        return graphView;
    }

    public void loadDataIntoView(){
        updateView(map, "NM");
        updateView(map,"LSF");
        updateView(map,"LLF");
        updateView(map,"LSB");
        updateView(map,"LLB");
        updateView(map,"TSL");
        updateView(map,"TLL");
        updateView(map,"TSR");
        updateView(map,"TLR");
        updateView(map,"DSFR");
        updateView(map,"DLFR");
        updateView(map,"DSFL");
        updateView(map,"DLFL");
        updateView(map,"DSBR");
        updateView(map,"DLBR");
        updateView(map,"DSBL");
        updateView(map,"DLBL");
    }

    //dynamic presentation
    public void setCurrentTimeData(String moveType){
        showAllView(false);
        View[] arr = map.get(moveType);
        showView(arr[0], true);
        showView(arr[1], true);
    }

    public void updateView(HashMap map, String moveType){

        View[] arr = (View[]) map.get(moveType);

        if(data.get(moveType).floatValue() == 0.0){
            showView(arr[0], false);
            showView(arr[1], true);
            ((ImageView) arr[1]).setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.light_gray)));
        }
        else{
            showView(arr[0], true);
            showView(arr[1], true);

            if(isTop) {
                ((ImageView) arr[1]).setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.dark_blue)));
            }
            else{
                ((ImageView) arr[1]).setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.elegant_red)));
            }
        }

        ((TextView)arr[0]).setText(data.get(moveType).floatValue() + "%");
    }

    public void showView(View view, boolean bool){
        if(bool){
            view.setVisibility(View.VISIBLE);
        }
        else{
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void showAllView(boolean bool){
        if(bool){
            loadDataIntoView();
        }
        else{
            for(String key : map.keySet()){
                View[] arr = map.get(key);
                showView(arr[0], false);
                showView(arr[1], false);
            }
        }
    }


}