package com.scetia.app_sand.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.navigation.NavigationBar;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.app_sand.R;
import com.scetia.app_sand.R2;
import com.scetia.app_sand.adapter.TestingInfoQueryListAdapter;
import com.scetia.app_sand.base.BaseVMFragment;
import com.scetia.app_sand.bean.TestingQueryResultBean;
import com.scetia.app_sand.viewmodel.TestingInfoQueryListViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/10/30
 */
public class TestingInfoQueryListFragment extends BaseVMFragment<TestingInfoQueryListViewModel> {
    public static final String TESTING_QUERY_INFO_ID = "testing_query_info_id";
    @BindView(R2.id.testing_info_query_search)
    SearchView testingInfoQuerySearch;
    @BindView(R2.id.testing_info_query_toolbar)
    Toolbar testingInfoQueryToolbar;
    @BindView(R2.id.testing_info_query_list_rv)
    RecyclerView testingInfoQueryListRv;
    @BindView(R2.id.testing_info_query_list_refresh)
    SmartRefreshLayout testingInfoQueryListRefresh;
    private String queryKey;
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
        viewModel.getLoadState().observe(this, loadState -> {
            switchUIPageState(loadState, testingInfoQueryListRefresh);
        });

        viewModel.getResultQueryLiveData().observe(this, resultQueryBeans -> {
            if (viewModel.getLoadState().getValue() == LoadState.LOAD_INIT_SUCCESS
                    || viewModel.getLoadState().getValue() == LoadState.EMPTY) {
                listAdapter.setNewData(resultQueryBeans);
            } else {
                listAdapter.addData(resultQueryBeans);
            }
        });
    }

    @Override
    protected void initView() {
        testingInfoQuerySearch.setIconifiedByDefault(false);
        setUnderLinearTransparent(testingInfoQuerySearch);
        initRecyclerView();

        testingInfoQueryListRefresh.setEnableLoadMoreWhenContentNotFull(false);
        testingInfoQueryListRefresh.setOnLoadMoreListener((layout) -> {
            viewModel.loadMoreData();
        });
        testingInfoQueryListRefresh.setOnRefreshListener(refreshLayout -> {
            viewModel.refreshLoad();
        });
        //点击查询按钮
        testingInfoQuerySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setQueryKey(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryKey = newText;
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        viewModel.loadInitData();
    }

    @Override
    protected NavigationBar createTopBarView() {
        return null;
    }

    @Override
    protected View getImmersionBarNavigation() {
        return testingInfoQueryToolbar;
    }

    /**
     * 初始化列表刷新和显示
     */
    private void initRecyclerView() {
        listAdapter = new TestingInfoQueryListAdapter();
        testingInfoQueryListRv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        testingInfoQueryListRv.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            TestingQueryResultBean bean= (TestingQueryResultBean) adapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putString(TESTING_QUERY_INFO_ID,bean.getFlowConsignInfoID());
            Navigation.findNavController(view).navigate(R.id.actionTestingQueryListToDetail,bundle);
        });
    }


    @OnClick(R2.id.test_info_query_back)
    public void onViewClicked(View view) {
        Navigation.findNavController(view).navigateUp();
    }


    @OnClick(R2.id.tv_top_bar_search)
    public void onViewClicked() {
        viewModel.setQueryKey(queryKey);
    }
}
