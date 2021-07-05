package com.scetia.app_sand.fragment;

import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.app_sand.R;
import com.scetia.app_sand.R2;
import com.scetia.app_sand.adapter.CCNoListAdapter;
import com.scetia.app_sand.base.BaseVMFragment;
import com.scetia.app_sand.bean.TestingCommissionBean;
import com.scetia.app_sand.viewmodel.SandTestingCommissionDetailViewModel;
import com.scetia.app_sand.viewmodel.TestingCommissionModelFactory;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2021/2/4
 */
public class UniqueIdentificationNoFragment extends BaseVMFragment<SandTestingCommissionDetailViewModel> {
    @BindView(R2.id.testing_info_query_search)
    SearchView testingInfoQuerySearch;
    @BindView(R2.id.rv_unique_identification_no)
    RecyclerView rvUniqueIdentificationNo;
    @BindView(R2.id.testing_info_query_toolbar)
    Toolbar testingInfoQueryToolbar;
    private String queryKey = "";
    private CCNoListAdapter listAdapter;

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        return new TestingCommissionModelFactory(requireActivity().getApplication(), this);
    }

    @Override
    protected Class<SandTestingCommissionDetailViewModel> getViewModelClass() {
        return SandTestingCommissionDetailViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_unique_identification_no;
    }

    @Override
    protected void observerData() {
        viewModel.getLoadState().observe(this, loadState -> {
        });
        viewModel.getCcNoLiveData().observe(this, ccNoBeans -> {
            listAdapter.setNewData(ccNoBeans);
        });
    }

    @Override
    protected View getImmersionBarNavigation() {
        return testingInfoQueryToolbar;
    }

    @Override
    protected void initView() {
        navigationBar.setVisibility(View.GONE);
        testingInfoQuerySearch.setIconifiedByDefault(false);
        testingInfoQuerySearch.setQueryHint("输入唯一性标识号");
        setUnderLinearTransparent(testingInfoQuerySearch);
        //点击查询按钮
        testingInfoQuerySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.getCCNoSource(newText);
                return false;
            }
        });
        listAdapter = new CCNoListAdapter();
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            TestingCommissionBean.CCNoBean ccNo = (TestingCommissionBean.CCNoBean) adapter.getItem(position);
            EventBusUtil.sendEvent(new GlobalEvent<>(EventBusUtil.C, ccNo.getCcNoStr()));
            Navigation.findNavController(view).navigateUp();
        });
        rvUniqueIdentificationNo.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvUniqueIdentificationNo.setAdapter(listAdapter);
    }

    @Override
    protected void initData() {
        viewModel.getCCNoSource(queryKey);
    }

    @OnClick(R2.id.tv_top_bar_search)
    public void onViewClicked() {
        viewModel.getCCNoSource(queryKey);
    }

    @OnClick(R2.id.test_info_query_back)
    public void queryBackClick(View view) {
        leftNavBarClick(view);
    }
}
