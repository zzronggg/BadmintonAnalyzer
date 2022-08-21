package com.zzrong.badminton_analyzer.func;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentViewModel extends ViewModel {

    public MutableLiveData<ArrayList<String>> videoData;
    public MutableLiveData<ArrayList<String>> sectionTime;

    public MutableLiveData<ArrayList<String>> pred;

    public MutableLiveData<ArrayList<String>> stat;
    public MutableLiveData<Boolean[]> sectRecyclerState;

    public LiveData<ArrayList<String>> getData() {
        if (videoData == null) {
            videoData = new MutableLiveData<>();
            videoData.setValue(new ArrayList<>());
        }
        return videoData;
    }
    public LiveData<ArrayList<String>> getSect() {
        if (sectionTime == null) {
            sectionTime = new MutableLiveData<>();
            sectionTime.setValue(new ArrayList<>());
        }
        return sectionTime;
    }

    public LiveData<ArrayList<String>> getPred() {
        if (pred == null) {
            pred = new MutableLiveData<>();
            pred.setValue(new ArrayList<>());
        }
        return pred;
    }

    public LiveData<ArrayList<String>> getStat() {
        if (stat == null) {
            stat = new MutableLiveData<>();
            stat.setValue(new ArrayList<>());
        }
        Log.i("Stat: ", Boolean.toString(stat==null));
        return sectionTime;
    }

    public LiveData<Boolean[]> getRecyclerState() {
        if (sectRecyclerState == null) {
            sectRecyclerState = new MutableLiveData<>();
            //must be invoked when there is data in sectTime
            Boolean[] arr = new Boolean[sectionTime.getValue().size()];
            Arrays.fill(arr,Boolean.FALSE);
            sectRecyclerState.setValue(arr);
        }
        return sectRecyclerState;
    }

    public void setData(ArrayList<String> arr) { this.videoData.setValue(arr); }
    public void setSect(ArrayList<String> arr) {
        this.sectionTime.setValue(arr);
    }
    public void setPred(ArrayList<String> arr) {
        this.pred.setValue(arr);
    }
    public void setStat(ArrayList<String> arr) {
        this.stat.setValue(arr);
    }

    public void updateRecyclerState(int idx){
        (this.sectRecyclerState.getValue())[idx] = true;
    }

}
