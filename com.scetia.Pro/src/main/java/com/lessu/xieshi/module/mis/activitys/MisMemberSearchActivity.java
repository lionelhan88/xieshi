package com.lessu.xieshi.module.mis.activitys;

import android.os.Bundle;
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
import com.lessu.xieshi.bean.LoadState;
import com.lessu.xieshi.module.mis.adapter.MisMemberSearchListAdapter;
import com.lessu.xieshi.module.mis.viewmodel.MisSearchViewModel;
import com.lessu.xieshi.module.mis.bean.MisHySearchResultData;
import com.lessu.xieshi.module.mis.listener.AdapterItemClickListener;

import org.greenrobot.eventbus.EventBus;

public class MisMemberSearchActivity extends NavigationActivity implements View.OnClickListener {
    private EditText et_hy_search;
    private TextView tv_hy_search;
    private MisSearchViewModel viewModel;
    private MisMemberSearchListAdapter searchAdapter;
    private RecyclerView recyclerView ;
    private SwipeRefreshLayout swipeRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_huiyuanchaxun);
        this.setTitle("会员信息查询");
        initDataListener();
        initView();
    }

    /**
     * 注册viewModel的数据监听
     */
    private void initDataListener() {
        viewModel = new ViewModelProvider(this).get(MisSearchViewModel.class);
        //状态监听
        viewModel.getSearchLoadState().observe(this, new Observer<LoadState>() {
            @Override
            public void onChanged(LoadState loadState) {
                searchAdapter.setLoadState(loadState);
                swipeRefresh.setRefreshing(loadState==LoadState.LOAD_INIT);
            }
        });
        //数据加入adapter中显示
        viewModel.getPagedListLiveData().observe(this, new Observer<PagedList<MisHySearchResultData.ListContentBean>>() {
            @Override
            public void onChanged(PagedList<MisHySearchResultData.ListContentBean> listContentBeans) {
                searchAdapter.submitList(listContentBeans);
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        swipeRefresh = findViewById(R.id.mis_member_swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.blue_light1, R.color.blue_normal1, R.color.blue_normal2);
        //刷新列表
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.reFresh();
            }
        });
        searchAdapter = new MisMemberSearchListAdapter(viewModel);
        searchAdapter.setAdapterItemClickListener(new AdapterItemClickListener<MisHySearchResultData.ListContentBean>() {
            @Override
            public void onItemClickListener(int position, MisHySearchResultData.ListContentBean contentBean) {
                EventBus.getDefault().postSticky(contentBean);
                startOtherActivity(HyDetailActivity.class);
            }
        });

        recyclerView = findViewById(R.id.lv_search_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(searchAdapter);

        et_hy_search = findViewById(R.id.et_hy_search);
        tv_hy_search = findViewById(R.id.tv_hy_search);
        tv_hy_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        viewModel.setQueryKey(et_hy_search.getText().toString().trim());
    }
}
