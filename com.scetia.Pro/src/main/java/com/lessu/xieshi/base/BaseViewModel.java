package com.lessu.xieshi.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.bean.LoadState;
import com.lessu.xieshi.http.ExceptionHandle;
import com.lessu.xieshi.lifcycle.IBaseLifecycle;

/**
 * created by ljs
 * on 2020/11/26
 */
public abstract class BaseViewModel extends AndroidViewModel implements IBaseLifecycle {
    protected MutableLiveData<LoadState> loadState = new MutableLiveData<>();
    protected MutableLiveData<ExceptionHandle.ResponseThrowable> throwableLiveData = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<LoadState> getLoadState() {
        return loadState;
    }

    public MutableLiveData<ExceptionHandle.ResponseThrowable> getThrowable() {
        return throwableLiveData;
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
