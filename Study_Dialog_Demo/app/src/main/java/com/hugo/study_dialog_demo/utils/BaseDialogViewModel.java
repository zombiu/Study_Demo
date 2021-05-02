package com.hugo.study_dialog_demo.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class BaseDialogViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isDialogShow;

    public BaseDialogViewModel(@NonNull Application application) {
        super(application);
        isDialogShow = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getDialogLiveData() {
        return isDialogShow;
    }
}
