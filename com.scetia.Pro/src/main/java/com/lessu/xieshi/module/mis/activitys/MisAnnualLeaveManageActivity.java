package com.lessu.xieshi.module.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.scetia.Pro.common.Util.DensityUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.DateUtil;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.mis.adapter.MisAnnualLeaveListAdapter;
import com.lessu.xieshi.module.mis.bean.MisAnnualLeaveData;
import com.lessu.xieshi.module.mis.viewmodel.MisAnnualLeaveViewModel;
import com.lessu.xieshi.view.RecyclerViewItemDecoration;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lessu.xieshi.R.id.bt_njone_sq;
import static com.lessu.xieshi.R.id.bt_njtwo_sp;
import static com.lessu.xieshi.R.id.bt_njtwo_sq;

public class MisAnnualLeaveManageActivity extends NavigationActivity {
    public static final String SP_YEAR = "sp_year";
    @BindView(R.id.sp_nj_nianfen)
    Spinner spNJYear;
    @BindView(R.id.sp_nj_zt)
    Spinner spNJState;
    @BindView(R.id.bt_nj_search)
    Button btNjSearch;
    @BindView(bt_njtwo_sq)
    Button btNjtwoSq;
    @BindView(bt_njtwo_sp)
    Button btNjtwoSp;
    @BindView(R.id.ll_nj_two)
    LinearLayout llNjTwo;
    @BindView(bt_njone_sq)
    Button btNjoneSq;
    @BindView(R.id.ll_nj_one)
    LinearLayout llNjOne;
    @BindView(R.id.activity_misnianjia)
    LinearLayout activityMisnianjia;
    @BindView(R.id.rv_mis_annual_leave)
    RecyclerView annualLeaveRv;
    @BindView(R.id.mis_annual_leave_refresh)
    SwipeRefreshLayout misAnnualLeaveRefresh;
    @BindView(R.id.tv_mis_annual_leave_year)
    TextView tvMisAnnualLeaveYear;
    @BindView(R.id.tv_mis_annual_leave_state)
    TextView tvMisAnnualLeaveState;
    private String njApproveBtn;
    private MisAnnualLeaveViewModel viewModel;
    private MisAnnualLeaveListAdapter listAdapter;
    private Calendar curDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misnianjia);
        ButterKnife.bind(this);
        this.setTitle("年假管理");
        initDataListener();
        initView();
    }

    /**
     * 注册数据监听，更新UI界面
     */
    private void initDataListener() {
        viewModel = new ViewModelProvider(this).get(MisAnnualLeaveViewModel.class);
        viewModel.getPageLoadState().observe(this, new Observer<LoadState>() {
            @Override
            public void onChanged(LoadState loadState) {
                listAdapter.setLoadState(loadState);
                misAnnualLeaveRefresh.setRefreshing(loadState==LoadState.LOAD_INIT);
            }
        });

        viewModel.getAnnualLeaveData().observe(this, new Observer<MisAnnualLeaveData>() {
            @Override
            public void onChanged(MisAnnualLeaveData misAnnualLeaveData) {
                njApproveBtn = misAnnualLeaveData.getNJApproveBtn();
                if (njApproveBtn.equals("0")) {
                    llNjOne.setVisibility(View.VISIBLE);
                    llNjTwo.setVisibility(View.INVISIBLE);
                } else if (njApproveBtn.equals("1")) {
                    llNjOne.setVisibility(View.INVISIBLE);
                    llNjTwo.setVisibility(View.VISIBLE);
                }
            }
        });
        //将数据源添加到adapter中
        viewModel.getPagedListLiveData().observe(this, new Observer<PagedList<MisAnnualLeaveData.AnnualLeaveBean>>() {
            @Override
            public void onChanged(PagedList<MisAnnualLeaveData.AnnualLeaveBean> annualLeaveBeans) {
                listAdapter.submitList(annualLeaveBeans);
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        listAdapter = new MisAnnualLeaveListAdapter(viewModel);
        annualLeaveRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        int div = DensityUtil.dip2px(MisAnnualLeaveManageActivity.this,5);
        annualLeaveRv.addItemDecoration(new RecyclerViewItemDecoration(div));
        annualLeaveRv.setAdapter(listAdapter);
        misAnnualLeaveRefresh.setColorSchemeResources(R.color.blue_light1, R.color.blue_normal1, R.color.blue_normal2);
        misAnnualLeaveRefresh.setOnRefreshListener(() -> viewModel.reRefresh());
        curDate=Calendar.getInstance();
        //设置初始查询年份为当前年份
        tvMisAnnualLeaveYear.setText(String.valueOf(curDate.get(Calendar.YEAR)));
        //设置初始查询状态为”全部“
        tvMisAnnualLeaveState.setText(getResources().getStringArray(R.array.annual_leave_state)[0]);
    }

    @OnClick({R.id.bt_nj_search, R.id.bt_njtwo_sq, R.id.bt_njtwo_sp, R.id.bt_njone_sq,R.id.tv_mis_annual_leave_year,R.id.tv_mis_annual_leave_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_nj_search:
                viewModel.reRefresh();
                break;
            case R.id.bt_njtwo_sq:
            case R.id.bt_njone_sq:
                startOtherActivity(MisAnnualLeaveApplyActivity.class);
                break;
            case R.id.bt_njtwo_sp:
                Intent intentSP = new Intent(this, MisAnnualLeaveApprovalActivity.class);
                intentSP.putExtra(SP_YEAR, tvMisAnnualLeaveYear.getText().toString());
                startActivity(intentSP);
                break;
            case R.id.tv_mis_annual_leave_year:
                //弹出年份选择
                int curYear = curDate.get(Calendar.YEAR);
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                startDate.set(Calendar.YEAR,curYear-1);
                endDate.set(Calendar.YEAR,curYear+1);
                new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String format = DateUtil.sdfYearDate.format(date);
                        tvMisAnnualLeaveYear.setText(format);
                        viewModel.setQueryYear(format);
                    }
                }).setType(new boolean[]{true, false, false, false, false, false})
                .setRangDate(startDate,endDate).setDate(curDate).build().show();
                break;
            case R.id.tv_mis_annual_leave_state:
                String[] arr = getResources().getStringArray(R.array.annual_leave_state);
                OptionsPickerView<String> pvOption = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tvMisAnnualLeaveState.setText(arr[options1]);
                        if(options1==0){
                            //选中”全部“传入空字符串""
                            viewModel.setQueryState("");
                        }else if(options1==1){
                            //选中”申请中“
                            viewModel.setQueryState("0");
                        }else{
                            //选中”已批准“
                            viewModel.setQueryState("1");
                        }
                    }
                }).build();
                pvOption.setPicker(Arrays.asList(arr));
                pvOption.show();
                break;
        }
    }
}
