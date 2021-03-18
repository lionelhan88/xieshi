package com.scetia.Pro.baseapp.basepage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.scetia.Pro.baseapp.lifecycle.IBaseLifecycle;
import com.scetia.Pro.baseapp.uitls.LoadState;

/**
 * created by ljs
 * on 2020/11/26
 */
public abstract class BaseViewModel extends AndroidViewModel implements IBaseLifecycle {
    //单一状态的加载
    protected MutableLiveData<LoadState> loadState;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<LoadState> getLoadState() {
        if(loadState==null){
            loadState = new MutableLiveData<>();
        }
        return loadState;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
