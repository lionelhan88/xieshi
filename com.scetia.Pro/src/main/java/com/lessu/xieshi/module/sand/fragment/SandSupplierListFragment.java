package com.lessu.xieshi.module.sand.fragment;


import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.navigation.BarButtonItem;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.sand.adapter.SandProviderListAdapter;
import com.lessu.xieshi.module.sand.bean.SandSupplierBean;
import com.lessu.xieshi.module.sand.viewmodel.SandSupplierListViewModel;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.baseapp.uitls.LoadState;

import butterknife.BindView;

/**
 * created by ljs
 * on 2020/11/5
 */
public class SandSupplierListFragment extends BaseVMFragment<SandSupplierListViewModel> {
    @BindView(R.id.sand_manage_provider_rv)
    RecyclerView sandManageProviderRv;
    @BindView(R.id.content_loading_progress)
    ContentLoadingProgressBar contentLoadingProgress;
    @BindView(R.id.content_loading_text)
    TextView contentLoadingText;
    @BindView(R.id.content_loading_layout)
    RelativeLayout contentLoadingLayout;
    private SandProviderListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sand_info_maintain;
    }

    @Override
    protected Class<SandSupplierListViewModel> getViewModelClass() {
        return SandSupplierListViewModel.class;
    }

    @Override
    protected void observerData() {
        viewModel.getLoadState().observe(this, loadState -> {
            switch (loadState) {
                case LOADING:
                    contentLoadingLayout.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    contentLoadingLayout.setVisibility(View.GONE);
                    break;
                case EMPTY:
                    //没有数据了
                    contentLoadingText.setText(loadState.getMessage());
                    break;
                case FAILURE:
                    contentLoadingProgress.setVisibility(View.GONE);
                    contentLoadingText.setText(loadState.getMessage());
                    break;
            }
        });
        viewModel.getSandSupplierLiveData().observe(this, sandSupplierBeans -> {
            listAdapter.setNewData(sandSupplierBeans);
        });
    }

    @Override
    protected void initView() {
        setTitle("供应商列表");
        listAdapter = new SandProviderListAdapter();
        sandManageProviderRv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        sandManageProviderRv.setAdapter(listAdapter);
        if (getArguments() != null && getArguments().getInt(SMFlowDeclarationDetailFragment.NAVIGATE_KEY) == 1) {
            listAdapter.setOnItemClickListener((adapter, view, position) -> {
                SandSupplierBean bean = (SandSupplierBean) adapter.getItem(position);
                listAdapter.addSelectedBean(bean, position);
            });
            BarButtonItem rightBtn = new BarButtonItem(requireActivity(), R.drawable.ic_tick);
            navigationBar.setRightBarItem(rightBtn);
            rightBtn.setOnClickListener(v -> {
                if (listAdapter.getSelectedBean() == null) {
                    ToastUtil.showShort("未有选中的项！");
                    return;
                }
                EventBusUtil.sendStickyEvent(new GlobalEvent(EventBusUtil.C, listAdapter.getSelectedBean()));
                Navigation.findNavController(v).navigateUp();
            });
        }
        viewModel.loadSuppliers();
    }

}
