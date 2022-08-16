package com.zzrong.badminton_analyzer.func;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {
    public MutableLiveData<ArrayList<VideoItem>> items;
    public LiveData<ArrayList<VideoItem>> getItems() {
        if (items == null) {
            items = new MutableLiveData<>();
            items.setValue(new ArrayList<>());
        }
        return items;
    }

    public void setItems(ArrayList<VideoItem> arr) {
        this.items.setValue(arr);
    }
}
