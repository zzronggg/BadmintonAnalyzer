package com.zzrong.badminton_analyzer.frag;

import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zzrong.badminton_analyzer.R;

public class VideoDataFragment extends Fragment {

    TextView tv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_data, container, false);
        return v;
    }

}