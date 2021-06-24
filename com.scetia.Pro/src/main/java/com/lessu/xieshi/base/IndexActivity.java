package com.lessu.xieshi.base;

import android.view.KeyEvent;

import androidx.lifecycle.ViewModelProvider;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.login.viewmodel.FirstViewModel;
import com.lessu.xieshi.module.login.viewmodel.FirstViewModelFactory;
import com.lessu.xieshi.module.login.viewmodel.LoginViewModel;
import com.lessu.xieshi.module.meet.activity.MeetingListActivity;
import com.lessu.xieshi.module.mis.activities.EvaluationComparisonPrintActivity;
import com.lessu.xieshi.module.mis.activities.MisAnnualLeaveManageActivity;
import com.lessu.xieshi.module.mis.activities.MisCertificateSearchActivity;
import com.lessu.xieshi.module.mis.activities.MisComparisonAprovalActivity;
import com.lessu.xieshi.module.mis.activities.MisMemberSearchActivity;
import com.lessu.xieshi.module.mis.activities.MisNoticesActivity;
import com.lessu.xieshi.module.mis.activities.MisPingGuActivity;
import com.lessu.xieshi.module.mis.activities.SealManageListActivity;
import com.lessu.xieshi.module.mis.bean.MisGuideBean;
import com.lessu.xieshi.utils.DeviceUtil;
import com.lessu.xieshi.utils.ToastUtil;
import com.lessu.xieshi.utils.UpdateAppUtil;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.network.bean.ExceptionHandle;

import java.util.ArrayList;

public abstract class IndexActivity extends BaseVMActivity<FirstViewModel> {

    @Override
    protected FirstViewModel createViewModel() {
        return new ViewModelProvider(this, new FirstViewModelFactory(this.getApplication(), this))
                .get(FirstViewModel.class);
    }

    /**
     * 监听数据变化，更新UI界面
     */
    @Override
    protected void observerData() {
        mViewModel.getMapLiveData().observe(this, map -> {
            String userPower = (String) map.get(LoginViewModel.TO_ACTIVITY);
            initMenu(userPower);
        });
    }

    @Override
    protected void initView() {
        login();
        //检查软件是否需要更新
        UpdateAppUtil.checkUpdateApp(this, false);
    }

    /**
     * 自动登陆
     */
    private void login() {
        //每次进入主界面要重新登陆获取数据，可能更新权限
        if (SPUtil.getSPConfig(SPUtil.AUTO_LOGIN_KEY, false)) {
            String userName = SPUtil.getSPConfig(Constants.User.KEY_USER_NAME, "");
            String password = SPUtil.getSPConfig(Constants.User.KEY_PASSWORD, "");
            //如果开启自动登录，进入页面需要自动登录
            mViewModel.login(userName, password,DeviceUtil.getDeviceId(this));
        } else {
            String userPower = SPUtil.getSPConfig(Constants.User.KEY_USER_POWER, "");
            initMenu(userPower);
        }
    }

    /**
     * 根据权限码显示对应的菜单项
     * @param userPower 用户权限码
     */
    private void initMenu(String userPower) {
        String misUserPower = userPower.substring(16);
        String externalUserPower = userPower.substring(0,16);
        if(misUserPower.contains("1")){
            initMisMenu(misUserPower);
        }else{
            initExternalMenu(externalUserPower);
        }
        //界面初始化完成，开启自动登录
        SPUtil.setSPConfig(SPUtil.AUTO_LOGIN_KEY, true);
    }

    /**
     * 协会内部用户显示的菜单项
     */
    private void initMisMenu(String userPower){
        ArrayList<MisGuideBean> menuArray = new ArrayList<>();
        MisGuideBean memberInfoSearch = new MisGuideBean(R.drawable.huiyuanxinxi, "会员信息查询", MisMemberSearchActivity.class);
        MisGuideBean certificateInfoSearch = new MisGuideBean(R.drawable.zhengshuxinxi, "证书信息查询", MisCertificateSearchActivity.class);
        MisGuideBean evaluationInfoSearch = new MisGuideBean(R.drawable.pingguchaxun, "评估信息查询", MisPingGuActivity.class);
        MisGuideBean notice = new MisGuideBean(R.drawable.xinxitongzhi, "信息通知", MisNoticesActivity.class);
        MisGuideBean annualManage = new MisGuideBean(R.drawable.nianjiashenqing, "年假管理", MisAnnualLeaveManageActivity.class);
        //2018-10-16新增功能模块 “比对审批”
        MisGuideBean comparisonApproval = new MisGuideBean(R.drawable.shujubidui, "比对审批", MisComparisonAprovalActivity.class);
        MisGuideBean meetingManager = new MisGuideBean(R.drawable.icon_mis_meeting, "会议安排", MeetingListActivity.class);
        MisGuideBean sealManager = new MisGuideBean(R.drawable.ic_mis_matter_approve, "事项审批", SealManageListActivity.class);
        MisGuideBean evaluation = new MisGuideBean(R.drawable.ic_evaluation_comparison, "证书打印", EvaluationComparisonPrintActivity.class);
        char s1 = userPower.charAt(1);
        char s2 = userPower.charAt(2);
        char s3 = userPower.charAt(3);
        char s4 = userPower.charAt(4);
        char s5 = userPower.charAt(5);
        char s6 = userPower.charAt(0);
        char s7 = userPower.charAt(6);
        char s8 = '0';
        if (userPower.length() >= 8) {
            s8 = userPower.charAt(7);
        }
        if (s1 == '1') {
            menuArray.add(memberInfoSearch);
        }
        if (s2 == '1') {
            menuArray.add(certificateInfoSearch);
        }
        if (s3 == '1') {
            menuArray.add(evaluationInfoSearch);
        }
        if (s4 == '1') {
            menuArray.add(notice);
        }
        if (s5 == '1') {
            menuArray.add(annualManage);
        }
        if (s6 == '1') {
            //新增的权限“比对审批”
            menuArray.add(comparisonApproval);
        }
        if (s7 == '1') {
            menuArray.add(sealManager);
        }
        menuArray.add(meetingManager);
        if (s8 == '1') {
            menuArray.add(evaluation);
        }

        for (int i = menuArray.size() - 1; i >= 0; i--) {
            addItemView(menuArray.get(i));
        }
    }

    /**
     * 外部企业显示的菜单项
     */
    protected void initExternalMenu(String userPower) {

    }

    /**
     * 添加菜单选项
     *
     * @param misguidebean 菜单选项
     */
    protected void addItemView(MisGuideBean misguidebean) {

    }

    @Override
    protected void inLoading(LoadState loadState) {
        LSAlert.showProgressHud(this, getResources().getString(R.string.login_loading_text));
    }

    @Override
    protected void inFailure(LoadState loadState) {
        LSAlert.dismissProgressHud();
        switch (loadState.getCode()) {
            case 3000:
                LSAlert.showAlert(this, "提示", loadState.getMessage() + "\n是否重新登录？"
                        , "确定", false, () -> DeviceUtil.loginOut(this));
                break;
            case ExceptionHandle.NETWORK_ERROR:
                LSAlert.showAlert(this, "提示", loadState.getMessage(), "重试", "退出", false
                        , new LSAlert.AlertCallback() {
                            @Override
                            public void onConfirm() {
                                mViewModel.login(
                                        SPUtil.getSPConfig(Constants.User.KEY_USER_NAME, ""),
                                        SPUtil.getSPConfig(Constants.User.KEY_PASSWORD, ""),
                                        DeviceUtil.getDeviceId(IndexActivity.this)
                                );
                            }

                            @Override
                            public void onCancel() {
                                AppApplication.exit();
                            }
                        });
                break;
            default:
                LSAlert.showAlert(this, "提示", loadState.getMessage());
                break;
        }
    }

    private long time = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - time > 2000) {
                time = System.currentTimeMillis();
                ToastUtil.showShort(getString(R.string.logout_text));
                return true;
            } else {
                AppApplication.exit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
