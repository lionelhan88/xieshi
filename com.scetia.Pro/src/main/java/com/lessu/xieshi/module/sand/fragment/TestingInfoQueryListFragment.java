package com.lessu.xieshi.module.sand.fragment;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.data.LoadState;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.sand.adapter.TestingInfoQueryListAdapter;
import com.lessu.xieshi.module.sand.viewmodel.TestingInfoQueryListViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/10/30
 */
public class TestingInfoQueryListFragment extends BaseVMFragment<TestingInfoQueryListViewModel> {
    @BindView(R.id.test_info_query_back)
    ImageView testInfoQueryBack;
    @BindView(R.id.testing_info_query_search)
    SearchView testingInfoQuerySearch;
    @BindView(R.id.testing_info_query_toolbar)
    Toolbar testingInfoQueryToolbar;
    @BindView(R.id.testing_info_query_list_rv)
    RecyclerView testingInfoQueryListRv;
    @BindView(R.id.testing_info_query_list_refresh)
    SmartRefreshLayout testingInfoQueryListRefresh;

    private TestingInfoQueryListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_testing_info_query_list;
    }

    @Override
    protected Class<TestingInfoQueryListViewModel> getViewModelClass() {
        return TestingInfoQueryListViewModel.class;
    }

    @Override
    protected void observerData() {
        viewModel.getLoadState().observe(this,loadState -> {
            switchUIPageState(loadState,testingInfoQueryListRefresh);
        });
        viewModel.getResultQueryLiveData().observe(this,resultQueryBeans -> {
            if(viewModel.getLoadState().getValue()==LoadState.LOAD_INIT){
                listAdapter.setNewData(resultQueryBeans);
            }
        });
    }

    @Override
    protected void initView() {
        navigationBar.setVisibility(View.GONE);
        testingInfoQuerySearch.setIconifiedByDefault(false);
        setUnderLinearTransparent(testingInfoQuerySearch);
        initRecyclerView();
        testingInfoQueryListRefresh.setOnLoadMoreListener((layout)->{
            viewModel.loadMoreData();
        });
        testingInfoQueryListRefresh.setOnRefreshListener(refreshLayout -> {
            viewModel.refreshLoad();
        });
    }

    @Override
    protected void initData() {
        viewModel.loadInitData();
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this).titleBar(testingInfoQueryToolbar)
                .navigationBarColor(com.lessu.uikit.R.color.light_gray)
                .navigationBarDarkIcon(true)
                .init();
    }

    /**
     * 初始化列表刷新和显示
     */
    private void initRecyclerView() {
        listAdapter = new TestingInfoQueryListAdapter();
        testingInfoQueryListRv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        testingInfoQueryListRv.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Navigation.findNavController(view).navigate(R.id.actionTestingQueryListToDetail);
            }
        });
    }


    @OnClick(R.id.test_info_query_back)
    public void onViewClicked() {
        requireActivity().onBackPressed();
    }


}
