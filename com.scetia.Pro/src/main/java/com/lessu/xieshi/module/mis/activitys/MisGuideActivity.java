package com.lessu.xieshi.module.mis.activitys;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.R;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.common.Util.SPUtil;
import com.lessu.xieshi.module.login.bean.LoginUserBean;
import com.lessu.xieshi.module.login.viewmodel.FirstViewModel;
import com.lessu.xieshi.module.login.viewmodel.FirstViewModelFactory;
import com.lessu.xieshi.module.weather.WeatherDetailActivity;
import com.lessu.xieshi.set.SettingActivity;
import com.scetia.Pro.common.Util.Common;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.module.login.LoginActivity;
import com.lessu.xieshi.module.meet.activity.MeetingListActivity;
import com.lessu.xieshi.module.mis.bean.MisGuideBean;
import com.lessu.xieshi.module.scan.ScanActivity;
import com.lessu.xieshi.Utils.UpdateAppUtil;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;

import java.util.ArrayList;

/**
 * 协会内部人员登录后进入的主菜单界面
 */
public class MisGuideActivity extends NavigationActivity {
    private LinearLayout ll_addparent;
    private BarButtonItem handleButtonItem2;
    private FirstViewModel firstViewModel;
    final ArrayList<MisGuideBean> menuArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misguideactivity);
        this.setTitle("内部管理");
        BarButtonItem handleButtonItem = new BarButtonItem(this, R.drawable.back);
        handleButtonItem.setOnClickMethod(this, "houtui");
        navigationBar.setLeftBarItem(null);

        BarButtonItem handleButtonItem1 = new BarButtonItem(this, R.drawable.duoyund);
        handleButtonItem2 = new BarButtonItem(this, R.drawable.icon_scan_white);
        handleButtonItem1.setOnClickMethod(this, "tianqi");
        navigationBar.addRightBarItem(handleButtonItem2);
        navigationBar.setLeftBarItem(handleButtonItem1);
        initDataListener();
        initView();
        login();
        //检查软件是否需要更新
        UpdateAppUtil.checkUpdateApp(this, false);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        ll_addparent = findViewById(R.id.ll_addparent);
        handleButtonItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPUtil.getSPConfig(Common.USERID, "").equals("")) {
                    LSAlert.showDialog(MisGuideActivity.this, "提示", "使用此功能需要重新登录\n是否现在登录？",
                            "确定", "取消", new LSAlert.DialogCallback() {
                                @Override
                                public void onConfirm() {
                                    //退出登录
                                    loginOut();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                } else {
                    startActivity(new Intent((MisGuideActivity.this), ScanActivity.class));
                }
            }
        });
        MisGuideBean settingbean = new MisGuideBean(R.drawable.shezhimis, "系统设置", SettingActivity.class);
        addItemView(settingbean);
    }

    /**
     * 监听数据变化，更新UI界面
     */
    private void initDataListener() {
        firstViewModel = new ViewModelProvider(this, new FirstViewModelFactory(this.getApplication(), this))
                .get(FirstViewModel.class);

        firstViewModel.getUserBeanData().observe(this, new Observer<LoginUserBean>() {
            @Override
            public void onChanged(LoginUserBean userBean) {
                String userPower = userBean.getUserPower();
                String userId = userBean.getUserId();
                String MemberInfoStr = userBean.getMemberInfoStr();
                String token = userBean.getToken();
                //保存一些用户信息
                SPUtil.setSPConfig(Common.USERPOWER, userPower);
                SPUtil.setSPConfig(Common.USERID, userId);
                SPUtil.setSPConfig(Common.MEMBERINFOSTR, MemberInfoStr);
                SPUtil.setSPLSUtil("Token", token);
                String shortUserPower = userPower.substring(16);
                initMenu(shortUserPower);
            }
        });

        firstViewModel.getLoadState().observe(this, new Observer<LoadState>() {
            @Override
            public void onChanged(LoadState loadState) {
                if (loadState == LoadState.LOADING) {
                    LSAlert.showProgressHud(MisGuideActivity.this, "正在登陆...");
                } else {
                    ll_addparent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //加载弹窗消失的太快，会造成闪一下的效果，给用户体验不好，所以使用延迟消失
                            LSAlert.dismissProgressHud();
                        }
                    }, 300);
                }
            }
        });
        firstViewModel.getThrowable().observe(this, new Observer<ExceptionHandle.ResponseThrowable>() {
            @Override
            public void onChanged(ExceptionHandle.ResponseThrowable throwable) {
                if (throwable.code == 3000) {
                    LSAlert.showAlert(MisGuideActivity.this, "提示", throwable.message + "\n是否重新登录？"
                            , "确定", false, () -> loginOut());
                } else {
                    LSAlert.showAlert(MisGuideActivity.this, "提示", throwable.message);
                }
            }
        });
    }

    /**
     * 自动登陆
     */
    private void login() {
        String userName = SPUtil.getSPConfig(Common.USERNAME, "");
        String password = SPUtil.getSPConfig(Common.PASSWORD, "");
        String deviceId = SPUtil.getSPConfig(Common.DEVICEID, "");
        //每次进入主界面要重新登陆获取数据，可能更新权限
        if (SPUtil.getSPConfig(SPUtil.AUTO_LOGIN_KEY, false)) {
            //如果开启自动登录，进入页面需要自动登录
            firstViewModel.login(userName, password, deviceId);
            LogUtil.showLogE("misGuid页面自动登录了");
        } else {
            String userPower = SPUtil.getSPConfig(Common.USERPOWER, "");
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
        char s1 = shortUserPower.charAt(0);
        char s2 = shortUserPower.charAt(1);
        char s3 = shortUserPower.charAt(2);
        char s4 = shortUserPower.charAt(3);
        char s5 = shortUserPower.charAt(4);
        char s6 = '0';
        if (shortUserPower.length() == 6) {
            s6 = shortUserPower.charAt(0);
            s1 = shortUserPower.charAt(1);
            s2 = shortUserPower.charAt(2);
            s3 = shortUserPower.charAt(3);
            s4 = shortUserPower.charAt(4);
            s5 = shortUserPower.charAt(5);
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

    /**
     * 退出登录
     */
    private void loginOut() {
        Intent intentTUICHU = new Intent();
        intentTUICHU.setClass(MisGuideActivity.this, LoginActivity.class);
        intentTUICHU.putExtra("exit", true);
        intentTUICHU.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentTUICHU);
        finish();
    }

    @PermissionNeed(value = {Manifest.permission.ACCESS_FINE_LOCATION}, requestCode = 1)
    public void tianqi() {
        startOtherActivity(WeatherDetailActivity.class);
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
                ToastUtil.showShort("再次点击退出程序");
                return true;
            } else {
                AppApplication.exit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
