package com.lessu.xieshi.module.weather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * created by ljs
 * on 2020/12/2
 */
public class WeatherModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private LifecycleOwner ower;
    public WeatherModelFactory(Application application,LifecycleOwner ower) {
        this.application = application;
        this.ower = ower;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WeatherViewModel(application,ower);
    }
}
