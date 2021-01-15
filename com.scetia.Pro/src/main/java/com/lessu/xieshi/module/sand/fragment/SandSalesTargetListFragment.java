package com.lessu.xieshi.module.sand.fragment;

import android.view.View;

import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.navigation.BarButtonItem;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.sand.adapter.SandSalesManageListAdapter;
import com.lessu.xieshi.module.sand.bean.AddedSandSalesTargetBean;
import com.lessu.xieshi.module.sand.viewmodel.SandSalesTargetListViewModel;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/5
 */
public class SandSalesTargetListFragment extends BaseVMFragment<SandSalesTargetListViewModel> {
    @BindView(R.id.sand_sales_target_list_rv)
    RecyclerView sandSalesTargetListRv;
    @BindView(R.id.sand_sales_target_list_refresh)
    SmartRefreshLayout sandSalesTargetListRefresh;
    @BindView(R.id.sand_sales_target_list_fab)
    FloatingActionButton sandSalesTargetListFab;
    private SandSalesManageListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_smart_refresh_list_common_layout;
    }

    @Override
    protected Class<SandSalesTargetListViewModel> getViewModelClass() {
        return SandSalesTargetListViewModel.class;
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

        viewModel.getAddedSanSalesTargetLiveData().observe(this, new Observer<List<AddedSandSalesTargetBean>>() {
            @Override
            public void onChanged(List<AddedSandSalesTargetBean> addedSandSalesTargetBeans) {
                if (viewModel.getLoadDataState().getValue() == LoadState.LOAD_INIT_SUCCESS||
                        viewModel.getLoadDataState().getValue()==LoadState.EMPTY) {
                    listAdapter.setNewData(addedSandSalesTargetBeans);
                } else {
                    listAdapter.addData(addedSandSalesTargetBeans);
                }
            }
        });
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.sand_manage_info_sales_manage_text));
        sandSalesTargetListRefresh.setEnableLoadMoreWhenContentNotFull(false);
        sandSalesTargetListRv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        listAdapter = new SandSalesManageListAdapter(!(getArguments()!=null&&
                getArguments().getInt(SMFlowDeclarationDetailFragment.NAVIGATE_KEY)==1));

        sandSalesTargetListRv.setAdapter(listAdapter);

        sandSalesTargetListRefresh.setOnRefreshListener(refreshLayout -> {
            viewModel.refresh();
        });
        sandSalesTargetListRefresh.setOnLoadMoreListener(refreshLayout -> {
            viewModel.loadData(false);
        });

        listAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            AddedSandSalesTargetBean bean = (AddedSandSalesTargetBean) adapter.getItem(position);
            if (view.getId() == R.id.right) {
                viewModel.delAddedSanSalesTarget(bean, position);
            }else if(view.getId()==R.id.content){
                if(getArguments()!=null&&getArguments().getInt(SMFlowDeclarationDetailFragment.NAVIGATE_KEY)==1) {
                    listAdapter.addSelectedBean(position);
                }
            }
        });

        if(getArguments()!=null&&getArguments().getInt(SMFlowDeclarationDetailFragment.NAVIGATE_KEY)==1) {
            BarButtonItem rightBtn = new BarButtonItem(requireActivity(),R.drawable.ic_tick);
            navigationBar.setRightBarItem(rightBtn);
            rightBtn.setOnClickListener(v->{
                if(listAdapter.getSelectedBean()==null){
                    ToastUtil.showShort("未有选中的项！");
                    return;
                }
                EventBusUtil.sendStickyEvent(new GlobalEvent<>(EventBusUtil.B,listAdapter.getSelectedBean()));
                Navigation.findNavController(v).navigateUp();
            });
        }
    }

    @Override
    protected void initData() {
        viewModel.setPageIndex(0);
        viewModel.loadData(true);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addSalesSuccess(GlobalEvent<Boolean> event){
        if(event.getCode()==EventBusUtil.A){
            viewModel.setPageIndex(0);
            viewModel.loadData(false);
        }
    }


    @OnClick({R.id.sand_sales_target_list_fab})
    public void onViewClicked(View view) {
        EventBusUtil.sendStickyEvent(new GlobalEvent<>(EventBusUtil.C, listAdapter.getData()));
        Navigation.findNavController(view).navigate(R.id.actionSalesFragmentToCompanyResult);
    }
}
