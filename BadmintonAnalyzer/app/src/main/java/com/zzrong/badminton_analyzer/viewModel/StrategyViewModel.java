package com.zzrong.badminton_analyzer.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StrategyViewModel extends ViewModel {

    public MutableLiveData<Long> playTime;

    public LiveData<Long> getPlayTime() {
        if (playTime == null) {
            playTime = new MutableLiveData<>();
            playTime.setValue(Long.valueOf("0"));
        }
        return playTime;
    }

    public void setPlayTime(Long time){
        playTime.setValue(time);
    }
}
