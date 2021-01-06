package com.lessu.xieshi.module.unqualified;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.bean.ListSampleDetail;
import com.lessu.data.LoadState;
import com.lessu.xieshi.module.unqualified.viewmodel.TestingReportContentViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestingReportContentActivity extends NavigationActivity  {
    @BindView(R.id.ll_report_conclusion)
    LinearLayout llReportConclusion;
    @BindView(R.id.rv_testing_report_content)
    RecyclerView rvTestingReportContent;
    private ReportContentInfoAdapter adapter;
    private ArrayList<ListSampleDetail> infoList = new ArrayList<>();
    private TestingReportContentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_content_activity);
        ButterKnife.bind(this);
        this.setTitle("检测报告内容");
        initDataListener();
        initView();
    }

    private void initDataListener() {
        viewModel = new ViewModelProvider(this).get(TestingReportContentViewModel.class);
        viewModel.getLoadState().observe(this, loadState -> {
            if (loadState == LoadState.LOADING) {
                LSAlert.showProgressHud(TestingReportContentActivity.this, "正在加载数据...");
            } else {
                LSAlert.dismissProgressHud();
            }
        });

        viewModel.getThrowable().observe(this, throwable -> {
            ToastUtil.showShort(throwable.message);
        });

        viewModel.getReportContentLiveData().observe(this, reportContentBean -> {
            infoList.clear();
            infoList.add(new ListSampleDetail("报告编号", reportContentBean.getReport_ID()));
            infoList.add(new ListSampleDetail("委托编号", reportContentBean.getConSign_ID()));
            infoList.add(new ListSampleDetail("检测类别", reportContentBean.getExam_Kind()));
            infoList.add(new ListSampleDetail("工程连续号", reportContentBean.getProject_SSN()));
            infoList.add(new ListSampleDetail("委托单位", reportContentBean.getEntrustUnitName()));
            infoList.add(new ListSampleDetail("工程名称", reportContentBean.getProJectName()));
            infoList.add(new ListSampleDetail("防伪校验码", reportContentBean.getIdentifyingCode()));
            infoList.add(new ListSampleDetail("工程地址", reportContentBean.getProjectAddress()));
            infoList.add(new ListSampleDetail("施工单位", reportContentBean.getBuildUnitName()));
            infoList.add(new ListSampleDetail("见证单位", reportContentBean.getSuperviseUnitName()));
            infoList.add(new ListSampleDetail("取样人(编号)", reportContentBean.getSampling()));
            infoList.add(new ListSampleDetail("见证人(编号)", reportContentBean.getWitness()));
            infoList.add(new ListSampleDetail("委托日期", reportContentBean.getDetectonDate()));
            infoList.add(new ListSampleDetail("报告日期", reportContentBean.getReport_CreateTime()));
            infoList.add(new ListSampleDetail("检测机构名称", reportContentBean.getMemberName()));
            infoList.add(new ListSampleDetail("检测单位地址", reportContentBean.getContactAddress()));
            adapter.setNewData(infoList);
        });
    }

    /**
     * 初始化控件和数据
     */
    private void initView() {
        adapter = new ReportContentInfoAdapter();
        rvTestingReportContent.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvTestingReportContent.setAdapter(adapter);

        //加载数据
        HashMap<String, Object> params = new HashMap<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        String Report_id = bundle.getString("Report_id");
        String Checksum = bundle.getString("Checksum");
        String Token = LSUtil.valueStatic("Token");
        params.put("Token", Token);
        params.put("Report_id", Report_id);
        params.put("Checksum", Checksum);
        viewModel.loadData(params);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick(R.id.ll_report_conclusion)
    public void openReportConclusion() {
        Intent intent = new Intent(TestingReportContentActivity.this, UqTestingReportConclusionActivity.class);
        Bundle bundle = new Bundle();
        Bundle bundelForData = this.getIntent().getExtras();
        String Report_ID = bundelForData.getString("Report_id");
        String Checksum = bundelForData.getString("Checksum");
        bundle.putString("Report_id", Report_ID);
        bundle.putString("Checksum", Checksum);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    static class ReportContentInfoAdapter extends BaseQuickAdapter<ListSampleDetail, BaseViewHolder> {

        public ReportContentInfoAdapter() {
            super(R.layout.unqualified_testing_report_content_info_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, ListSampleDetail item) {
            helper.setText(R.id.item_tv1,item.tv1);
            helper.setText(R.id.item_tv2,item.tv2);
            if (helper.getAdapterPosition() % 2 == 0) {
                helper.itemView.setBackgroundColor(Color.parseColor("#EAEAEA"));
            } else {
                helper.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }
    }
}
