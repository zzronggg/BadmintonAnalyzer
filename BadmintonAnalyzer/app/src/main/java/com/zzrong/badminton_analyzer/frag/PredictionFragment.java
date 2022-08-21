package com.zzrong.badminton_analyzer.frag;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zzrong.badminton_analyzer.R;

import java.util.ArrayList;

public class PredictionFragment extends Fragment {

  private ArrayList<String> content;

    public PredictionFragment() {

    }

    public static PredictionFragment newInstance(ArrayList<String> arr) {
        PredictionFragment fragment = new PredictionFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("item", arr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            content = getArguments().getStringArrayList("item");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_prediction, container, false);
//        ((TextView)v.findViewById(R.id.predText)).setText(content.get(0));
        return v;
    }
}