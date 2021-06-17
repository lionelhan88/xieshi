package com.lessu.xieshi.base;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.module.mis.activities.MisNoticesActivity;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 带有ViewModel的activity类
 * @param <VM>
 */
public abstract  class BaseVMActivity<VM extends BaseViewModel> extends NavigationActivity {

    protected VM mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mViewModel=createViewModel();
        mViewModel.getLoadState().observe(this,loadState -> {
            switch (loadState){
                case LOADING:
                   inLoading(loadState);
                    break;
                case SUCCESS:
                    inSuccess(loadState);
                    break;
                case FAILURE:
                    inFailure(loadState);
                    break;
            }
        });
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取ViewModel类的实例
     * @return mViewModel
     */
    protected VM createViewModel(){
       return new ViewModelProvider(this).get(getVmClass());
    }

    /**
     * 获取当前VM的class类
     * @return VM.class
     */
    private Class<VM> getVmClass(){
        Class<VM> modelClass = null;
        Type genericSuperclass = getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType){
           modelClass= (Class<VM>) ((ParameterizedType)genericSuperclass).getActualTypeArguments()[0];
        }
        return modelClass;
    }

    /**
     * 加载状态时
     * @param loadState
     */
    protected void inLoading(LoadState loadState){
        LSAlert.showProgressHud(this, loadState.getMessage());
    }

    /**
     * 加载成功时
     * @param loadState
     */
    protected void inSuccess(LoadState loadState){
        LSAlert.dismissProgressHud();
    }

    /**
     * 加载失败时
     * @param loadState
     */
    protected void inFailure(LoadState loadState){
        LSAlert.dismissProgressHud();
        LSAlert.showAlert(this, loadState.getMessage());
    }

}
