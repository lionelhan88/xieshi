package com.lessu.xieshi.set;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.google.gson.JsonElement;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.uikit.views.LSAlert.DialogCallback;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.Common;
import com.lessu.xieshi.Utils.UpdateAppUtil;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.lessu.xieshi.module.login.LoginActivity;
import com.lessu.xieshi.module.scan.ScanActivity;
import com.scetia.Pro.common.Util.SPUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends XieShiSlidingMenuActivity {
    @BindView(R.id.serviceTextView)
    TextView serviceTextView;
    @BindView(R.id.setting_app_version_name)
    TextView settingAppVersionName;
    private HashMap<String, String> serviceTitleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        ButterKnife.bind(this);
        setTitle("设置");
        String serviceString = SPUtil.getSPLSUtil("service","");
        serviceTitleMap = new HashMap<>();
        serviceTitleMap.put("telecom", "电信服务器");
        serviceTitleMap.put("unicom", "联通服务器");
        if (serviceTitleMap.containsKey(serviceString)) {
            serviceTextView.setText(serviceTitleMap.get(serviceString));
        } else {
            serviceTextView.setText("当前服务器错误");
        }
        settingAppVersionName.setText(Common.getVersionName(this));

    }

    public void menuButtonDidClick() {
        menu.toggle();
    }

    @OnClick(R.id.jiechuButton)
    public void jiechuButtonDidClick() {
        //解除绑定的操作
        String description = "手机号:" + SPUtil.getSPLSUtil("PhoneNumber","") + "\n" + "用户名:" +
                SPUtil.getSPConfig(Common.USERNAME, "");
        LSAlert.showDialog(SettingActivity.this, "解除账号绑定", description, "确定", "取消", new DialogCallback() {
            @Override
            public void onConfirm() {
                HashMap<String, Object> params = new HashMap<>();
                params.put("UserName", SPUtil.getSPConfig(Common.USERNAME, ""));
                params.put("PhoneNumber", SPUtil.getSPLSUtil("PhoneNumber",""));
                params.put("PassWord", SPUtil.getSPConfig(Common.PASSWORD, ""));
                System.out.println(params);
                EasyAPI.apiConnectionAsync(SettingActivity.this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/User_UnBind "), params, new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        SPUtil.setSPConfig(Common.USERPOWER, "");
                        SPUtil.setSPLSUtil("Token", "");
                        Intent intent = new Intent();
                        intent.setClass(SettingActivity.this, LoginActivity.class);
                        intent.putExtra("jiebang", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @OnClick(R.id.passwordButton)
    public void passwordButtonDidClick() {
        Intent intent = new Intent(SettingActivity.this, PasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.serviceButton)
    public void serviceButtonDidClick() {
        Intent intent = new Intent(SettingActivity.this, ServiceActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.bt_set_login_out)
    public void resetLogin() {
        LSAlert.showDialog(this, "提示", "确定要重新登陆吗？", "确定", "取消",
                new DialogCallback() {
                    @Override
                    public void onConfirm() {
                        loginOut();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    @OnClick(R.id.updateButton)
    public void updateButtonDidClick() {
        UpdateAppUtil.checkUpdateApp(this, true);
    }

    @OnClick(R.id.scanLogin)
    public void scanLogin() {
        if (SPUtil.getSPConfig(Common.USERID, "").equals("")) {
            //如果userid是"",则要提示用户重新登录才能获取到userId
            LSAlert.showDialog(SettingActivity.this, "提示", "使用此功能需要重新登录\n是否现在登录？",
                    "确定", "取消", new DialogCallback() {
                        @Override
                        public void onConfirm() {
                            //退出登录
                            loginOut();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
        openScanPage();
    }

    @PermissionNeed(Manifest.permission.CAMERA)
    private void openScanPage() {
        startOtherActivity(ScanActivity.class);
    }

    /**
     * 如果用户永久拒绝了，就要打开
     */
    @PermissionDenied
    private void shouldOpenScan(int requestCode) {
        LSAlert.showDialog(this, "提示", "扫码登陆需要授予相机权限，请在系统设置中打开权限！", "去设置", "不设置",
                new DialogCallback() {
                    @Override
                    public void onConfirm() {
                        PermissionSettingPage.start(SettingActivity.this, true);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    /**
     * 退出登录
     */
    private void loginOut() {
        Intent intentLoginOut = new Intent();
        intentLoginOut.setClass(SettingActivity.this, LoginActivity.class);
        intentLoginOut.putExtra("exit", true);
        intentLoginOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentLoginOut);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String service = SPUtil.getSPLSUtil("service","");
            if (serviceTitleMap.containsKey(service)) {
                serviceTextView.setText(serviceTitleMap.get(service));
            } else {
                serviceTextView.setText("当前服务器错误");
            }
        }
    }
}
