package com.scetia.app_sand.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.uikit.views.LSAlert;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.app_sand.R;
import com.scetia.app_sand.R2;
import com.scetia.app_sand.adapter.TestingCommissionListAdapter;
import com.scetia.app_sand.base.BaseVMFragment;
import com.scetia.app_sand.bean.TestingCommissionBean;
import com.scetia.app_sand.viewmodel.TestingCommissionListViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/10
 */
public class TestingCommissionListFragment extends BaseVMFragment<TestingCommissionListViewModel> {
    @BindView(R2.id.sand_sales_target_list_rv)
    RecyclerView sandSalesTargetListRv;
    @BindView(R2.id.sand_sales_target_list_refresh)
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
        viewModel.getLoadState().observe(this,loadState -> {
            switchUIPageState(loadState,sandSalesTargetListRefresh);
        });

        //数据列表加载成功
        viewModel.getTestingCommissionLiveData().observe(this,testingCommissionBeans -> {
            if(viewModel.getLoadState().getValue()== LoadState.LOAD_INIT_SUCCESS
            ||viewModel.getLoadState().getValue()== LoadState.EMPTY){
                //初始化数据成功，需要重新刷新数据
                listAdapter.setNewData(testingCommissionBeans);
            }else{
                //加载更多数据成功，直接在后面追加数据
                listAdapter.addData(testingCommissionBeans);
            }
        });
        //观察删除了哪一项，列表中也要删除
        viewModel.getDelPosition().observe(this,position-> listAdapter.remove(position));
    }

    @Override
    protected void initView() {
        setTitle(getString(R.string.testing_commission_list_title_text));
        listAdapter = viewModel.getListAdapter();
        sandSalesTargetListRv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        sandSalesTargetListRv.setAdapter(listAdapter);
        listAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId()==R.id.right){
                TestingCommissionBean bean = (TestingCommissionBean) adapter.getItem(position);
                if(bean.getSampleStatus().equals("已送样")){
                    LSAlert.showAlert(requireContext(),"当前委托已送样，不能删除！");
                    return;
                }
                LSAlert.showAlert(requireActivity(), "", "确定要删除吗？", "删除", () -> {
                    viewModel.delCommission(bean.getId(),position);
                });
            }else if(view.getId()==R.id.commission_list_item_content){
                TestingCommissionBean bean = (TestingCommissionBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("id",bean.getId());
                bundle.putString("flowInfoId",bean.getFlowInfoId());
                Navigation.findNavController(view).navigate(R.id.actionCommissionListToDetail,bundle);
            }
        });
        sandSalesTargetListRefresh.setEnableLoadMoreWhenContentNotFull(false);
        sandSalesTargetListRefresh.setOnRefreshListener(refreshLayout ->viewModel.refreshOnLoad());
        sandSalesTargetListRefresh.setOnLoadMoreListener(refreshLayout -> viewModel.loadData(false));
    }

    @Override
    protected void initData() {
        viewModel.loadData(true);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addOrUpdateSuccess(GlobalEvent<Boolean> event){
        if(event.getCode()==EventBusUtil.D){
            //添加委托或者更新委托成功后，需要刷新列表页面
            viewModel.refreshOnLoad();
        }
    }

    @OnClick(R2.id.sand_sales_target_list_fab)
    public void onViewClicked(View v) {
        Navigation.findNavController(v).navigate(R.id.actionCommissionListToDetail);
    }
}
