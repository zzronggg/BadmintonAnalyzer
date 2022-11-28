package com.zzrong.badminton_analyzer.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class MovementViewModel extends ViewModel {
    public MutableLiveData<Integer> tabState;

    public LiveData<Integer> getTabState(){
        if(tabState == null){
            tabState = new MutableLiveData<>();
            tabState.setValue(0);
        }
        return tabState;
    }


    public void setTabState(Integer position){
        this.tabState.setValue(position);
    }
}
