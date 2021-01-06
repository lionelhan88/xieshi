package com.lessu.xieshi.module.sand.fragment;

import android.view.View;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.data.LoadState;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.sand.adapter.TestingCommissionListAdapter;
import com.lessu.xieshi.module.sand.viewmodel.TestingCommissionListViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/10
 */
public class TestingCommissionListFragment extends BaseVMFragment<TestingCommissionListViewModel> {
    @BindView(R.id.sand_sales_target_list_rv)
    RecyclerView sandSalesTargetListRv;
    @BindView(R.id.sand_sales_target_list_refresh)
    SmartRefreshLayout sandSalesTargetListRefresh;
    private TestingCommissionListAdapter listAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_smart_refresh_list_common_layout;
    }

    @Override
    protected Class<TestingCommissionListViewModel> getViewModelClass() {
        return TestingCommissionListViewModel.class;
    }

    @Override
    protected void observerData() {
        viewModel.getLoadDatState().observe(this,loadState -> {
            switchUIPageState(loadState,sandSalesTargetListRefresh);
        });
        //数据列表加载成功
        viewModel.getTestingCommissionLiveData().observe(this,testingCommissionBeans -> {
            if(viewModel.getLoadDatState().getValue()== LoadState.LOAD_INIT_SUCCESS
            ||viewModel.getLoadDatState().getValue()== LoadState.EMPTY){
                //初始化数据成功，需要重新刷新数据
                listAdapter.setNewData(testingCommissionBeans);
            }else{
                //加载更多数据成功，直接在后面追加数据
                listAdapter.addData(testingCommissionBeans);
            }
        });
    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.testing_commission_list_title_text));
        listAdapter = viewModel.getListAdapter();
        sandSalesTargetListRv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        sandSalesTargetListRv.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            Navigation.findNavController(view).navigate(R.id.actionCommissionListToDetail);
        });

        sandSalesTargetListRefresh.setOnRefreshListener(refreshLayout ->viewModel.refreshOnLoad());
        sandSalesTargetListRefresh.setOnLoadMoreListener(refreshLayout -> viewModel.loadData(false));
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.loadData(true);
    }

    @OnClick(R.id.sand_sales_target_list_fab)
    public void onViewClicked(View v) {
        Navigation.findNavController(v).navigate(R.id.actionCommissionListToDetail);
    }
}
