package com.lessu.xieshi.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lessu.BaseFragment;
import com.lessu.data.LoadState;
import com.lessu.uikit.views.LSAlert;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * created by ljs
 * on 2021/1/4
 */
public abstract class BaseVMFragment<VM extends ViewModel> extends BaseFragment {
    protected VM viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initViewModel();
        observerData();
    }

    private void initViewModel() {
        if (getViewModelFactory() == null) {
            viewModel = new ViewModelProvider(this).get(getViewModelClass());
        } else {
            viewModel = new ViewModelProvider(this, getViewModelFactory()).get(getViewModelClass());
        }
    }

    protected abstract Class<VM> getViewModelClass();

    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    protected void observerData() {

    }

    protected void switchUIPageState(LoadState loadState, SmartRefreshLayout smartRefreshLayout) {
        switch (loadState) {
            case LOAD_INIT:
                LSAlert.showProgressHud(requireActivity(), "正在加载...");
                break;
            case EMPTY:
                LSAlert.dismissProgressHud();
                smartRefreshLayout.finishRefresh(true);
                LSAlert.showAlert(requireActivity(), "暂无相关数据！");
                break;
            case LOAD_INIT_SUCCESS:
                LSAlert.dismissProgressHud();
                smartRefreshLayout.finishRefresh(true);
                break;
            case NO_MORE:
                smartRefreshLayout.finishLoadMoreWithNoMoreData();

                break;
            case SUCCESS:
                smartRefreshLayout.finishLoadMore(true);
                break;
            case FAILURE:
                LSAlert.dismissProgressHud();
                smartRefreshLayout.finishLoadMore(false);
                smartRefreshLayout.finishRefresh(false);
                break;
        }
    }
}
