package com.zzrong.badminton_analyzer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.tabs.TabLayout;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.viewModel.MovementViewModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MovementFragment extends Fragment {

    private List<Fragment> fragList;
    private MovementViewModel model;

    public MovementFragment() {
    }

    public static MovementFragment newInstance(HashMap<String, Float> blue, HashMap<String, Float> red){
        MovementFragment frag = new MovementFragment();

        Bundle args = new Bundle();
        args.putSerializable("blue", blue);
        args.putSerializable("red", red);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movement, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setModel();
        initFrag();
    }

    @Override
    public void onResume(){
        super.onResume();
        setTab();
    }

    public void initFrag(){
        SubMovementFragment fragTop = SubMovementFragment.newInstance(
                (HashMap<String, Float>) getArguments().getSerializable("blue"), true);
        SubMovementFragment fragBot = SubMovementFragment.newInstance(
                (HashMap<String, Float>) getArguments().getSerializable("red"), false);
        fragList = Arrays.asList(fragTop, fragBot);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.movement_fragviewer, fragList.get(0), "f_move_blue");
        ft.add(R.id.movement_fragviewer, fragList.get(1), "f_move_red");
        ft.hide(fragList.get(1));

        ft.commit();
    }


    public void setModel(){
        model = new ViewModelProvider(getActivity()).get(MovementViewModel.class);
        model.getTabState();
    }

    public void setTab(){
        TabLayout tabLayout = getView().findViewById(R.id.tab_move);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchFrag(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void switchFrag(int pos){

        TabLayout tabLayout = getView().findViewById(R.id.tab_move);

        if(pos == 0){
            tabLayout.setTabTextColors(Color.parseColor("#535353"), Color.parseColor("#43B5F5"));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#43B5F5"));
        }
        else{
            tabLayout.setTabTextColors(Color.parseColor("#535353"), Color.parseColor("#FD736E"));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FD736E"));
        }

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.hide(fragList.get(model.getTabState().getValue()));
        ft.show(fragList.get(pos));
        ft.commit();

        model.setTabState(pos);
    }

    public SubMovementFragment getBlueFrag(){

        return (SubMovementFragment) fragList.get(0);
    }
    public SubMovementFragment getRedFrag(){
        return (SubMovementFragment) fragList.get(1);
    }



}