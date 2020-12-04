package com.lessu.xieshi.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.foundation.LSUtil;
import com.lessu.foundation.ValidateHelper;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ValidateActivity extends NavigationActivity {
    @BindView(R.id.phoneNumEditText)
    EditText phoneNumEditText;
    @BindView(R.id.validateCodeEditText)
    EditText validateCodeEditText;
    @BindView(R.id.getValidateButton)
    com.lessu.uikit.Buttons.Button getValidateButton;
    @BindView(R.id.validateButton)
    com.lessu.uikit.Buttons.Button validateButton;
    private String userName;
    private String passWord;
    private String token1;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validate_activity);
        setTitle("绑定");
        ButterKnife.bind(this);
        if (LSUtil.valueStatic("PhoneNumber") != null) {
            String phoneNumber = LSUtil.valueStatic("PhoneNumber");
			phoneNumEditText.setText(phoneNumber);
        }
        //验证码的倒计时60s
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int currentTime = (int) (millisUntilFinished / 1000);
                getValidateButton.setText("重新获取验证码(" + currentTime + "s)");
            }

            @Override
            public void onFinish() {
                getValidateButton.setEnabled(true);
                getValidateButton.setBackgroundResource(R.drawable.huoquyanzhengma1);
                getValidateButton.setText("重新获取验证码");
            }
        };
    }

    @OnTextChanged(R.id.phoneNumEditText)
    public void phoneNumEditTextChanged() {
        LSUtil.setValueStatic("PhoneNumber", phoneNumEditText.getText().toString());
    }

    @OnClick(R.id.getValidateButton)
    public void getValidateButtonDidPress() {
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("UserName");
        passWord = bundle.getString("PassWord");
        String deviceId = bundle.getString("DeviceId");
        String PhoneNumber = phoneNumEditText.getText().toString();
        if (!ValidateHelper.validatePhone(PhoneNumber)) {
            LSAlert.showAlert(ValidateActivity.this, "手机号输入有误！请重新输入");
            return;
        }
        JsonObject paramJson = new JsonObject();
        paramJson.addProperty("UserName", userName);
        paramJson.addProperty("PassWord", passWord);
        paramJson.addProperty("DeviceId", deviceId);
        paramJson.addProperty("PhoneNumber", PhoneNumber);
        HashMap<String, Object> params = new HashMap<>();
        params.put("UserName", userName);
        params.put("PassWord", passWord);
        params.put("DeviceId", deviceId);
        params.put("PhoneNumber", PhoneNumber);
        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/User_BindStart"), params, new EasyAPI.ApiFastSuccessCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                try {
                    System.out.println(result);
                    JsonObject resultJson = result.getAsJsonObject();
                    if (!resultJson.get("Success").getAsBoolean()) {
                        //如果账号之前帮的手机号与当前输入的手机号不符合则提示
                        LSAlert.showAlert(ValidateActivity.this, "当前账户及设备已绑定或正进行绑定中，请不要重复绑定");
                        return;
                    }
                    token1 = resultJson.get("Data").getAsJsonObject().get("Token").getAsString();
                    System.out.println(token1);
                    LSUtil.setValueStatic("Token", token1);
                    String PhoneNumber = resultJson.get("Data").getAsJsonObject().get("PhoneNumber").getAsString();
                    LSUtil.setValueStatic("PhoneNumber", PhoneNumber);
                    //开始进入验证码倒计时时间
                    getValidateButton.setBackgroundResource(R.drawable.yanzhengma);
                    getValidateButton.setEnabled(false);
                    countDownTimer.start();
                } catch (Exception e) {
                    LSAlert.showAlert(ValidateActivity.this, e.getMessage());
                }

            }
        });
    }

    @OnClick(R.id.validateButton)
    public void validateButtonDidPress() {
        String PhoneNumber = phoneNumEditText.getText().toString();
        if (!ValidateHelper.validatePhone(PhoneNumber)) {
            LSAlert.showAlert(ValidateActivity.this, "手机号输入有误！请重新输入");
            return;
        }
        if (validateCodeEditText.getText().toString().isEmpty()) {
            LSAlert.showAlert(ValidateActivity.this, "请输入验证码");
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Token", LSUtil.valueStatic("Token"));
        params.put("CheckCode", validateCodeEditText.getText().toString());

        EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/User_BindEnd"), params, new EasyAPI.ApiFastSuccessCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                boolean success = result.getAsJsonObject().get("Success").getAsBoolean();
                //2018-10-19日修改
                if (success) {
                    //如果验证手机成功，则到登录页面重新登录
                    Intent intent = new Intent();
                    intent.putExtra("userName", userName);
                    intent.putExtra("password", passWord);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    LSAlert.showAlert(ValidateActivity.this, "验证失败，请重新验证");
                }
            }
        });
    }
}
