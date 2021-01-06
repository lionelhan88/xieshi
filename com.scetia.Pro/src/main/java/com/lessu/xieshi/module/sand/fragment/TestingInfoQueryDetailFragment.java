package com.lessu.xieshi.module.sand.fragment;


import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.BaseFragment;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.adapter.ResultQueryDetailParamterAdapter;
import com.lessu.xieshi.module.sand.bean.TestingQueryResultBean;

import butterknife.BindView;

/**
 * created by ljs
 * on 2020/11/2
 */
public class TestingInfoQueryDetailFragment extends BaseFragment {
    @BindView(R.id.result_query_detail_sample_name)
    TextView resultQueryDetailSampleName;
    @BindView(R.id.sand_result_query_detail_top)
    LinearLayout sandResultQueryDetailTop;
    @BindView(R.id.result_query_detail_rv)
    RecyclerView resultQueryDetailRv;
    private ResultQueryDetailParamterAdapter paramterAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_testing_info_query_details;
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.result_query_detail_text));
        paramterAdapter = new ResultQueryDetailParamterAdapter();
        resultQueryDetailRv.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));
        resultQueryDetailRv.setAdapter(paramterAdapter);
    }

    @Override
    protected void initData() {
        for (int i=0;i<15;i++){
            TestingQueryResultBean testingQueryResultBean = new TestingQueryResultBean();
            testingQueryResultBean.setSampleName("中沙");
            testingQueryResultBean.setParameterDetectValue("111");
            testingQueryResultBean.setParameterDetectResult("检测结果");
            paramterAdapter.addData(testingQueryResultBean);
        }
    }

}
