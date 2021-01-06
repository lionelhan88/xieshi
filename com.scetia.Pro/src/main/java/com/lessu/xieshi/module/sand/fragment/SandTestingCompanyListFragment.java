package com.lessu.xieshi.module.sand.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lessu.EventBusUtil;
import com.lessu.GlobalEvent;
import com.lessu.data.LoadState;
import com.lessu.navigation.BarButtonItem;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.sand.adapter.SandTestingCompanyListAdapter;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;
import com.lessu.xieshi.module.sand.viewmodel.SandTestingCompanyListViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/9
 */
public class SandTestingCompanyListFragment extends BaseVMFragment<SandTestingCompanyListViewModel> {
    @BindView(R.id.sand_sales_target_list_rv)
    RecyclerView sandSalesTargetListRv;
    @BindView(R.id.sand_sales_target_list_refresh)
    SmartRefreshLayout sandSalesTargetListRefresh;
    @BindView(R.id.sand_sales_target_list_fab)
    FloatingActionButton sandSalesTargetListFab;
    private SandTestingCompanyListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_smart_refresh_list_common_layout;
    }

    @Override
    protected Class<SandTestingCompanyListViewModel> getViewModelClass() {
        return SandTestingCompanyListViewModel.class;
    }


    @Override
    protected void observerData() {
        viewModel.getLoadState().observe(this, loadState -> {
            if (loadState == LoadState.LOADING) {
                LSAlert.showProgressHud(requireActivity(), "正在删除...");
            } else {
                LSAlert.dismissProgressHud();
            }
        });

        viewModel.getLoadDataState().observe(this, loadState -> {
            switchUIPageState(loadState,sandSalesTargetListRefresh);
        });

        viewModel.getThrowable().observe(this, responseThrowable -> {
            ToastUtil.showShort(responseThrowable.message);
        });


        viewModel.getItemPosition().observe(this, integer -> {
            listAdapter.remove(integer);
        });

        viewModel.getAddedSanSalesTargetLiveData().observe(this, addedTestingCompanyBeans -> {
            if (viewModel.getLoadDataState().getValue() == LoadState.LOAD_INIT_SUCCESS) {
                listAdapter.setNewData(addedTestingCompanyBeans);
            } else {
                listAdapter.addData(addedTestingCompanyBeans);
            }
        });
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.sand_manage_info_testing_manage_text));
        listAdapter = viewModel.getListAdapter();
        Bundle arguments = getArguments();
        listAdapter.setCanSwipe(arguments == null);
        sandSalesTargetListFab.setVisibility(arguments==null?View.VISIBLE:View.GONE);
        if (arguments == null) {
            listAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                if (view.getId() == R.id.right) {
                    AddedTestingCompanyBean bean = (AddedTestingCompanyBean) adapter.getItem(position);
                    viewModel.delAddedTestingCompany(bean, position);
                }
            });
        } else {
            //检测委托详情跳转过来的 ，进行选择
            listAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                if (view.getId() == R.id.company_manage_item_content) {
                    listAdapter.addSelect(position);
                }
            });
            BarButtonItem rightBtn = new BarButtonItem(requireActivity(), R.drawable.ic_tick);
            navigationBar.setRightBarItem(rightBtn);
            rightBtn.setOnClickListener(v -> {
                if (listAdapter.getSelectBean() == null) {
                    ToastUtil.showShort("未有选中的项！");
                    return;
                }
                EventBusUtil.sendStickyEvent(new GlobalEvent<>(EventBusUtil.B, listAdapter.getSelectBean()));
                Navigation.findNavController(v).navigateUp();
            });
        }
        sandSalesTargetListRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sandSalesTargetListRv.setAdapter(listAdapter);

        sandSalesTargetListRefresh.setEnableLoadMoreWhenContentNotFull(false);
        sandSalesTargetListRefresh.setOnRefreshListener(refreshLayout -> {
            viewModel.refresh();
        });
        sandSalesTargetListRefresh.setOnLoadMoreListener(refreshLayout -> {
            viewModel.loadData(false);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.setPageIndex(0);
        viewModel.loadData(true);
    }

    @OnClick({R.id.sand_sales_target_list_fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sand_sales_target_list_fab:
                EventBusUtil.sendStickyEvent(new GlobalEvent<>(EventBusUtil.B, listAdapter.getData()));
                Navigation.findNavController(view).navigate(R.id.actionTestingMangeToTestingQueryResult);
                break;
        }
    }
}
