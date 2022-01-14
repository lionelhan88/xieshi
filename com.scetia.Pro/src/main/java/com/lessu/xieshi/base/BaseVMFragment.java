package com.lessu.xieshi.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lessu.xieshi.utils.ToastUtil;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.uikit.views.LSAlert;
import com.scetia.Pro.baseapp.fragment.BaseFragment;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * created by ljs
 * on 2021/1/4
 */
public abstract class BaseVMFragment<VM extends ViewModel> extends BaseFragment {
    protected VM viewModel;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViewModel();
        observerData();
        super.onViewCreated(view, savedInstanceState);
        if (isRegisterEventBus() && !EventBusUtil.isRegistered(this)) {
            EventBusUtil.register(this);
        }
    }

    private void initViewModel() {
        if (getViewModelFactory() == null) {
            viewModel = new ViewModelProvider(this).get(getViewModelClass());
        } else {
            viewModel = new ViewModelProvider(this, getViewModelFactory()).get(getViewModelClass());
        }
    }


    //是否注册EventBus
    protected boolean isRegisterEventBus() {
        return false;
    }

    protected  Class<VM> getViewModelClass(){
        Class<VM> modelClass = null;
        Type genericSuperclass = getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType){
            modelClass= (Class<VM>) ((ParameterizedType)genericSuperclass).getActualTypeArguments()[0];
        }
        return modelClass;
    }

    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    protected void observerData() {

    }

    protected void switchUIPageState(LoadState loadState, SmartRefreshLayout smartRefreshLayout) {
        switch (loadState) {
            case LOAD_INIT:
            case LOADING:
                LSAlert.showProgressHud(requireActivity(), loadState.getMessage());
                break;
            case LOAD_INIT_SUCCESS:
                LSAlert.dismissProgressHud();
                smartRefreshLayout.finishRefresh(true);
                break;
            case EMPTY:
                LSAlert.dismissProgressHud();
                smartRefreshLayout.finishRefresh(true);
                LSAlert.showAlert(requireActivity(), loadState.getMessage());
                break;
            case NO_MORE:
                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                smartRefreshLayout.setEnableFooterFollowWhenNoMoreData(true);
                break;
            case SUCCESS:
                LSAlert.dismissProgressHud();
                smartRefreshLayout.finishLoadMore(true);
                break;
            case FAILURE:
                LSAlert.dismissProgressHud();
                smartRefreshLayout.finishLoadMore(false);
                smartRefreshLayout.finishRefresh(false);
                ToastUtil.showShort(loadState.getMessage());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }
}
