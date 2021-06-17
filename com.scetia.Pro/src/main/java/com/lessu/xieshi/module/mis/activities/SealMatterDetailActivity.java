package com.lessu.xieshi.module.mis.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.xieshi.utils.SealManageUtil;
import com.lessu.xieshi.http.service.MisApiService;
import com.lessu.xieshi.module.meet.activity.ScalePictureActivity;
import com.lessu.xieshi.module.mis.adapter.MatterTypeTagsAdapter;
import com.lessu.xieshi.module.mis.adapter.SealMatterDetailAnnexListAdapter;
import com.lessu.xieshi.module.mis.bean.SealManageBean;
import com.lessu.xieshi.module.web.WordPreviewActivity;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.XSRetrofit;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * created by Lollipop
 * on 2021/3/3
 */
public class SealMatterDetailActivity extends NavigationActivity {
    @BindView(R.id.seal_matter_detail_apply_man)
    TextView sealMatterDetailApplyMan;
    @BindView(R.id.seal_matter_detail_apply_state)
    TextView sealMatterDetailApplyState;
    @BindView(R.id.seal_matter_detail_apply_date)
    TextView sealMatterDetailApplyDate;
    @BindView(R.id.seal_matter_detail_apply_content)
    TextView sealMatterDetailApplyContent;
    @BindView(R.id.rv_seal_matter_detail_annex)
    RecyclerView rvSealMatterDetailAnnex;
    @BindView(R.id.seal_matter_detail_review_man)
    TextView sealMatterDetailReviewMan;
    @BindView(R.id.seal_matter_detail_review_date)
    TextView sealMatterDetailReviewDate;
    @BindView(R.id.seal_matter_detail_approve_man)
    TextView sealMatterDetailApproveMan;
    @BindView(R.id.seal_matter_detail_approve_date)
    TextView sealMatterDetailApproveDate;
    @BindView(R.id.seal_matter_detail_stamp_man)
    TextView sealMatterDetailStampMan;
    @BindView(R.id.seal_matter_detail_stamp_date)
    TextView sealMatterDetailStampDate;
    @BindView(R.id.btn_seal_matter_detail_approve)
    Button btnSealMatterDetailApprove;
    @BindView(R.id.ll_annex_list)
    LinearLayout llAnnexList;
    @BindView(R.id.cb_is_stamp)
    CheckBox cbIsStamp;
    @BindView(R.id.seal_matter_detail_stamp_sign)
    TextView tvStampSign;
    @BindView(R.id.seal_matter_detail_flow_types)
    TagFlowLayout matterTypesFlowLayout;
    @BindView(R.id.btn_seal_matter_detail_un_approve)
    Button btnSealMatterDetailUnApprove;
    @BindView(R.id.btn_seal_matter_detail_revoke)
    Button btnSealMatterDetailRevoke;
    private String curMatterId;
    private int role;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seal_matter_detail;
    }

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.seal_matter_detail_name));
    }

    @Override
    protected void initData() {
        SealManageBean sealManage = getIntent().getParcelableExtra(Constants.SealManage.KEY_SEAL_MANAGE_BEAN);
        if (sealManage != null) {
            curMatterId = sealManage.getYzId();
            sealMatterDetailApplyMan.setText(sealManage.getApplyMan());
            sealMatterDetailApplyDate.setText(SealManageUtil.formatDate(sealManage.getApplyDate()));
            int state = sealManage.getYzState();
            //事项状态颜色
            int stateTextColorByState = SealManageUtil.getStateTextColorByState(this, state);
            sealMatterDetailApplyState.setTextColor(stateTextColorByState);
            sealMatterDetailApplyState.setText(sealManage.getYzStateText());
            //获取当前账号的角色
            role = sealManage.getIsLd();
            checkApproveBtnState(state, role);
            String yzTypeText = sealManage.getYzTypeText();
            if (!TextUtils.isEmpty(sealManage.getYzTypeText())) {
                List<String> types = Arrays.asList(yzTypeText.split(" "));
                MatterTypeTagsAdapter adapter = new MatterTypeTagsAdapter(types);
                matterTypesFlowLayout.setAdapter(adapter);
            }
            sealMatterDetailApplyContent.setText(sealManage.getYzContent());
            sealMatterDetailReviewMan.setText(SealManageUtil.formatStr(sealManage.getShMan()));
            sealMatterDetailReviewDate.setText(SealManageUtil.formatDate(sealManage.getShDate()));
            sealMatterDetailApproveMan.setText(SealManageUtil.formatStr(sealManage.getPzMan()));
            sealMatterDetailApproveDate.setText(SealManageUtil.formatDate(sealManage.getPzDate()));
            sealMatterDetailStampMan.setText(SealManageUtil.formatStr(sealManage.getGzMan()));
            sealMatterDetailStampDate.setText(SealManageUtil.formatDate(sealManage.getGzDate()));
            List<SealManageBean.YzFjInfoBean> yzFjInfo = sealManage.getYzFjInfo();
            if (yzFjInfo == null || yzFjInfo.isEmpty()) {
                llAnnexList.setVisibility(View.GONE);
            } else {
                initAnnexList(yzFjInfo);
            }
            //显示是否盖章
            cbIsStamp.setChecked(sealManage.getIsGz() == Constants.SealManage.STAMP_OK);
            String sealNumbers = sealManage.getGzFs();
            String cbIsStampLabel = "0";
            String stampSign = "—";
            if (!TextUtils.isEmpty(sealNumbers) && sealManage.getIsGz() == Constants.SealManage.STAMP_OK) {
                String[] split = sealNumbers.split("-");
                cbIsStampLabel = split[0];
                stampSign = split[1];
            }
            cbIsStamp.setText("盖章:" + cbIsStampLabel + "份");
            tvStampSign.setText(stampSign);
        }
    }

    /**
     * 初始化附件列表
     *
     * @param yzFjInfo
     */
    private void initAnnexList(List<SealManageBean.YzFjInfoBean> yzFjInfo) {
        SealMatterDetailAnnexListAdapter annexListAdapter = new SealMatterDetailAnnexListAdapter();
        rvSealMatterDetailAnnex.setLayoutManager(new LinearLayoutManager(this));
        rvSealMatterDetailAnnex.setAdapter(annexListAdapter);
        annexListAdapter.setNewData(yzFjInfo);
        annexListAdapter.setOnItemClickListener((adapter, view, position) -> {
            //注意：这里的文件有多种类型，如果是图片需要单独处理
            SealManageBean.YzFjInfoBean bean = (SealManageBean.YzFjInfoBean) adapter.getItem(position);
            String filePath = bean.getFilePath();
            String fileType = filePath.substring(filePath.lastIndexOf("."));
            if (isPicture(fileType)) {
                //如果是图片则点击时放大
                Intent scaleIntent = new Intent(this, ScalePictureActivity.class);
                scaleIntent.putExtra("commission_detail_photo", Constants.SealManage.ANNEX_HOST + bean.getFilePath());
                startActivity(scaleIntent);
            } else {
                Intent intent = new Intent(this, WordPreviewActivity.class);
                intent.putExtra(Constants.SealManage.KEY_YZ_FJ_BEAN, bean);
                startActivity(intent);
            }
        });
    }

    /**
     * 判断文件后缀名是否是图片类型
     *
     * @return
     */
    private boolean isPicture(String fileType) {
        String curFileType = fileType.toLowerCase();
        if (curFileType.equals(".jpg") || curFileType.equals(".jpeg") || curFileType.equals(".png") || curFileType.equals(".gif")
                || curFileType.equals(".bmp") || curFileType.equals("webp")) {
            return true;
        }
        return false;
    }

    /**
     * 根据当前事项状和角色态显示“操作”按钮
     *
     * @param state
     */
    private void checkApproveBtnState(int state, int role) {
        switch (state) {
            case Constants.SealManage.STATE_APPLYING:
                //申请中
                btnSealMatterDetailRevoke.setVisibility(View.GONE);
                if (role == Constants.SealManage.SECRETARY_GENERAL) {
                    btnSealMatterDetailApprove.setVisibility(View.GONE);
                    btnSealMatterDetailUnApprove.setVisibility(View.GONE);
                } else if (role == Constants.SealManage.DEPARTMENT_LEADER) {
                    btnSealMatterDetailApprove.setVisibility(View.VISIBLE);
                    btnSealMatterDetailUnApprove.setVisibility(View.VISIBLE);
                    btnSealMatterDetailApprove.setText("审核");
                    btnSealMatterDetailUnApprove.setText("审核不通过");
                }
                break;
                //已审核
            case Constants.SealManage.STATE_REVIEWED:
                //审核未通过
            case Constants.SealManage.STATE_UN_REVIEWED:
                if (role == Constants.SealManage.SECRETARY_GENERAL) {
                    //如果是秘书长，显示“批准”
                    btnSealMatterDetailApprove.setVisibility(View.VISIBLE);
                    btnSealMatterDetailUnApprove.setVisibility(View.VISIBLE);
                    btnSealMatterDetailRevoke.setVisibility(View.GONE);
                    btnSealMatterDetailApprove.setText("批准");
                    btnSealMatterDetailUnApprove.setText("批准不通过");
                } else if (role == Constants.SealManage.DEPARTMENT_LEADER) {
                    //如果是部门领导，只有在“申请中”才能显示“审核”按钮
                    btnSealMatterDetailApprove.setVisibility(View.GONE);
                    btnSealMatterDetailUnApprove.setVisibility(View.GONE);
                    //显示“撤销”按钮
                    btnSealMatterDetailRevoke.setVisibility(View.VISIBLE);
                }
                break;
                //已批准
            case Constants.SealManage.STATE_APPROVED:
                //批准未通过
            case Constants.SealManage.STATE_UN_APPROVED:
                if (role == Constants.SealManage.SECRETARY_GENERAL) {
                    btnSealMatterDetailApprove.setVisibility(View.GONE);
                    btnSealMatterDetailUnApprove.setVisibility(View.GONE);
                    btnSealMatterDetailRevoke.setVisibility(View.VISIBLE);
                } else if (role == Constants.SealManage.DEPARTMENT_LEADER) {
                    btnSealMatterDetailApprove.setVisibility(View.GONE);
                    btnSealMatterDetailUnApprove.setVisibility(View.GONE);
                    btnSealMatterDetailRevoke.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 提交操作数据
     *
     * @param curMatterId
     * @param operate
     */
    private void requestMatter(String curMatterId, int operate) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(Constants.User.XS_TOKEN, Constants.User.GET_TOKEN());
        param.put("S1", curMatterId);
        param.put("S2", operate);
        LSAlert.showProgressHud(this, "正在提交...");
        XSRetrofit.getInstance().getService(MisApiService.class)
                .approveSealMatter(GsonUtil.mapToJsonStr(param))
                .compose(XSRetrofit.applyTransformer())
                .subscribe(new ResponseObserver<Object>() {
                    @Override
                    public void success(Object o) {
                        LSAlert.dismissProgressHud();
                        //TODO：操作数据成功，返回列表，刷新
                        EventBusUtil.sendEvent(new GlobalEvent<>(EventBusUtil.A, true));
                        LSAlert.showAlert(SealMatterDetailActivity.this, "", "提交成功"
                                , "确定", false, () -> finish());
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        LSAlert.dismissProgressHud();
                        LSAlert.showAlert(SealMatterDetailActivity.this, throwable.message);
                    }
                });
    }

    @OnClick({R.id.btn_seal_matter_detail_approve, R.id.btn_seal_matter_detail_un_approve, R.id.btn_seal_matter_detail_revoke})
    public void onClick(View view) {
        int operate = 0;
        switch (view.getId()) {
            case R.id.btn_seal_matter_detail_approve:
                //TODO:提交批准或者审核请求
                operate = role == Constants.SealManage.SECRETARY_GENERAL ?
                        Constants.SealManage.OPERATE_APPROVE : Constants.SealManage.OPERATE_REVIEW;
                break;
            case R.id.btn_seal_matter_detail_un_approve:
                //TODO:不同意审核或者批准
                operate = role == Constants.SealManage.OPERATE_APPROVE ?
                        Constants.SealManage.OPERATE_UN_APPROVE : Constants.SealManage.OPERATE_UN_REVIEW;
                break;
            case R.id.btn_seal_matter_detail_revoke:
                //TODO:撤销操作
                operate = role == Constants.SealManage.SECRETARY_GENERAL ? Constants.SealManage.OPERATE_APPROVE_REVOKE :
                        Constants.SealManage.OPERATE_REVIEW_REVOKE;
        }
        requestMatter(curMatterId, operate);
    }

}
