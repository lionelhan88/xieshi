package com.lessu.xieshi.module.unqualified;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.unqualified.adapter.ConstructionListAdapter;
import com.lessu.xieshi.module.unqualified.viewmodel.ConstructionListViewModel;
import com.scetia.Pro.common.Util.Constants;

import java.util.HashMap;

import butterknife.BindView;

public class UnqualifiedConstructionListActivity extends XieShiSlidingMenuActivity {
    @BindView(R.id.rv_construction_list)
    RecyclerView rvConstructionList;
    @BindView(R.id.frame_construction_list)
    FrameLayout frameConstructionList;
    @BindView(R.id.listView)
    PullToRefreshListView listView;
    private ConstructionListAdapter listAdapter;
    private ConstructionListViewModel viewModel;
    private View loadingView;

 /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.construction_list_activity);
        ButterKnife.bind(this);
        this.setTitle("工程列表");
        observerData();
        initView();
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.construction_list_activity;
    }

    @Override
    protected void observerData() {
        viewModel = new ViewModelProvider(this).get(ConstructionListViewModel.class);
        //传入初始化加载的参数
        viewModel.setParams(getParams());
        viewModel.getPageState().observe(this, new Observer<LoadState>() {
            @Override
            public void onChanged(LoadState loadState) {
                if (loadState != LoadState.LOAD_INIT) {
                    frameConstructionList.removeView(loadingView);
                } else {
                    if (loadingView == null) {
                        loadingView = LayoutInflater.from(UnqualifiedConstructionListActivity.this).inflate(R.layout.list_footer__layout,
                                null, false);
                    }
                    //显示加载提示
                    frameConstructionList.addView(loadingView);
                }
                listAdapter.setLoadState(loadState);
            }
        });

        viewModel.getPagedListLiveData().observe(this, constructionBeans -> {
            listAdapter.submitList(constructionBeans);
        });
    }

    private HashMap<String, Object> getParams() {
        String token =  Constants.User.GET_TOKEN();
        Bundle bundle = getIntent().getExtras();
        String MemberId = bundle.getString("MemberId");
        String StartDate = bundle.getString("StartDate");
        String EndDate = bundle.getString("EndDate");
        String QueryKey = bundle.getString("QueryKey");
        String KeyName = bundle.getString("KeyName");
        String QueryPower = bundle.getString("QueryPower");
        String Type = bundle.getString("Type");
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        params.put("MemberId", MemberId);
        params.put("StartDate", StartDate);
        params.put("EndDate", EndDate);
        params.put("QueryKey", QueryKey);
        params.put("KeyName", KeyName);
        params.put("QueryPower", QueryPower);
        params.put("Type", Type);
        return params;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        this.setTitle("工程列表");
        listAdapter = new ConstructionListAdapter(viewModel);
        listView.setVisibility(View.GONE);
        rvConstructionList.setVisibility(View.VISIBLE);
        rvConstructionList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvConstructionList.setAdapter(listAdapter);
    }

    public void menuButtonDidClick() {
        menu.toggle();
    }

}
