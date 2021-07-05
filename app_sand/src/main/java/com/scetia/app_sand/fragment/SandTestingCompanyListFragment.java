package com.scetia.app_sand.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lessu.navigation.BarButtonItem;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.app_sand.R;
import com.scetia.app_sand.R2;
import com.scetia.app_sand.adapter.SandTestingCompanyListAdapter;
import com.scetia.app_sand.base.BaseVMFragment;
import com.scetia.app_sand.bean.AddedTestingCompanyBean;
import com.scetia.app_sand.util.ToastUtil;
import com.scetia.app_sand.viewmodel.SandTestingCompanyListViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/9
 */
public class SandTestingCompanyListFragment extends BaseVMFragment<SandTestingCompanyListViewModel> {
    @BindView(R2.id.sand_sales_target_list_rv)
    RecyclerView sandSalesTargetListRv;
    @BindView(R2.id.sand_sales_target_list_refresh)
    SmartRefreshLayout sandSalesTargetListRefresh;
    @BindView(R2.id.sand_sales_target_list_fab)
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
            switchUIPageState(loadState, sandSalesTargetListRefresh);
        });

        viewModel.getItemPosition().observe(this, integer -> {
            listAdapter.remove(integer);
        });

        viewModel.getAddedSanSalesTargetLiveData().observe(this, addedTestingCompanyBeans -> {
            if (viewModel.getLoadState().getValue() == LoadState.LOAD_INIT_SUCCESS
                    || viewModel.getLoadState().getValue() == LoadState.EMPTY) {
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
        sandSalesTargetListFab.setVisibility(arguments == null ? View.VISIBLE : View.GONE);
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
        sandSalesTargetListRv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        sandSalesTargetListRv.setAdapter(listAdapter);

        sandSalesTargetListRefresh.setEnableLoadMoreWhenContentNotFull(false);
        sandSalesTargetListRefresh.setOnRefreshListener(refreshLayout -> {
            viewModel.refresh();
        });
        sandSalesTargetListRefresh.setOnLoadMoreListener(refreshLayout -> {
            viewModel.loadMoreData();
        });
    }

    @Override
    protected void initData() {
        viewModel.loadDataInit();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addTestingCompanySuccess(GlobalEvent<Boolean> event) {
        if (event.getCode() == EventBusUtil.A) {
            viewModel.refresh();
        }
    }

    @OnClick({R2.id.sand_sales_target_list_fab})
    public void onViewClicked(View view) {
        EventBusUtil.sendStickyEvent(new GlobalEvent<>(EventBusUtil.B, listAdapter.getData()));
        Navigation.findNavController(view).navigate(R.id.actionTestingMangeToTestingQueryResult);
    }
}
