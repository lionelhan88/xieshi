package com.lessu.xieshi.set;

import android.Manifest;
import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.google.gson.JsonElement;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.uikit.views.LSAlert.DialogCallback;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.SettingUtil;
import com.lessu.xieshi.utils.ToastUtil;
import com.scetia.Pro.common.Util.Constants;
import com.lessu.xieshi.utils.UpdateAppUtil;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.lessu.xieshi.module.scan.ScanActivity;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.network.ConstantApi;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends XieShiSlidingMenuActivity {
    @BindView(R.id.serviceTextView)
    TextView serviceTextView;
    @BindView(R.id.setting_app_version_name)
    TextView settingAppVersionName;

    @Override
    protected int getLayoutId() {
        return R.layout.setting_activity;
    }

    @Override
    protected void initView() {
        setTitle("设置");
        settingAppVersionName.setText(SettingUtil.getVersionName(this));
        serviceTextView.setText(SPUtil.GET_SERVICE_API().equals(ConstantApi.XS_TELECOM_BASE_URL) ? "电信服务器" : "联通服务器");
    }

    public void menuButtonDidClick() {
        menu.toggle();
    }

    @OnClick(R.id.jiechuButton)
    public void unBindButtonDidClick() {
        //解除绑定的操作
        String description = "手机号:" + SPUtil.getSPLSUtil(Constants.User.XS_PHONE_NUMBER, "") + "\n" + "用户名:" +
                SPUtil.getSPConfig(Constants.User.KEY_USER_NAME, "");
        LSAlert.showDialog(SettingActivity.this, "解除账号绑定", description, "确定", "取消", () -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("UserName", SPUtil.getSPConfig(Constants.User.KEY_USER_NAME, ""));
            params.put("PhoneNumber", SPUtil.getSPLSUtil(Constants.User.XS_PHONE_NUMBER, ""));
            params.put("PassWord", SPUtil.getSPConfig(Constants.User.KEY_PASSWORD, ""));
            EasyAPI.apiConnectionAsync(SettingActivity.this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/User_UnBind "), params, new EasyAPI.ApiFastSuccessCallBack() {
                @Override
                public void onSuccessJson(JsonElement result) {
                    boolean success = result.getAsJsonObject().get("Success").getAsBoolean();
                    if(!success){
                        String message = result.getAsJsonObject().get("Message").getAsString();
                        ToastUtil.showShort(message);
                        return;
                    }
                    SPUtil.clearData();
                    SettingUtil.loginOut(SettingActivity.this);
                }
            });
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
    AlertDialog alertDialog;
    @OnClick(R.id.bt_set_login_out)
    public void resetLogin() {
      alertDialog =  LSAlert.showDialog(this, "提示", "确定要重新登录吗？", "确定", "取消",
              new DialogCallback() {
                  @Override
                  public void onConfirm() {
                      SettingUtil.loginOut(SettingActivity.this);
                  }
              });
    }

    @OnClick(R.id.updateButton)
    public void updateButtonDidClick() {
        UpdateAppUtil.checkUpdateApp(this, true);
    }

    @OnClick(R.id.scanLogin)
    @PermissionNeed(Manifest.permission.CAMERA)
    public void scanLogin() {
        startOtherActivity(ScanActivity.class);
    }

    /**
     * 如果用户永久拒绝了，就要打开
     */
    @PermissionDenied
    private void shouldOpenScan(int requestCode) {
        LSAlert.showDialog(this, "提示", "扫码登陆需要授予相机权限，请在系统设置中打开权限！", "去设置", "不设置",
                () -> PermissionSettingPage.start(SettingActivity.this, true));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            serviceTextView.setText(SPUtil.GET_SERVICE_API().equals(ConstantApi.XS_TELECOM_BASE_URL) ? "电信服务器" : "联通服务器");
        }
    }

    @Override
    protected void onDestroy() {
        if(alertDialog!=null) {
            alertDialog.dismiss();
        }
        super.onDestroy();
    }
}
