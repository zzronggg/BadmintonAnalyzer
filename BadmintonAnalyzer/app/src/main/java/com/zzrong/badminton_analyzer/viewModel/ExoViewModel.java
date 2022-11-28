package com.zzrong.badminton_analyzer.viewModel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExoViewModel extends ViewModel {

    public MutableLiveData<Long> playTime;
    public MutableLiveData<ArrayList<String>> videoData;
    public MutableLiveData<ArrayList<List<Object>>> sectionList;

    public MutableLiveData<ArrayList<HashMap<String, ArrayList<HashMap<String,Object>>>>> shotList;
    public MutableLiveData<ArrayList<String>> stat;
    public MutableLiveData<Boolean[]> sectRecyclerState;

    public MutableLiveData<Integer> mainTabState;
    public MutableLiveData<Integer> subTabState;

    public LiveData<ArrayList<String>> getData() {
        if (videoData == null) {
            videoData = new MutableLiveData<>();
            videoData.setValue(new ArrayList<>());
        }
        return videoData;
    }
    public LiveData<ArrayList<List<Object>>> getSect() {
        if (sectionList == null) {
            sectionList = new MutableLiveData<>();
            sectionList.setValue(new ArrayList<>());
        }
        return sectionList;
    }

    public LiveData<Integer> getMainTabState(){
        if(mainTabState == null){
            mainTabState = new MutableLiveData<>();
            mainTabState.setValue(0);
        }
        return mainTabState;
    }

    public LiveData<ArrayList<HashMap<String, ArrayList<HashMap<String,Object>>>>> getShotList() {
        if (shotList == null) {
            shotList = new MutableLiveData<>();
            shotList.setValue(new ArrayList<>());
        }
        return shotList;
    }

    public LiveData<Boolean[]> getRecyclerState() {
        if (sectRecyclerState == null) {
            sectRecyclerState = new MutableLiveData<>();
            //must be invoked when there is data in sectTime
            Log.d("postest: ", sectionList.getValue().size() + "");
            Boolean[] arr = new Boolean[sectionList.getValue().size()];
            Arrays.fill(arr,Boolean.FALSE);
            sectRecyclerState.setValue(arr);
        }
        return sectRecyclerState;
    }

    public LiveData<Long> getPlayTime() {
        if (playTime == null) {
            playTime = new MutableLiveData<>();
            playTime.setValue(Long.valueOf("0"));
        }
        return playTime;
    }

    public void setData(ArrayList<String> arr) { this.videoData.setValue(arr); }
    public void setMainTabState(Integer position){ this.mainTabState.setValue(position);}
    public void setSect(ArrayList<List<Object>> arr) {
        this.sectionList.setValue(arr);
    }
    public void setShotList(ArrayList<HashMap<String, ArrayList<HashMap<String,Object>>>> map) {
        this.shotList.setValue(map);
    }
    public void setStat(ArrayList<String> arr) {
        this.stat.setValue(arr);
    }
    public void setPlayTime(Long seekTime) {
        this.playTime.setValue(seekTime);
    }


    //記錄點擊過的sectItem
    public void updateRecyclerState(int idx){
        (this.sectRecyclerState.getValue())[idx] = true;
    }

}
