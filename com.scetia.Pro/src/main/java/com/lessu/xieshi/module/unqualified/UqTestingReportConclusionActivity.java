package com.lessu.xieshi.module.unqualified;

import java.util.HashMap;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.module.mis.activitys.Content;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.unqualified.bean.ReportConclusionBean;
import com.lessu.xieshi.module.unqualified.viewmodel.TestingReportConclusionViewModel;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UqTestingReportConclusionActivity extends NavigationActivity {
	private RecyclerView rvUqTestingReportConclusion;
	private UqTestingReportConclusionAdapter listAdapter;
	private TestingReportConclusionViewModel viewModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uq_sample_list_activity);
		this.setTitle("报告结论");
		rvUqTestingReportConclusion = findViewById(R.id.rv_uq_testing_report_conclusion);
		intDataListener();
		initView();
	}

	private void intDataListener() {
		viewModel = new ViewModelProvider(this).get(TestingReportConclusionViewModel.class);

		viewModel.getLoadState().observe(this, loadState -> {
			if(loadState==LoadState.LOADING){
				LSAlert.showProgressHud(UqTestingReportConclusionActivity.this,"正在加载...");
			}else{
				LSAlert.dismissProgressHud();
			}
		});

		viewModel.getThrowable().observe(this, throwable -> ToastUtil.showShort(throwable.message));

		viewModel.getReportConclusionBeansLiveData().observe(this,reportConclusionBeans -> {
			listAdapter.setNewData(reportConclusionBeans);
		});
	}

	private void initView() {
		listAdapter = new UqTestingReportConclusionAdapter();
		rvUqTestingReportConclusion.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
		rvUqTestingReportConclusion.setAdapter(listAdapter);

		HashMap<String, Object> params = new HashMap<>();
		Bundle bundle=getIntent().getExtras();
		final String Report_ID = bundle.getString("Report_id");
		String Checksum = bundle.getString("Checksum");
		String Token = Content.getToken();
		params.put("Report_id", Report_ID);
		params.put("Checksum", Checksum);
		params.put("Token", Token);

		listAdapter.setOnItemClickListener((adapter1, view, position) -> {
			Intent intent = new Intent (UqTestingReportConclusionActivity.this, SampleDetailActivity.class) ;
			Bundle bundleDetail = new Bundle();
			ReportConclusionBean bean = (ReportConclusionBean) adapter1.getItem(position);
			bundleDetail.putString("Report_id", Report_ID);
			bundleDetail.putString("Checksum", Checksum);
			bundleDetail.putString("Sample_id",bean.getSampleId());
			intent.putExtras(bundleDetail);
			startActivity(intent);
		});
		viewModel.loadData(params);
	}

	static class UqTestingReportConclusionAdapter extends BaseQuickAdapter<ReportConclusionBean, BaseViewHolder> {
		public UqTestingReportConclusionAdapter() {
			super(R.layout.uq_testing_report_conclusion_item);
		}

		@Override
		protected void convert(BaseViewHolder helper, ReportConclusionBean item) {
			helper.setText(R.id.sampleIdTextView,item.getSampleId());
			helper.setText(R.id.examResultTextView,item.getExamResult());
		}
	}
}
