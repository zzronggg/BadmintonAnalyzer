package com.zzrong.badminton_analyzer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import com.zzrong.badminton_analyzer.R;

import java.util.ArrayList;

public class MovementFragment extends Fragment {

    private ArrayList<String> up;
    private ArrayList<String> down;

    public MovementFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movement, container, false);
        View panel = inflater.inflate(R.layout.panel_movement, container, false);
        FrameLayout frameLayout = v.findViewById(R.id.frame_ground);
        frameLayout.addView(panel);
        return v;
    }


    public static MovementFragment newInstance(ArrayList<String> up, ArrayList<String> down) {
        MovementFragment fragment = new MovementFragment();
//        Bundle args = new Bundle();
//        args.putStringArrayList("up", up);
//        args.putStringArrayList("down", down);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            seq_up = getArguments().getStringArrayList("up");
//            seq_down = getArguments().getStringArrayList("down");
//        }
    }

}