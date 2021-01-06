package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.data.LoadState;
import com.lessu.xieshi.http.BuildSandResultData;
import com.lessu.xieshi.http.BuildSandRetrofit;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.api.BuildSandApiService;
import com.lessu.xieshi.module.sand.bean.SandSupplierBean;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/21
 */
public class SandSupplierListViewModel extends BaseViewModel {
    private MutableLiveData<List<SandSupplierBean>> sandSupplierLiveData = new MutableLiveData<>();
    public SandSupplierListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<SandSupplierBean>> getSandSupplierLiveData() {
        return sandSupplierLiveData;
    }

    /**
     * 获取供应商列表数据
     */
    public void loadSuppliers(){
        loadState.postValue(LoadState.LOAD_INIT);
        BuildSandRetrofit.getInstance().getService(BuildSandApiService.class)
                .getSuppliers()
                .compose(BuildSandRetrofit.<BuildSandResultData<List<SandSupplierBean>>, List<SandSupplierBean>>applyTransformer())
                .subscribe(new ResponseObserver<List<SandSupplierBean>>() {
                    @Override
                    public void success(List<SandSupplierBean> sandSupplierBeans) {
                        loadState.postValue(LoadState.LOAD_INIT_SUCCESS);
                        sandSupplierLiveData.postValue(sandSupplierBeans);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        loadState.postValue(LoadState.FAILURE);
                        throwableLiveData.postValue(throwable);
                    }
                });
    }
}
