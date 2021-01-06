package com.lessu.xieshi.module.sand.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

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
import com.lessu.xieshi.module.sand.adapter.SMFlowDeclarationListAdapter;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;
import com.lessu.xieshi.module.sand.viewmodel.SMFlowDeclarationListViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/10/28
 */
public class SMFlowDeclarationListFragment extends BaseVMFragment<SMFlowDeclarationListViewModel> {
    @BindView(R.id.sand_manage_flow_declaration_rv)
    RecyclerView sandManageFlowDeclarationRv;
    @BindView(R.id.sand_manage_flow_declaration_refresh)
    SmartRefreshLayout sandManageFlowDeclarationRefresh;
    @BindView(R.id.sand_manage_flow_declaration_fab)
    FloatingActionButton sandManageFlowDeclarationFab;
    @BindView(R.id.sm_flow_declaration_declare_rg)
    RadioGroup smFlowDeclarationDeclareRg;
    private SMFlowDeclarationListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sm_flow_declaration_list;
    }

    @Override
    protected Class<SMFlowDeclarationListViewModel> getViewModelClass() {
        return SMFlowDeclarationListViewModel.class;
    }

    @Override
    protected void observerData() {
        viewModel.getLoadDataState().observe(this, loadState -> {
          switchUIPageState(loadState,sandManageFlowDeclarationRefresh);
        });

        viewModel.getLoadState().observe(this, loadState -> {
            if (loadState == LoadState.LOADING) {
                LSAlert.showProgressHud(requireActivity(), "正在删除...");
            } else {
                LSAlert.dismissProgressHud();
            }
        });

        viewModel.getThrowable().observe(this, responseThrowable -> {
            ToastUtil.showShort(responseThrowable.message);
        });
    }


    @Override
    protected void initView() {
        setTitle("流向申报记录");
        initRecyclerView();
        Bundle arguments = getArguments();
        sandManageFlowDeclarationRefresh.setEnableLoadMoreWhenContentNotFull(false);
        sandManageFlowDeclarationRefresh.setOnRefreshListener(refreshLayout -> {
            viewModel.refresh();
        });
        sandManageFlowDeclarationRefresh.setOnLoadMoreListener(refreshLayout -> {
            viewModel.loadData(false);
        });
        sandManageFlowDeclarationFab.setVisibility(arguments == null ? View.VISIBLE : View.GONE);
        smFlowDeclarationDeclareRg.setVisibility(arguments == null ? View.VISIBLE : View.GONE);
        //如果从检测委托跳转过来，不允许删除和查看
        if (arguments == null) {
            listAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                FlowDeclarationBean bean = (FlowDeclarationBean) adapter.getItem(position);
                if (view.getId() == R.id.flow_declaration_item_content) {
                    EventBusUtil.sendStickyEvent(new GlobalEvent<>(EventBusUtil.A, bean));
                    Navigation.findNavController(view).navigate(R.id.actionSMFlowDeclarationListToDetail);
                } else {
                    //删除当前项
                    LSAlert.showAlert(requireActivity(), "", "确定要删除吗？", "删除", () -> {
                        viewModel.delFlowDeclaration(bean.getId(), position);
                    });
                }
            });
        } else {
            listAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                if (view.getId() == R.id.flow_declaration_item_content) {
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
                EventBusUtil.sendEvent(new GlobalEvent<>(EventBusUtil.A, listAdapter.getSelectBean()));
                Navigation.findNavController(v).navigateUp();
            });
        }


        viewModel.getFlowDeclarationLiveData().observe(this, flowDeclarationBeans -> {
            if (viewModel.getLoadDataState().getValue() == LoadState.LOAD_INIT_SUCCESS) {
                listAdapter.setNewData(flowDeclarationBeans);
            } else {
                listAdapter.addData(flowDeclarationBeans);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.setPageIndex(0);
        viewModel.loadData(true);
    }

    private void initRecyclerView() {
        listAdapter = viewModel.getListAdapter();
        listAdapter.setCanSlide(getArguments() == null);
        sandManageFlowDeclarationRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sandManageFlowDeclarationRv.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener((adapter, view, position) ->
                Navigation.findNavController(view).navigate(R.id.actionSMFlowDeclarationListToDetail));
    }

    @OnClick(R.id.sand_manage_flow_declaration_fab)
    public void onViewClicked(View view) {
        Navigation.findNavController(view).navigate(R.id.actionSMFlowDeclarationListToDetail);
    }
}
