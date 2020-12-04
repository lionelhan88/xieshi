package com.lessu.xieshi.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.AppApplication;
import com.lessu.xieshi.bean.LoadState;
import com.lessu.xieshi.http.ExceptionHandle;
import com.lessu.xieshi.login.bean.LoginUserBean;
import com.lessu.xieshi.login.viewmodel.LoginViewModel;
import com.lessu.xieshi.module.mis.activitys.MisGuideActivity;
import com.lessu.xieshi.Utils.UpdateAppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 当前页面在android9.0后要开启硬件加速，否则输入密码时不会实时显示
 */
public class LoginActivity extends NavigationActivity {
    private static final int REQUEST_READ_PHONE_STATE = 2;
    @BindView(R.id.tv_login_version)
    TextView tvLoginVersion;
    @BindView(R.id.userNameEditText)
    EditText userNameEditText;
    @BindView(R.id.passWordEditText)
    EditText passWordEditText;
    private LoginViewModel loginViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        navigationBar.setVisibility(View.GONE);
        ImmersionBar.with(this).titleBar(tvLoginVersion)
                .navigationBarColor(R.color.light_gray)
                .navigationBarDarkIcon(true)
                .init();
        initData();
        initDataListener();
        initRequestAllPermission();
    }
    /**
     * 申请所有需要的权限
     */
    @PermissionNeed({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA,
    Manifest.permission.REQUEST_INSTALL_PACKAGES})
    private void initRequestAllPermission(){
        //申请权限
        UpdateAppUtil.checkUpdateApp(this,false);
    }


    @PermissionDenied
    private void readPhoneStateDenied(int requestCode){
        if(requestCode==REQUEST_READ_PHONE_STATE){
            LSAlert.showAlert(this, "提示", "此应用需要授权电话管理权限，请授权！", "授权", new LSAlert.AlertCallback() {
                @Override
                public void onConfirm() {
                    //TODO:跳转到系统设置页面去授予权限
                    PermissionSettingPage.start(LoginActivity.this,true);
                }
            });
        }
    }

    @Override
    protected void initImmersionBar() {
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //显示出版本号
        String versionName = Common.getVersionName(this);
        tvLoginVersion.setText(versionName);
        //拿到本地存储的权限
        String userPower = Shref.getString(this, Common.USERPOWER, "");
        Intent intent = getIntent();
        boolean isExit = intent.getBooleanExtra("exit", false);
        boolean unBind = intent.getBooleanExtra("jiebang", false);
        //如果之前已经登录，打开程序直接进入主界面
        if (userPower != null && !userPower.equals("") && !isExit && !unBind) {
            checkUserPower(userPower);
        }
    }

    /**
     * 监听数据变化，更新UI界面
    */
    private void initDataListener(){
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getThrowable().observe(this, new Observer<ExceptionHandle.ResponseThrowable>() {
            @Override
            public void onChanged(ExceptionHandle.ResponseThrowable throwable) {
                LSAlert.showAlert(LoginActivity.this,"提示",throwable.message);
            }
        });
        loginViewModel.getUserBeanData().observe(this, new Observer<LoginUserBean>() {
            @Override
            public void onChanged(LoginUserBean userBean) {
                dispatchActivity(userBean);
            }
        });
        loginViewModel.getLoadState().observe(this, new Observer<LoadState>() {
            @Override
            public void onChanged(LoadState loadState) {
                switch (loadState){
                    case LOADING:
                        LSAlert.showProgressHud(LoginActivity.this,"正在登陆...");
                        break;
                    case FAILURE:
                    case SUCCESS:
                        LSAlert.dismissProgressHud();
                        break;
                }
            }
        });
    }
    @OnClick(R.id.loginButton)
    public void loginButtonDidPress() {
        //登陆接口访问
        final String userName = userNameEditText.getText().toString();
        final String password = passWordEditText.getText().toString();
        login(userName, password);
    }

    /**
     * 执行登陆请求
     * @param name 用户名
     * @param password 密码
     */
    @PermissionNeed(value = Manifest.permission.READ_PHONE_STATE,requestCode = REQUEST_READ_PHONE_STATE)
    private void login(final String name, final String password) {
        final String deviceId = getDeviceId();
        loginViewModel.login(name,password,deviceId);
    }

    /**
     * 解析用户 权限，跳转对应的菜单界面
     */
    private void dispatchActivity(LoginUserBean loginUser){
        String userPower = loginUser.getUserPower();
        String userName = loginUser.getUserName();
        String token = loginUser.getToken();
        String PhoneNumber = loginUser.getPhoneNumber();
        String userId = loginUser.getUserId();
        String MemberInfoStr = loginUser.getMemberInfoStr();
        boolean isFirstLogin = loginUser.isIsFirstLogin();
        String deviceId = getDeviceId();
        String password = passWordEditText.getText().toString();
        LSUtil.setValueStatic("PhoneNumber", PhoneNumber);
        LSUtil.setValueStatic("UserName", userName);

        //TODO:判断是否是第一次登陆，如果是第一次登陆就进入手机验证界面
        if (isFirstLogin) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            bundle.putString("UserName", userName);
            bundle.putString("PassWord", password);
            bundle.putString("DeviceId", deviceId);
            bundle.putString("UserPower", userPower);
            Intent intent = new Intent(LoginActivity.this, ValidateActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0x01);
        } else {
            LSUtil.setValueStatic("Token", token);
            Shref.setString(LoginActivity.this, Common.USERNAME, userName);
            Shref.setString(LoginActivity.this, Common.PASSWORD, password);
            Shref.setString(LoginActivity.this, Common.DEVICEID, deviceId);
            Shref.setString(LoginActivity.this, Common.USERPOWER, userPower);
            Shref.setString(LoginActivity.this, Common.USERID, userId);
            Shref.setString(LoginActivity.this, Common.MEMBERINFOSTR, MemberInfoStr);
            //TODO:去跳转界面
            checkUserPower(userPower);
            //这里将自动登录取消，避免用户从登录页面进入后，再次在主菜单界面登录一次
            Shref.setBoolean(this,Shref.AUTO_LOGIN_KEY,false);
        }
    }

    /**
     * 检查权限，根据不同的权限跳转不同的页面
     * @param userPower
     */
    private void checkUserPower(String userPower) {
        LogUtil.showLogD("用户权限===" + userPower);
        if (userPower.equals("")) {
            LSAlert.showAlert(this, "无角色权限！");
            return;
        }
        //新增的权限“比对审批”多一位 2018-10-19
        String shortUserPower;
        if (userPower.length() == 15) {
            shortUserPower = userPower.substring(9, 15);
        } else if (userPower.length() < 15) {
            shortUserPower = userPower.substring(9, 14);
        } else {
            //新版本新加了权限
            shortUserPower = userPower.substring(16);
        }
        final String userName = Shref.getString(this, Common.USERNAME, null);
        Intent intent;
        if (shortUserPower.equals("00000") || shortUserPower.equals("000000")) {
            intent = new Intent(this, FirstActivity.class);
            String unMisPower = userPower.length()<15 ? userPower.substring(userPower.length()):userPower.substring(0, 15);
            intent.putExtra("userpower", unMisPower);
        } else {
            intent = new Intent(this, MisGuideActivity.class);
            intent.putExtra("shortuserpower", shortUserPower);
        }
        intent.putExtra("username", userName);
        startActivity(intent);
        finish();
    }

    /**
     * 获取设备的uid号
     */
    private String getDeviceId(){
        //获取存在本地的deviceID
        String deviceId = Shref.getString(this, Common.DEVICEID, "");
        /*如果本地已经存在deviceID，则使用本地的deviceID*/
        if (deviceId.equals("")) {
           //如果没有取到本地存储的device_id,就获取系统的IEMI
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //android9以后获取不到IEMI的编码了，可能会发出异常
            try {
                deviceId = telephonyManager.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(deviceId==null||deviceId.equals("")){
                deviceId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return deviceId;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x01) {
            if (RESULT_OK == resultCode) {
                String userName1 = data.getStringExtra("userName");
                String password1 = data.getStringExtra("password");
                login(userName1, password1);
            }
        }
    }
    private long time=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-time>2000){
                time = System.currentTimeMillis();
                ToastUtil.showShort("再次点击退出程序");
                return true;
            }else{
                AppApplication.exit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
