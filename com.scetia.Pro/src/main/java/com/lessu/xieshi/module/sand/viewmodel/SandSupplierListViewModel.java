package com.lessu.xieshi.module.sand.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.module.sand.repository.SandSupplierRepository;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.bean.BuildSandResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.http.service.BuildSandApiService;
import com.lessu.xieshi.module.sand.bean.SandSupplierBean;
import com.scetia.Pro.network.manage.BuildSandRetrofit;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/21
 */
public class SandSupplierListViewModel extends BaseViewModel {
    private MutableLiveData<List<SandSupplierBean>> sandSupplierLiveData = new MutableLiveData<>();
    private SandSupplierRepository repository = new SandSupplierRepository();
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
        loadState.postValue(LoadState.LOADING);
        repository.getSupplierList(new ResponseObserver<List<SandSupplierBean>>() {
            @Override
            public void success(List<SandSupplierBean> sandSupplierBeans) {
                if(sandSupplierBeans.size()>0){
                    loadState.postValue(LoadState.SUCCESS);
                }else{
                    loadState.postValue(LoadState.EMPTY.setMessage("暂无数据！"));
                }
                sandSupplierLiveData.postValue(sandSupplierBeans);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }
}
