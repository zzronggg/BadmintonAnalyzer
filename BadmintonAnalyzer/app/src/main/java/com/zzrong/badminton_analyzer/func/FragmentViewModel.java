package com.zzrong.badminton_analyzer.func;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class FragmentViewModel extends ViewModel {

    public MutableLiveData<ArrayList<String>> videoData;
    public MutableLiveData<ArrayList<String>> sectionTime;
//    public MutableLiveData<ArrayList<String>> analysis;

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
//    public LiveData<ArrayList<String>> getAnal() {
//        if (analysis == null) {
//            analysis = new MutableLiveData<>();
//            analysis.setValue(new ArrayList<>());
//        }
//        return analysis;
//    }

    public void setData(ArrayList<String> arr) { this.videoData.setValue(arr); }
    public void setSect(ArrayList<String> arr) {
        this.sectionTime.setValue(arr);
    }
//    public void setAnal(ArrayList<String> arr) {
//        this.analysis.setValue(arr);
//    }
}
