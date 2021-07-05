package com.scetia.app_sand.fragment;

import android.view.View;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lessu.navigation.BarButtonItem;
import com.lessu.uikit.views.LSAlert;

import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.app_sand.R;
import com.scetia.app_sand.R2;
import com.scetia.app_sand.adapter.SandSalesManageListAdapter;
import com.scetia.app_sand.base.BaseVMFragment;
import com.scetia.app_sand.bean.AddedSandSalesTargetBean;
import com.scetia.app_sand.util.ToastUtil;
import com.scetia.app_sand.viewmodel.SandSalesTargetListViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/5
 */
public class SandSalesTargetListFragment extends BaseVMFragment<SandSalesTargetListViewModel> {
    @BindView(R2.id.sand_sales_target_list_rv)
    RecyclerView sandSalesTargetListRv;
    @BindView(R2.id.sand_sales_target_list_refresh)
    SmartRefreshLayout sandSalesTargetListRefresh;
    @BindView(R2.id.sand_sales_target_list_fab)
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
        viewModel.getDeleteDataState().observe(this, loadState -> {
            if (loadState == LoadState.LOADING) {
                LSAlert.showProgressHud(requireActivity(), "正在删除...");
            } else {
                LSAlert.dismissProgressHud();
            }
            if(loadState==LoadState.FAILURE){
                ToastUtil.showShort(loadState.getMessage());
            }
        });

        viewModel.getLoadState().observe(this, loadState -> {
            switchUIPageState(loadState,sandSalesTargetListRefresh);
        });

        viewModel.getItemPosition().observe(this, integer -> {
            listAdapter.remove(integer);
        });

        viewModel.getAddedSanSalesTargetLiveData().observe(this, addedSandSalesTargetBeans -> {
            if (viewModel.getLoadState().getValue() == LoadState.LOAD_INIT_SUCCESS||
                    viewModel.getLoadState().getValue()==LoadState.EMPTY) {
                listAdapter.setNewData(addedSandSalesTargetBeans);
            } else {
                listAdapter.addData(addedSandSalesTargetBeans);
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
            viewModel.loadMoreData();
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
       viewModel.loadInitData();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addSalesSuccess(GlobalEvent<Boolean> event){
        if(event.getCode()==EventBusUtil.A){
            viewModel.refresh();
        }
    }


    @OnClick({R2.id.sand_sales_target_list_fab})
    public void onViewClicked(View view) {
        EventBusUtil.sendStickyEvent(new GlobalEvent<>(EventBusUtil.C, listAdapter.getData()));
        Navigation.findNavController(view).navigate(R.id.actionSalesFragmentToCompanyResult);
    }
}
