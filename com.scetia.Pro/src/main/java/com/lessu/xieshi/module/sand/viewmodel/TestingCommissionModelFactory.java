package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * created by ljs
 * on 2020/12/30
 */
public class TestingCommissionModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private LifecycleOwner lifecycleOwner;

    public TestingCommissionModelFactory(Application application, LifecycleOwner lifecycleOwner) {
        this.application = application;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SandTestingCommissionDetailViewModel(application,lifecycleOwner);
    }
}
