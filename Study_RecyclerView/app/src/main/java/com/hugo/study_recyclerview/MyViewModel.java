package com.hugo.study_recyclerview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.blankj.utilcode.util.LogUtils;

public class MyViewModel extends AndroidViewModel {

    public MyViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh() {
        LogUtils.e("-->>");
    }
}
