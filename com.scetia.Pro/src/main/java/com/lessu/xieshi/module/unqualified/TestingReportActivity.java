package com.lessu.xieshi.module.unqualified;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.data.LoadState;
import com.lessu.xieshi.module.unqualified.adapter.TestingReportListAdapter;
import com.lessu.xieshi.module.unqualified.bean.TestingReportData;
import com.lessu.xieshi.module.unqualified.viewmodel.TestingReportListViewModel;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestingReportActivity extends NavigationActivity {
    @BindView(R.id.rv_construction_list)
    RecyclerView rvTestingReportList;
    @BindView(R.id.listView)
    PullToRefreshListView listView;
    @BindView(R.id.frame_construction_list)
    FrameLayout frameConstructionList;
    private TestingReportListViewModel viewModel;
    private TestingReportListAdapter listAdapter;
    private View loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.construction_list_activity);
        ButterKnife.bind(this);
        this.setTitle("检测报告");
        initDataListener();
        initView();
    }

    /**
     * 监听数据变化，更新UI
     */
    private void initDataListener() {
        viewModel = new ViewModelProvider(this).get(TestingReportListViewModel.class);
        viewModel.setQueryKey(getParams());
        viewModel.getPageLoadState().observe(this, new Observer<LoadState>() {
            @Override
            public void onChanged(LoadState loadState) {
                if(loadState!=LoadState.LOAD_INIT){
                    frameConstructionList.removeView(loadingView);
                }else{
                    if(loadingView==null) {
                        loadingView = LayoutInflater.from(TestingReportActivity.this).inflate(R.layout.list_footer__layout, null, false);
                    }
                    frameConstructionList.addView(loadingView);
                }
                listAdapter.setLoadState(loadState);
            }
        });

        //将输入源装入适配器
        viewModel.getPagedListLiveData().observe(this, new Observer<PagedList<TestingReportData.TestingReportBean>>() {
            @Override
            public void onChanged(PagedList<TestingReportData.TestingReportBean> testingReportBeans) {
                listAdapter.submitList(testingReportBeans);
            }
        });
    }

    private HashMap<String,Object> getParams(){
        String token = LSUtil.valueStatic("Token");
        Bundle bundle = TestingReportActivity.this.getIntent().getExtras();
        String ProjectName = bundle.getString("ProjectName");
        String ProjectArea = bundle.getString("ProjectArea");
        String Report_CreateDate = bundle.getString("Report_CreateDate");
        String EntrustUnitName = bundle.getString("EntrustUnitName");
        String BuildingReportNumber = bundle.getString("BuildingReportNumber");
        String EntrustType = bundle.getString("EntrustType");
        String ManageUnitID = bundle.getString("ManageUnitID");
        String UqExecStatus = bundle.getString("UqExecStatus");
        String ItemName = bundle.getString("ItemName");
        String type = bundle.getString("Type");
        String ItemID = bundle.getString("ItemID");

        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        params.put("Type", type);
        params.put("ProjectName", ProjectName);
        params.put("ProjectArea", ProjectArea);
        params.put("EntrustUnitName", EntrustUnitName);
        params.put("BuildingReportNumber", BuildingReportNumber);
        params.put("ItemID", ItemID);
        params.put("Report_CreateDate", Report_CreateDate);
        params.put("ItemName", ItemName);
        params.put("EntrustType", EntrustType);
        params.put("ManageUnitID", ManageUnitID);
        params.put("UqExecStatus", UqExecStatus);
        return params;
    }
    /**
     * 初始化控件
     */
    private void initView() {
        listAdapter = new TestingReportListAdapter(viewModel);
        listView.setVisibility(View.GONE);
        rvTestingReportList.setVisibility(View.VISIBLE);
        rvTestingReportList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvTestingReportList.setAdapter(listAdapter);
        listAdapter.setAdapterItemClickListener((position, testingReportBean) -> {
            Bundle bundle = new Bundle();
            bundle.putString("Report_id", testingReportBean.getReport_ID());
            bundle.putString("Checksum", testingReportBean.getIdentifyingCode());
            Intent intent = new Intent(TestingReportActivity.this, TestingReportContentActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}
