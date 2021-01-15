package com.lessu.xieshi.module.todaystatistics;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.bean.TodayStatisticsBean;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;

import java.util.ArrayList;
import java.util.Calendar;

public class TodayStatisticsActivity extends NavigationActivity implements  View.OnClickListener {
	private TextView tv_qianyitian;
	private TextView tv_houyitian;
	private LinearLayout ll_riqi;
	private TextView tv_riqi;
	private TextView tv_gcyanshouyp;
	private TextView tv_jdchoujianyp;
	private TextView tv_feigcyanshouyp;
	private TextView tv_tongjishijian;
	private RecyclerView rvToadyStatistics;
	private ToadyStatisticsListAdapter adapter;
	private TodayStatisticsViewModel viewModel;
	private String riqi;
	private int Year;
	private int Month;
	private int Day;
	private Calendar cal = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.today_statistics_activity);
		this.setTitle("今日统计");
		initDataListener();
		initView();
	}

	private void initDataListener() {
		viewModel = new ViewModelProvider(this).get(TodayStatisticsViewModel.class);
		viewModel.getLoadState().observe(this, new Observer<LoadState>() {
			@Override
			public void onChanged(LoadState loadState) {
				if(loadState==LoadState.LOADING){
					LSAlert.showProgressHud(TodayStatisticsActivity.this,"正在加载...");
				}else{
					LSAlert.dismissProgressHud();
				}
			}
		});
		viewModel.getThrowable().observe(this, new Observer<ExceptionHandle.ResponseThrowable>() {
			@Override
			public void onChanged(ExceptionHandle.ResponseThrowable throwable) {
				if(throwable.code==3000) {
					LSAlert.showAlert(TodayStatisticsActivity.this,"无相关记录！");
					adapter.setNewData(new ArrayList<>());
					tv_gcyanshouyp.setText("");
					tv_jdchoujianyp.setText("");
					tv_feigcyanshouyp.setText("");
					tv_tongjishijian.setText("");
				}else{
					LSAlert.showAlert(TodayStatisticsActivity.this,throwable.message);
				}
			}
		});

		viewModel.getTodayStatisticsLiveData().observe(this, new Observer<TodayStatisticsBean>() {
			@Override
			public void onChanged(TodayStatisticsBean todayStatisticsBean) {
				TodayStatisticsBean.JsonContentBean jsonContent = todayStatisticsBean.getJsonContent();
				tv_gcyanshouyp.setText(jsonContent.getDay_ImportSampleCount()+"");
				tv_jdchoujianyp.setText(jsonContent.getDay_JdSample()+"");
				tv_feigcyanshouyp.setText(jsonContent.getDay_JdSample()+"");
				String[] split = jsonContent.getRecordTime().split("\\s+");
				tv_tongjishijian.setText(split[0]);
				adapter.setNewData(jsonContent.getItemList());
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		tv_qianyitian = findViewById(R.id.tv_qianyitian);
		tv_houyitian = findViewById(R.id.tv_houyitian);
		ll_riqi = findViewById(R.id.ll_riqi);
		tv_riqi = findViewById(R.id.tv_riqi);
		tv_gcyanshouyp = findViewById(R.id.tv_gcyanshouyp);
		tv_jdchoujianyp = findViewById(R.id.tv_jdchoujianyp);
		tv_feigcyanshouyp = findViewById(R.id.tv_feigcyanshouyp);
		tv_tongjishijian = findViewById(R.id.tv_tongjishijian);
		rvToadyStatistics = findViewById(R.id.rv_toady_statistics);
		rvToadyStatistics.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
		adapter = new ToadyStatisticsListAdapter();
		rvToadyStatistics.setAdapter(adapter);
		tv_riqi.setText(getTitleDate(0));
		tv_qianyitian.setOnClickListener(this);
		tv_houyitian.setOnClickListener(this);
		ll_riqi.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		viewModel.loadData("0",riqi);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.tv_qianyitian:
				tv_riqi.setText(getTitleDate(-1));
				viewModel.loadData("0",riqi);
				break;
			case R.id.tv_houyitian:
				tv_riqi.setText(getTitleDate(1));
				viewModel.loadData("0",riqi);
				break;
			case R.id.ll_riqi:
				new DatePickerDialog(this, mDateSetListener, Year, Month, Day).show();
				break;
		}
	}
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			cal.set(year,monthOfYear,dayOfMonth);
			tv_riqi.setText(getTitleDate(0));
			viewModel.loadData("0",riqi);
		}
	};

	/**
	 * 显示当前日日期
	 * @param distanceDay
	 * @return
	 */
	private String getTitleDate(int distanceDay){
		//使用默认时区和语言环境获得一个日历。
		cal.add(Calendar.DAY_OF_MONTH,distanceDay);
		Year = cal.get(Calendar.YEAR);
		Month= cal.get(Calendar.MONTH);
		Day = cal.get(Calendar.DAY_OF_MONTH);
		formatDate(Year,Month,Day);
		return (Month+1)+"月"+Day+"日";
	}

	/**
	 * 格式化日期为"yyyyMMdd"
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 */
	private void formatDate(int year, int monthOfYear, int dayOfMonth) {
		String s1 = monthOfYear<9?"0" + (monthOfYear + 1):String.valueOf(monthOfYear + 1);
		String s2 = dayOfMonth<10? "0" + dayOfMonth:String.valueOf(dayOfMonth);
		riqi = year +s1+s2;
	}
}