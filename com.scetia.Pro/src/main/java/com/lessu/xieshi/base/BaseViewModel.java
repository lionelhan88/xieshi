package com.lessu.xieshi.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.bean.LoadMoreState;
import com.lessu.data.LoadState;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.lifcycle.IBaseLifecycle;

/**
 * created by ljs
 * on 2020/11/26
 */
public abstract class BaseViewModel extends AndroidViewModel implements IBaseLifecycle {
    //单一状态的加载
    protected MutableLiveData<LoadState> loadState;
    //多种状态的加载
    protected MutableLiveData<LoadMoreState> loadMoreState = new MutableLiveData<>();
    protected MutableLiveData<ExceptionHandle.ResponseThrowable> throwableLiveData = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<LoadState> getLoadState() {
        if(loadState==null){
            loadState = new MutableLiveData<>();
        }
        return loadState;
    }

    public MutableLiveData<LoadMoreState> getLoadMoreState() {
        return loadMoreState;
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
