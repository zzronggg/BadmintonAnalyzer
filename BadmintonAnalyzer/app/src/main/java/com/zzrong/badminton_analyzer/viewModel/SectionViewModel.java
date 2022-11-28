package com.zzrong.badminton_analyzer.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SectionViewModel extends ViewModel {

    public MutableLiveData<Integer> tabState;
    public MutableLiveData<ArrayList<List<Object>>> sectionList;

    public LiveData<Integer> getTabState(){
        if(tabState == null){
            tabState = new MutableLiveData<>();
            tabState.setValue(0);
        }
        return tabState;
    }
    public LiveData<ArrayList<List<Object>>> getSect() {
        if (sectionList == null) {
            sectionList = new MutableLiveData<>();
            sectionList.setValue(new ArrayList<>());
        }
        return sectionList;
    }

    public void setTabState(Integer position){
        this.tabState.setValue(position);
    }

    public void setSectList(ArrayList<List<Object>> arr) {
        this.sectionList.setValue(arr);
    }


}
