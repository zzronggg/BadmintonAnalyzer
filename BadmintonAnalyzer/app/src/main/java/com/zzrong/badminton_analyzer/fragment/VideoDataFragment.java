package com.zzrong.badminton_analyzer.fragment;

import java.util.*;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.activity.ExoPlayer;
import com.zzrong.badminton_analyzer.activity.PlayerDataActivity;



public class VideoDataFragment extends Fragment {

    String userName;
    String id;
    String title;
    String p1_1;
    String p2_1;
    String p1_2;
    String p2_2;
    String p1_3;
    String p2_3;
//    String cat;
    String date;

    //Edit Category
    HashMap<String, String> mapId;
    HashMap<String, Integer> mapPos;
//    String oldCat;  //record old state

    public VideoDataFragment(){

    }

    //title:0
    //player:1~2
    //score:3~8
    //category:9
    //analyzed date:10
    public static VideoDataFragment newInstance(String vid, ArrayList<String> arr,
                                                HashMap<String,String> hashMapId,
                                                HashMap<String,Integer> hashMapPos){
        VideoDataFragment fragment = new VideoDataFragment();
        Bundle args = new Bundle();
        args.putString("id", vid);
        args.putString("title", arr.get(0));
        args.putString("date", arr.get(1));
        args.putString("p1_1", arr.get(2));
        args.putString("p2_1", arr.get(3));
        args.putString("p1_2", arr.get(4));
        args.putString("p2_2", arr.get(5));
        args.putString("p1_3", arr.get(6));
        args.putString("p2_3", arr.get(7));
        args.putSerializable("bookmarkId", hashMapId);
        args.putSerializable("bookmarkPos", hashMapPos);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            id = getArguments().getString("id");
            title = getArguments().getString("title");
            p1_1 = getArguments().getString("p1_1");
            p2_1 = getArguments().getString("p2_1");
            p1_2 = getArguments().getString("p1_2");
            p2_2 = getArguments().getString("p2_2");
            p1_3 = getArguments().getString("p1_3");
            p2_3 = getArguments().getString("p2_3");
//            cat = getArguments().getString("category");
            date = getArguments().getString("date");
            mapId = (HashMap<String, String>) getArguments().getSerializable("bookmarkId");
            mapPos = (HashMap<String, Integer>) getArguments().getSerializable("bookmarkPos");
//            try {
//                userName = ((ExoPlayer)getContext()).getUserName();
//            }
//            catch (NullPointerException e){
//                Log.e("NullPointerException： ", "Switch to Landscape mode");
//            }

        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_video_data, container, false);

        //Set text
        EditText etTitle = v.findViewById(R.id.et_frag_data_title);
        etTitle.setText(title);

        Button btnL1 = v.findViewById(R.id.btn_left_circle_1);
        btnL1.setText(p1_1);

        Button btnL2 = v.findViewById(R.id.btn_left_circle_2);
        btnL2.setText(p1_2);

        Button btnL3 = v.findViewById(R.id.btn_left_circle_3);
        btnL3.setText(p1_3);

        Button btnR1 = v.findViewById(R.id.btn_right_circle_1);
        btnR1.setText(p2_1);

        Button btnR2 = v.findViewById(R.id.btn_right_circle_2);
        btnR2.setText(p2_2);

        Button btnR3 = v.findViewById(R.id.btn_right_circle_3);
        btnR3.setText(p2_3);

//        oldCat = cat;

        ((TextView)v.findViewById(R.id.tv_frag_data_date)).setText("分析日期：");
        ((TextView)v.findViewById(R.id.tv_frag_data_date_content)).setText(date);

        setTintList(v);
        setDataBtnListener(v);
        return v;
    }

    void setTintList(View v){

        //未加載數據
        if(((Button)v.findViewById(R.id.btn_left_circle_1)).getText().toString().isEmpty()) return;
        colorChange(v);
    }

    void colorChange(View v) {
        ColorStateList blue = ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.sugar_blue));
        ColorStateList red = ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.sugar_red));
        ColorStateList gray = ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.foggy_gray));

        int l1 = Integer.parseInt(((Button)v.findViewById(R.id.btn_left_circle_1)).getText().toString());
        int l2 = Integer.parseInt(((Button)v.findViewById(R.id.btn_left_circle_2)).getText().toString());
        int r1 = Integer.parseInt(((Button)v.findViewById(R.id.btn_right_circle_1)).getText().toString());
        int r2 = Integer.parseInt(((Button)v.findViewById(R.id.btn_right_circle_2)).getText().toString());

//      一局結束不會有同分情形，只管大於小於
        if(l1 > r1){
            ((Button)v.findViewById(R.id.btn_left_circle_1)).setBackgroundTintList(blue);
            ((Button)v.findViewById(R.id.btn_right_circle_1)).setBackgroundTintList(gray);
            ((Button)v.findViewById(R.id.btn_right_circle_1)).setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        }
        else{
            ((Button)v.findViewById(R.id.btn_right_circle_1)).setBackgroundTintList(red);
            ((Button)v.findViewById(R.id.btn_left_circle_1)).setBackgroundTintList(gray);
            ((Button)v.findViewById(R.id.btn_left_circle_1)).setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        }

        if(l2 > r2){
            ((Button)v.findViewById(R.id.btn_left_circle_2)).setBackgroundTintList(blue);
            ((Button)v.findViewById(R.id.btn_right_circle_2)).setBackgroundTintList(gray);
            ((Button)v.findViewById(R.id.btn_right_circle_2)).setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        }
        else if(r2 > l2){
            ((Button)v.findViewById(R.id.btn_right_circle_2)).setBackgroundTintList(red);
            ((Button)v.findViewById(R.id.btn_left_circle_2)).setBackgroundTintList(gray);
            ((Button)v.findViewById(R.id.btn_left_circle_2)).setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        }

        //l3 & r3 may be empty
        if(((Button)v.findViewById(R.id.btn_left_circle_3)).getText().toString().isEmpty() ||
                ((Button)v.findViewById(R.id.btn_right_circle_3)).getText().toString().isEmpty()){

            ((Button)v.findViewById(R.id.btn_left_circle_3)).setBackgroundTintList(gray);
            ((Button)v.findViewById(R.id.btn_right_circle_3)).setBackgroundTintList(gray);

            ((Button)v.findViewById(R.id.btn_right_circle_3)).setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
            ((Button)v.findViewById(R.id.btn_left_circle_3)).setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        }
        else{

            int l3 = Integer.parseInt(((Button)v.findViewById(R.id.btn_left_circle_3)).getText().toString());
            int r3 = Integer.parseInt(((Button)v.findViewById(R.id.btn_right_circle_3)).getText().toString());

            if(l3 > r3){
                ((Button)v.findViewById(R.id.btn_left_circle_3)).setBackgroundTintList(blue);
                ((Button)v.findViewById(R.id.btn_right_circle_3)).setBackgroundTintList(gray);
                ((Button)v.findViewById(R.id.btn_right_circle_3)).setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
            }
            else if(r3 > l3){
                ((Button)v.findViewById(R.id.btn_right_circle_3)).setBackgroundTintList(red);
                ((Button)v.findViewById(R.id.btn_left_circle_3)).setBackgroundTintList(gray);
                ((Button)v.findViewById(R.id.btn_left_circle_3)).setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
            }


        }
    }

    void setDataBtnListener(View view){
        Button btn1 = view.findViewById(R.id.btn_frag_data_player1);
        Button btn2 = view.findViewById(R.id.btn_frag_data_player2);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlayerDataActivity.class);
                if(v.getId() == R.id.btn_frag_data_player1){
                    intent.putExtra("is_blue", true);
                }
                else{
                    intent.putExtra("is_blue", false);
                }
                intent.putExtra("vid",((ExoPlayer)getContext()).getVid());
                getContext().startActivity(intent);
            }
        };

        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
    }

//    void hide(View view){
//        view.setAlpha(0);
//        view.setEnabled(false);
//    }
//
//    void show(View view){
//        view.setAlpha(1);
//        view.setEnabled(true);
//    }

}