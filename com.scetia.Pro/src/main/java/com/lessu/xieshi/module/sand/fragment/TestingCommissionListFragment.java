package com.lessu.xieshi.module.sand.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.sand.adapter.TestingCommissionListAdapter;
import com.lessu.xieshi.module.sand.viewmodel.TestingCommissionListViewModel;
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
    public static final String TESTING_COMMISSION_INFO_ID = "info_id";
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

        viewModel.getLoadState().observe(this,loadState -> {
            if(loadState==LoadState.LOADING){
                LSAlert.showProgressHud(requireContext(),"正在删除...");
            }else{
                LSAlert.dismissProgressHud();
            }
        });

        viewModel.getThrowable().observe(this,responseThrowable -> {
            ToastUtil.showShort(responseThrowable.message);
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
                LSAlert.showAlert(requireActivity(), "", "确定要删除吗？", "删除", () -> {
                    viewModel.delCommission(bean.getId(),position);
                });
            }else if(view.getId()==R.id.commission_list_item_content){
                TestingCommissionBean bean = (TestingCommissionBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("id",bean.getId());
                bundle.putString("flowInfoId",bean.getFlowInfoId());
             //   EventBusUtil.sendStickyEvent(new GlobalEvent<>(EventBusUtil.C,bean));
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

    @OnClick(R.id.sand_sales_target_list_fab)
    public void onViewClicked(View v) {
        Navigation.findNavController(v).navigate(R.id.actionCommissionListToDetail);
    }
}
