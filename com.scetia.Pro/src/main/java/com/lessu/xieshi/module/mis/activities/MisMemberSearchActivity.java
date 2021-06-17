package com.lessu.xieshi.module.mis.activities;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.BaseVMActivity;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.mis.adapter.MisMemberSearchListAdapter;
import com.lessu.xieshi.module.mis.viewmodel.MisSearchViewModel;
import com.lessu.xieshi.module.mis.bean.MisMemberSearchResultData;
import com.scetia.Pro.baseapp.listener.AdapterItemClickListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class MisMemberSearchActivity extends BaseVMActivity<MisSearchViewModel> {
    @BindView(R.id.et_hy_search)
    EditText etMemberSearch;
    @BindView(R.id.lv_search_rv)
    RecyclerView recyclerView ;
    @BindView(R.id.mis_member_swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private MisMemberSearchListAdapter searchAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mis_huiyuanchaxun;
    }

    /**
     * 注册viewModel的数据监听
     */
    @Override
    protected void observerData() {
        //状态监听
        mViewModel.getSearchLoadState().observe(this, loadState -> {
            searchAdapter.setLoadState(loadState);
            swipeRefresh.setRefreshing(loadState==LoadState.LOAD_INIT);
        });
        //数据加入adapter中显示
        mViewModel.getPagedListLiveData().observe(this, listContentBeans -> searchAdapter.submitList(listContentBeans));
    }

    /**
     * 初始化控件
     */
    protected void initView() {
        this.setTitle("会员信息查询");
        swipeRefresh.setColorSchemeResources(R.color.blue_light1, R.color.blue_normal1, R.color.blue_normal2);
        //刷新列表
        swipeRefresh.setOnRefreshListener(() -> mViewModel.reFresh());
        searchAdapter = new MisMemberSearchListAdapter(mViewModel);
        searchAdapter.setAdapterItemClickListener((position, contentBean) -> {
            EventBus.getDefault().postSticky(contentBean);
            startOtherActivity(HyDetailActivity.class);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searchAdapter);
    }

    @OnClick(R.id.tv_hy_search)
    public void doSearch(){
        mViewModel.setQueryKey(etMemberSearch.getText().toString().trim());
    }
}
