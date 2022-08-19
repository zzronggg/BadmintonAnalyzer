package com.zzrong.badminton_analyzer.frag;

import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zzrong.badminton_analyzer.R;

import java.util.ArrayList;

public class VideoDataFragment extends Fragment {

    String s0;
    String s1;
    String s2;
    String s3;

    public VideoDataFragment(){

    }

    public static VideoDataFragment newInstance(ArrayList<String> arr){
        VideoDataFragment fragment = new VideoDataFragment();
        Bundle args = new Bundle();
        args.putString("s0", arr.get(0));
        args.putString("s1", arr.get(1));
        args.putString("s2", arr.get(2));
        args.putString("s3", arr.get(3));
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            s0 = getArguments().getString("s0");
            s1 = getArguments().getString("s1");
            s2 = getArguments().getString("s2");
            s3 = getArguments().getString("s3");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_data, container, false);
        ((TextView)v.findViewById(R.id.tv_frag_data_1)).setText("名稱　　：");
        ((TextView)v.findViewById(R.id.tv_frag_data_5)).setText(s0 );
        ((TextView)v.findViewById(R.id.tv_frag_data_2)).setText("比分　　：");
        ((TextView)v.findViewById(R.id.tv_frag_data_6)).setText(s1);
        ((TextView)v.findViewById(R.id.tv_frag_data_3)).setText("分類　　：");
        ((TextView)v.findViewById(R.id.tv_frag_data_7)).setText(s2);
        ((TextView)v.findViewById(R.id.tv_frag_data_4)).setText(getString(R.string.uploaded_date,s3));
        return v;
    }

}