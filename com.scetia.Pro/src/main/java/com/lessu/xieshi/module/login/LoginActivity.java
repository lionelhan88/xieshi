package com.lessu.xieshi.module.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.SettingUtil;
import com.scetia.Pro.common.Util.Constants;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.AppApplication;
import com.scetia.Pro.common.Util.SPUtil;
import com.lessu.xieshi.module.login.viewmodel.LoginViewModel;
import com.lessu.xieshi.module.mis.activities.MisGuideActivity;
import com.lessu.xieshi.Utils.UpdateAppUtil;

import butterknife.BindView;
import butterknife.OnClick;


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
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    protected void initView() {
        navigationBar.setVisibility(View.GONE);
    }

    /**
     * 申请所有需要的权限
     */
    @PermissionNeed({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA,
            Manifest.permission.REQUEST_INSTALL_PACKAGES})
    private void initRequestAllPermission() {
        //申请权限
        UpdateAppUtil.checkUpdateApp(this, false);
    }


    @PermissionDenied
    private void readPhoneStateDenied(int requestCode) {
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            LSAlert.showAlert(this, "提示", "此应用需要授权电话管理权限，请授权！", "授权", () -> {
                //TODO:跳转到系统设置页面去授予权限
                PermissionSettingPage.start(LoginActivity.this, true);
            });
        }
    }

    @Override
    protected View getTitleBar() {
        return tvLoginVersion;
    }


    @Override
    protected void observerData() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getLoadState().observe(this, loadState -> {
            switch (loadState) {
                case LOADING:
                    LSAlert.showProgressHud(LoginActivity.this,  getResources().getString(R.string.login_loading_text));
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(LoginActivity.this, "提示", loadState.getMessage());
                    break;
            }
        });

        loginViewModel.getMapLiveData().observe(this, toActivityMap -> {
            Object o = toActivityMap.get(LoginViewModel.TO_ACTIVITY);
            if (o instanceof Bundle) {
                //是第一次登陆，跳转到手机号验证页面
                Bundle bundle = (Bundle) o;
                Intent intent = new Intent(LoginActivity.this, ValidateActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0x01);
            } else {
                //不是第一次登陆，直接跳转页面
                checkUserPower((String) o);
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        //显示出版本号
        tvLoginVersion.setText(SettingUtil.getVersionName(this));
        //拿到本地存储的权限码
        String userPower = SPUtil.getSPConfig(Constants.User.KEY_USER_POWER, "");
        Intent intent = getIntent();
        boolean isExit = intent.getBooleanExtra(Constants.Setting.EXIT, false);
        //如果之前已经登录，打开程序直接进入主界面
        if (!TextUtils.isEmpty(userPower) && !isExit) {
            checkUserPower(userPower);
        }else{
            initRequestAllPermission();
        }
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
     * @param name   用户名
     * @param password 密码
     */
    @PermissionNeed(value = Manifest.permission.READ_PHONE_STATE, requestCode = REQUEST_READ_PHONE_STATE)
    private void login(final String name, final String password) {
        loginViewModel.login(name, password);
    }

    /**
     * 检查权限，根据不同的权限跳转不同的页面
     *
     * @param userPower
     */
    private void checkUserPower(String userPower) {
        if (userPower.isEmpty()) {
            LSAlert.showAlert(this, "无角色权限！");
            return;
        }
        String shortUserPower;
        //目前测试 建设用砂功能，暂定权限为“1”
        if (userPower.equals("1")) {
            Intent intent = new Intent(this, FirstActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        //新增的权限“比对审批”多一位 2018-10-19
        if (userPower.length() == 15) {
            shortUserPower = userPower.substring(9, 15);
        } else if (userPower.length() < 15) {
            shortUserPower = userPower.substring(9, 14);
        } else {
            //新版本新加了权限
            shortUserPower = userPower.substring(16);
        }
        Intent intent;
        if (shortUserPower.equals("00000") || shortUserPower.equals("000000")) {
            intent = new Intent(this, FirstActivity.class);
        } else {
            intent = new Intent(this, MisGuideActivity.class);
        }
        startActivity(intent);
        finish();
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
