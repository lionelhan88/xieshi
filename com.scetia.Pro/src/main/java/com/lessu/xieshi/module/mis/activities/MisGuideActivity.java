package com.lessu.xieshi.module.mis.activities;

import android.Manifest;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.Utils.SettingUtil;
import com.lessu.xieshi.module.login.FirstActivity;
import com.lessu.xieshi.module.login.viewmodel.LoginViewModel;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.SPUtil;
import com.lessu.xieshi.module.login.viewmodel.FirstViewModel;
import com.lessu.xieshi.module.login.viewmodel.FirstViewModelFactory;
import com.lessu.xieshi.module.weather.WeatherDetailActivity;
import com.lessu.xieshi.set.SettingActivity;
import com.scetia.Pro.common.Util.Constants;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.module.meet.activity.MeetingListActivity;
import com.lessu.xieshi.module.mis.bean.MisGuideBean;
import com.lessu.xieshi.module.scan.ScanActivity;
import com.lessu.xieshi.Utils.UpdateAppUtil;
import com.scetia.Pro.network.bean.ExceptionHandle;

import java.util.ArrayList;

/**
 * 协会内部人员登录后进入的主菜单界面
 */
public class MisGuideActivity extends NavigationActivity {
    private LinearLayout ll_addparent;
    private FirstViewModel firstViewModel;
    final ArrayList<MisGuideBean> menuArray = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_misguideactivity;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        this.setTitle("内部管理");
        navigationBar.setLeftBarItem(null);
        BarButtonItem weatherItem = new BarButtonItem(this, R.drawable.duoyund);
        BarButtonItem scanItem = new BarButtonItem(this, R.drawable.icon_scan_white);
        weatherItem.setOnClickMethod(this, "openWeatherPage");
        scanItem.setOnClickMethod(this, "openScan");
        navigationBar.addRightBarItem(scanItem);
        navigationBar.setLeftBarItem(weatherItem);
        ll_addparent = findViewById(R.id.ll_addparent);
        MisGuideBean settingbean = new MisGuideBean(R.drawable.shezhimis, "系统设置", SettingActivity.class);
        addItemView(settingbean);
        login();
        //检查软件是否需要更新
        UpdateAppUtil.checkUpdateApp(this, false);
    }

    /**
     * 监听数据变化，更新UI界面
     */
    @Override
    protected void observerData() {
        firstViewModel = new ViewModelProvider(this, new FirstViewModelFactory(this.getApplication(), this))
                .get(FirstViewModel.class);
        firstViewModel.getMapLiveData().observe(this, map -> {
            String userPower = (String) map.get(LoginViewModel.TO_ACTIVITY);
            String shortUserPower = userPower.substring(16);
            initMenu(shortUserPower);
        });

        firstViewModel.getLoadState().observe(this, loadState -> {
            switch (loadState) {
                case LOADING:
                    LSAlert.showProgressHud(MisGuideActivity.this, getResources().getString(R.string.login_loading_text));
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    if (loadState.getCode() == 3000) {
                        LSAlert.showAlert(MisGuideActivity.this, "提示", loadState.getMessage() + "\n是否重新登录？"
                                , "确定", false, () -> SettingUtil.loginOut(this));
                    } else if (loadState.getCode() == ExceptionHandle.NETWORK_ERROR) {
                        LSAlert.showAlert(MisGuideActivity.this, "提示", loadState.getMessage(), "重试", "退出",false
                                , new LSAlert.AlertCallback() {
                                    @Override
                                    public void onConfirm() {
                                        firstViewModel.login(SPUtil.getSPConfig(Constants.User.KEY_USER_NAME, ""),
                                                SPUtil.getSPConfig(Constants.User.KEY_PASSWORD, ""));
                                    }

                                    @Override
                                    public void onCancel() {
                                        AppApplication.exit();
                                    }
                                });
                    } else {
                        LSAlert.showAlert(MisGuideActivity.this, "提示", loadState.getMessage());
                    }
                    break;
            }
        });
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
            firstViewModel.login(userName, password);
        } else {
            String userPower = SPUtil.getSPConfig(Constants.User.KEY_USER_POWER, "");
            if (!userPower.equals("")) {
                String shortUserPower = userPower.substring(16);
                initMenu(shortUserPower);
            }
        }
    }

    /**
     * 初始化菜单
     *
     * @param shortUserPower 权限
     */
    private void initMenu(String shortUserPower) {
        MisGuideBean huiyuanbean = new MisGuideBean(R.drawable.huiyuanxinxi, "会员信息查询", MisMemberSearchActivity.class);
        MisGuideBean zhenshubean = new MisGuideBean(R.drawable.zhengshuxinxi, "证书信息查询", MisCertificateSearchActivity.class);
        MisGuideBean pinguchaxunbean = new MisGuideBean(R.drawable.pingguchaxun, "评估信息查询", MisPingGuActivity.class);
        MisGuideBean xinxibean = new MisGuideBean(R.drawable.xinxitongzhi, "信息通知", MisNoticesActivity.class);
        MisGuideBean nianjiashenqinbean = new MisGuideBean(R.drawable.nianjiashenqing, "年假管理", MisAnnualLeaveManageActivity.class);
        //2018-10-16新增功能模块 “比对审批”
        MisGuideBean comparisonApproval = new MisGuideBean(R.drawable.shujubidui, "比对审批", MisComparisonAprovalActivity.class);
        MisGuideBean meetingManager = new MisGuideBean(R.drawable.icon_mis_meeting, "会议安排", MeetingListActivity.class);
        MisGuideBean sealManager = new MisGuideBean(R.drawable.ic_mis_matter_approve, "事项审批", SealManageListActivity.class);
        char s1 = shortUserPower.charAt(0);
        char s2 = shortUserPower.charAt(1);
        char s3 = shortUserPower.charAt(2);
        char s4 = shortUserPower.charAt(3);
        char s5 = shortUserPower.charAt(4);
        char s6 = '0';
        char s7 = '0';
        if (shortUserPower.length() == 7) {
            s6 = shortUserPower.charAt(0);
            s1 = shortUserPower.charAt(1);
            s2 = shortUserPower.charAt(2);
            s3 = shortUserPower.charAt(3);
            s4 = shortUserPower.charAt(4);
            s5 = shortUserPower.charAt(5);
            s7 = shortUserPower.charAt(6);
        }
        if (s1 == '1') {
            menuArray.add(huiyuanbean);
        }
        if (s2 == '1') {
            menuArray.add(zhenshubean);
        }
        if (s3 == '1') {
            menuArray.add(pinguchaxunbean);
        }
        if (s4 == '1') {
            menuArray.add(xinxibean);
        }
        if (s5 == '1') {
            menuArray.add(nianjiashenqinbean);
        }
        if (s6 == '1') {
            //新增的权限“比对审批”
            menuArray.add(comparisonApproval);
        }
        if(s7=='1'){
            menuArray.add(sealManager);
        }
        //会议安排暂时隐藏
        menuArray.add(meetingManager);

        for (int i = menuArray.size() - 1; i >= 0; i--) {
            addItemView(menuArray.get(i));
        }
        //界面初始化完成，开启自动登录
        SPUtil.setSPConfig(SPUtil.AUTO_LOGIN_KEY, true);
    }

    /**
     * 添加菜单选项
     *
     * @param misguidebean 菜单选项
     */
    private void addItemView(MisGuideBean misguidebean) {
        View view = View.inflate(this, R.layout.misaddguide_item, null);
        LinearLayout ll_additem = view.findViewById(R.id.ll_additem);
        ImageView iv_additem = view.findViewById(R.id.iv_additem);
        TextView tv_additem = view.findViewById(R.id.tv_additem);
        iv_additem.setImageResource(misguidebean.pic);
        tv_additem.setText(misguidebean.text);
        ll_additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (misguidebean.clazz.getName().contains("MeetingListActivity")) {
                    Intent meetingIntent = new Intent(MisGuideActivity.this, misguidebean.clazz);
                    meetingIntent.putExtra("type_user", 0);
                    startActivity(meetingIntent);
                } else {
                    startOtherActivity(misguidebean.clazz);
                }
            }
        });
        ll_addparent.addView(view, 0);
    }

    @PermissionNeed(value = {Manifest.permission.ACCESS_FINE_LOCATION}, requestCode = 1)
    public void openWeatherPage() {
        startOtherActivity(WeatherDetailActivity.class);
    }

    @PermissionNeed(value = {Manifest.permission.CAMERA}, requestCode = 0)
    public void openScan() {
        startActivity(new Intent((MisGuideActivity.this), ScanActivity.class));
    }

    /**
     * 如果用户永久拒绝了，就要打开
     */
    @PermissionDenied
    private void shouldOpenLocation(int requestCode) {
        if (requestCode == 1) {
            LSAlert.showDialog(this, "提示", "天气功能需要定位权限，请在系统设置中打开权限！", "去设置", "不设置",
                    new LSAlert.DialogCallback() {
                        @Override
                        public void onConfirm() {
                            PermissionSettingPage.start(MisGuideActivity.this, true);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
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
