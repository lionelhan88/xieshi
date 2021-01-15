package com.lessu.xieshi.module.sand.fragment;


import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.xieshi.R;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.sand.ObservableScrollView;
import com.lessu.xieshi.module.sand.adapter.ResultQueryDetailParameterAdapter;
import com.lessu.xieshi.module.sand.viewmodel.TestingInfoQueryDetailViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.baseapp.uitls.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.lessu.xieshi.module.sand.fragment.TestingInfoQueryListFragment.TESTING_QUERY_INFO_ID;

/**
 * created by ljs
 * on 2020/11/2
 */
public class TestingInfoQueryDetailFragment extends BaseVMFragment<TestingInfoQueryDetailViewModel> {
    @BindView(R.id.result_query_detail_sample_name)
    TextView resultQueryDetailSampleName;
    @BindView(R.id.sand_result_query_detail_top)
    LinearLayout sandResultQueryDetailTop;
    @BindView(R.id.result_query_detail_rv)
    RecyclerView resultQueryDetailRv;
    @BindView(R.id.testing_info_query_detail_serial)
    TextView testingInfoQueryDetailSerial;
    @BindView(R.id.testing_info_query_detail_sample_code)
    TextView testingInfoQueryDetailSampleCode;
    @BindView(R.id.testing_info_query_detail_sample_spec)
    TextView testingInfoQueryDetailSampleSpec;
    @BindView(R.id.testing_info_query_detail_sample_state)
    TextView testingInfoQueryDetailSampleState;
    @BindView(R.id.testing_info_query_detail_commission_date)
    TextView testingInfoQueryDetailCommissionDate;
    @BindView(R.id.testing_info_query_detail_testing_result)
    TextView testingInfoQueryDetailTestingResult;
    @BindView(R.id.result_query_detail_bottom_commission_name)
    TextView resultQueryDetailBottomCommissionName;
    @BindView(R.id.result_query_detail_bottom_ship_name)
    TextView resultQueryDetailBottomShipName;
    @BindView(R.id.result_query_detail_bottom_wharf)
    TextView resultQueryDetailBottomWharf;
    @BindView(R.id.result_query_detail_bottom_sales)
    TextView resultQueryDetailBottomSales;
    @BindView(R.id.center_info)
    ConstraintLayout centerInfo;

    @BindView(R.id.content_loading_layout)
    RelativeLayout contentLoadingLayout;
    @BindView(R.id.content_loading_progress)
    ContentLoadingProgressBar contentLoadingProgressBar;
    @BindView(R.id.content_loading_text)
    TextView contentLoadingText;
    @BindView(R.id.info_query_detail_scroll)
    ObservableScrollView infoQueryDetailScroll;
    private ResultQueryDetailParameterAdapter parameterAdapter;
    private String testingQueryInfoId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_testing_info_query_details;
    }

    @Override
    protected Class<TestingInfoQueryDetailViewModel> getViewModelClass() {
        return TestingInfoQueryDetailViewModel.class;
    }

    @Override
    protected void observerData() {
        viewModel.getLoadState().observe(this, loadState -> {
            switch (loadState) {
                case LOADING:
                    contentLoadingLayout.setVisibility(View.VISIBLE);
                    contentLoadingProgressBar.setVisibility(View.VISIBLE);
                    contentLoadingText.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    contentLoadingLayout.setVisibility(View.GONE);
                    break;
                case FAILURE:
                    contentLoadingText.setVisibility(View.VISIBLE);
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    break;
            }
        });

        viewModel.getThrowable().observe(this, responseThrowable -> {
            contentLoadingText.setText(responseThrowable.message);
        });

        viewModel.getSandParameterResult().observe(this, sandParameterResultBeans -> {
            parameterAdapter.setNewData(sandParameterResultBeans);
        });

        viewModel.getQueryResultBean().observe(this, testingQueryResultBean -> {
            testingInfoQueryDetailSerial.setText(testingQueryResultBean.getFlowInfoNo());//流水号
            resultQueryDetailSampleName.setText(testingQueryResultBean.getSampleName());//样品名称
            testingInfoQueryDetailSampleCode.setText(testingQueryResultBean.getSampleNo());//样品编号
            testingInfoQueryDetailSampleSpec.setText(testingQueryResultBean.getSpecName());//样品规格
            testingInfoQueryDetailSampleState.setText(testingQueryResultBean.getSampleStatusName());//样品状态
            testingInfoQueryDetailCommissionDate.setText(testingQueryResultBean.getConsignCreateDate());//委托日期
            testingInfoQueryDetailTestingResult.setText(testingQueryResultBean.getSampleExamResult());//样品检测结论
            resultQueryDetailBottomCommissionName.setText(testingQueryResultBean.getConsignNo());//委托编号
            resultQueryDetailBottomShipName.setText(testingQueryResultBean.getShipName());//船名
            resultQueryDetailBottomWharf.setText(testingQueryResultBean.getTerminalName());//码头
            resultQueryDetailBottomSales.setText(testingQueryResultBean.getCustomerUnitMemberName());//销售对象

        });
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.result_query_detail_text));
        parameterAdapter = new ResultQueryDetailParameterAdapter();
        resultQueryDetailRv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        resultQueryDetailRv.setAdapter(parameterAdapter);
        infoQueryDetailScroll.setScrollViewListener((x, y, oldx, oldy) -> {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) sandResultQueryDetailTop.getLayoutParams();
            ViewGroup.MarginLayoutParams layoutParams2 = (ViewGroup.MarginLayoutParams) resultQueryDetailSampleName.getLayoutParams();
            int height =sandResultQueryDetailTop.getPaddingTop()+layoutParams.topMargin+testingInfoQueryDetailSerial.getHeight()
                    +layoutParams2.topMargin+resultQueryDetailSampleName.getHeight();
            if(y<=height){
                setTitle(getResources().getString(R.string.result_query_detail_text));
            }else{
                setTitle(resultQueryDetailSampleName.getText().toString());
            }
        });
    }

    @Override
    protected void initData() {
        testingQueryInfoId = getArguments().getString(TESTING_QUERY_INFO_ID);
        viewModel.loadTestingInfo(testingQueryInfoId);
    }

    @OnClick(R.id.content_loading_layout)
    public void contentLoadingClick() {
        if (viewModel.getLoadState().getValue() == LoadState.FAILURE) {
            //只有加载失败才能点击响应
            viewModel.loadTestingInfo(testingQueryInfoId);
        }
    }


}
