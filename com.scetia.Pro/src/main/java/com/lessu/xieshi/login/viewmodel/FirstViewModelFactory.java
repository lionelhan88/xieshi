package com.lessu.xieshi.login.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * created by ljs
 * on 2020/11/30
 */
public class FirstViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private LifecycleOwner ower;
    public FirstViewModelFactory(Application application,LifecycleOwner ower) {
        this.application = application;
        this.ower = ower;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FirstViewModel(application,ower);
    }
}
