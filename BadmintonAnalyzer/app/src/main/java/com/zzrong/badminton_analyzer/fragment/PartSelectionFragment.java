package com.zzrong.badminton_analyzer.fragment;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.tabs.TabLayout;
import com.zzrong.badminton_analyzer.R;
import com.zzrong.badminton_analyzer.viewModel.SectionViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartSelectionFragment extends Fragment {

    String id;
    String data;
    TabLayout tabLayout;
    List<int[]> scores;
    List<Fragment> fragList;
    SectionViewModel model;

    public static PartSelectionFragment newInstance(String vid, String parsableStr, ArrayList<Integer> scoreInfo){
        PartSelectionFragment fragment = new PartSelectionFragment();

        Bundle args = new Bundle();
        args.putString("id", vid);
        args.putString("data", parsableStr);
        args.putIntegerArrayList("scores", scoreInfo);
        fragment.setArguments(args);

        return  fragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("debug: ", "PartFrag:onCreate()");
        initialize();
        initRecyclerFragments();
    }

    public void onResume(){
        super.onResume();
        Log.d("debug: ", "PartFrag:onResume()");
        setTab();
        restoreTab();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_part_selection, container, false);
    }

    public void initialize(){
        if(getArguments()!=null){
            id = getArguments().getString("id");
            data = getArguments().getString("data");
            ArrayList<Integer> arr = getArguments().getIntegerArrayList("scores");
            if(arr.size() == 2) {
                int[] game1 = new int[] {0, arr.get(0) - 1};
                int[] game2 = new int[] {arr.get(0), arr.stream().mapToInt(Integer::intValue).sum() - 1};
                scores = Arrays.asList(game1, game2);
            }
            else{
                int[] game1 = new int[] {0, arr.get(0) - 1};
                int[] game2 = new int[] {arr.get(0), arr.get(0) + arr.get(1) - 1};
                int[] game3 = new int[] {arr.get(0) + arr.get(1), arr.stream().mapToInt(Integer::intValue).sum() - 1};
                scores = Arrays.asList(game1, game2, game3);
            }
        }

        model = new ViewModelProvider(getActivity()).get(SectionViewModel.class);
        model.getTabState();
        model.getSect();
//        ((ExoPlayer) getActivity()).getViewModel().getSect();
//        if(((ExoPlayer)getActivity()).getViewModel().getSect()!=null) {
//            model.setSectList(((ExoPlayer) getActivity()).getViewModel().getSect().getValue());
//        }
    }

    public void setTab(){
        tabLayout = getView().findViewById(R.id.tab_sect);

        if(scores.size()==3){
            tabLayout.addTab(new TabLayout.Tab().setText("第三局"), false);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switchFrag(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    public void initRecyclerFragments(){

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(scores.size()==2) {
            SubSectionFragment f1 = SubSectionFragment.newInstance(data, scores.get(0));
            SubSectionFragment f2 = SubSectionFragment.newInstance(data, scores.get(1));
            fragList = Arrays.asList(f1,f2);

            ft.replace(R.id.sectionFrame, fragList.get(0), "f_game_1");
            ft.add(R.id.sectionFrame, fragList.get(1), "f_game_2");
            ft.hide(fragList.get(1));
        }
        else if(scores.size()==3){
            SubSectionFragment f1 = SubSectionFragment.newInstance(data,scores.get(0));
            SubSectionFragment f2 = SubSectionFragment.newInstance(data,scores.get(1));
            SubSectionFragment f3 = SubSectionFragment.newInstance(data,scores.get(2));
            fragList = Arrays.asList(f1,f2,f3);

            ft.replace(R.id.sectionFrame, fragList.get(0), "f_game_1");
            ft.add(R.id.sectionFrame, fragList.get(1), "f_game_2");
            ft.add(R.id.sectionFrame, fragList.get(2), "f_game_3");
            ft.hide(fragList.get(1));
            ft.hide(fragList.get(2));
        }

        ft.commit();

    }

    public void restoreTab(){
        int n = model.getTabState().getValue();
        switchFrag(n);
        switchFrag(0);
        tabLayout.getTabAt(n).select();
    }

    public void switchFrag(int pos){

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.hide(fragList.get(model.getTabState().getValue()));
        ft.show(fragList.get(pos));
        ft.commit();

        model.setTabState(pos);
    }

}