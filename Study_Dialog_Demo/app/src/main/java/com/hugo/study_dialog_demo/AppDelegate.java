package com.hugo.study_dialog_demo;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.hugo.study_dialog_demo.utils.MyViewModel;

public class AppDelegate {
    private Application application;

    public void init(Application application) {
        this.application = application;


        ViewModelStore viewModelStore = new ViewModelStore();
        ViewModelProvider viewModelProviderNoContext = new ViewModelProvider(viewModelStore, new ViewModelProvider.NewInstanceFactory());
        ViewModelProvider viewModelProvider = new ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory.getInstance(application));
        ViewModel viewModel = viewModelProvider.get(MyViewModel.class);


        viewModelStore.clear();

    }
}
