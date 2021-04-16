package com.lessu.xieshi.module.mis.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.mis.bean.EvaluationComparisonBean;
import com.lessu.xieshi.module.mis.viewmodel.EvaluationComparisonDetailViewModel;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.common.Util.Constants;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by Lollipop
 * on 2021/4/15
 */
public class EvaluationComparisonDetailActivity extends NavigationActivity {
    @BindView(R.id.evaluation_comparison_detail_member_no)
    TextView evaluationComparisonDetailMemberNo;
    @BindView(R.id.evaluation_comparison_detail_member_name)
    TextView evaluationComparisonDetailMemberName;
    @BindView(R.id.evaluation_comparison_detail_state)
    TextView evaluationComparisonDetailState;
    @BindView(R.id.evaluation_comparison_detail_expired_date)
    TextView evaluationComparisonDetailExpiredDate;
    @BindView(R.id.evaluation_comparison_detail_apply_type)
    TextView evaluationComparisonDetailApplyType;
    @BindView(R.id.evaluation_comparison_detail_reissue_date)
    TextView evaluationComparisonDetailReissueDate;
    @BindView(R.id.evaluation_comparison_detail_remarks)
    TextView evaluationComparisonDetailRemarks;
    @BindView(R.id.evaluation_comparison_detail_apply_man)
    TextView evaluationComparisonDetailApplyMan;
    @BindView(R.id.evaluation_comparison_detail_apply_date)
    TextView evaluationComparisonDetailApplyDate;
    @BindView(R.id.evaluation_comparison_detail_type)
    TextView evaluationComparisonDetailType;
    @BindView(R.id.btn_evaluation_comparison_detail_approve)
    Button btnEvaluationComparisonDetailApprove;
    private EvaluationComparisonDetailViewModel viewModel;
    private EvaluationComparisonBean.EvaluationComparisonItem evaluationComparisonItem;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_evaluation_comparison_detail;
    }

    @Override
    protected void observerData() {
        viewModel = new ViewModelProvider(this).get(EvaluationComparisonDetailViewModel.class);
        viewModel.getLoadState().observe(this,loadState -> {
            switch (loadState) {
                case LOADING:
                    LSAlert.showProgressHud(this, getResources().getString(R.string.posting_data_text));
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(this, "", "批准成功", "确定", false, () -> {
                        EventBusUtil.sendEvent(new GlobalEvent<>(EventBusUtil.E,true));
                        finish();
                    });
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(this,loadState.getMessage());
                    break;
            }
        });
    }

    @Override
    protected void initView() {
        setTitle("证书打印详情");
    }

    @Override
    protected void initData() {
        evaluationComparisonItem = getIntent().getParcelableExtra(Constants.EvaluationComparison.KEY_CONTENT_BEAN);
        evaluationComparisonDetailMemberNo.setText(evaluationComparisonItem.getS1());
        evaluationComparisonDetailMemberName.setText(evaluationComparisonItem.getS11());
        evaluationComparisonDetailState.setText(evaluationComparisonItem.getS2Text());
        evaluationComparisonDetailExpiredDate.setText(evaluationComparisonItem.getS4());
        evaluationComparisonDetailApplyType.setText(evaluationComparisonItem.getS14Text());
        evaluationComparisonDetailType.setText(evaluationComparisonItem.getS7Text());
        evaluationComparisonDetailReissueDate.setText(formatDate(evaluationComparisonItem.getS6()));
        evaluationComparisonDetailRemarks.setText(evaluationComparisonItem.getS8());
        evaluationComparisonDetailApplyMan.setText(evaluationComparisonItem.getS9());
        evaluationComparisonDetailApplyDate.setText(formatDate(evaluationComparisonItem.getS10()));
        //如果是已经批准的项就不限已批准按钮了
        String approve = getIntent().getStringExtra(Constants.EvaluationComparison.KEY_APPROVE_TYPE);
        if(approve!=null&&approve.equals(Constants.EvaluationComparison.APPROVE_ENABLE)&&evaluationComparisonItem.getS2()
                .equals(Constants.EvaluationComparison.STATE_APPLYING+"")) {
            btnEvaluationComparisonDetailApprove.setVisibility(View.VISIBLE);
        }else{
            btnEvaluationComparisonDetailApprove.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_evaluation_comparison_detail_approve)
    public void ButtonClickDid(){
        //TODO:请求批准
        HashMap<String,Object> params = new HashMap<>();
        params.put("s1",evaluationComparisonItem.getAID());
        params.put("s2",evaluationComparisonItem.getS14());
        String s4 = evaluationComparisonItem.getS4();
        params.put("s3",formatDate(s4));
        params.put("s4",evaluationComparisonItem.getS1());
        viewModel.requestApprove(params);
    }

    private String formatDate(String date){
        if(date.contains(" ")){
            date = date.substring(0,date.indexOf(" "));
        }
        return date;
    }
}
