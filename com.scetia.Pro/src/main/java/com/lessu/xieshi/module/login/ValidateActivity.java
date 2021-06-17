package com.lessu.xieshi.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.ToastUtil;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.SPUtil;
import com.lessu.xieshi.module.login.viewmodel.ValidateViewModel;

import butterknife.BindView;
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
    private String deviceId;
    private CountDownTimer countDownTimer;
    private ValidateViewModel viewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.validate_activity;
    }

    @Override
    protected void initView() {
        setTitle("绑定");
        String phoneNumber = SPUtil.getSPLSUtil(Constants.User.XS_PHONE_NUMBER,"");
        if (!TextUtils.isEmpty(phoneNumber)) {
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

    @Override
    protected void observerData() {
        viewModel = new ViewModelProvider(this).get(ValidateViewModel.class);
        viewModel.getLoadState().observe(this,loadState -> {
            switch (loadState){
                case LOADING:
                    LSAlert.showProgressHud(ValidateActivity.this,loadState.getMessage());
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    ToastUtil.showShort(loadState.getMessage());
                    break;
            }
        });

        //获取验证码成功
        viewModel.getValidateCodeLiveData().observe(this, validateCodeBean -> {
           SPUtil.setSPLSUtil(Constants.User.XS_TOKEN, validateCodeBean.getToken());
            SPUtil.setSPLSUtil(Constants.User.XS_PHONE_NUMBER, validateCodeBean.getPhoneNumber());
            //开始进入验证码倒计时时间
            getValidateButton.setBackgroundResource(R.drawable.yanzhengma);
            getValidateButton.setEnabled(false);
            countDownTimer.start();
        });

        //绑定成功
        viewModel.getLoginUserLiveData().observe(this, loginUserBean -> {
            Intent intent = new Intent();
            intent.putExtra("userName", userName);
            intent.putExtra("password", passWord);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @OnTextChanged(R.id.phoneNumEditText)
    public void phoneNumEditTextChanged() {
        SPUtil.setSPLSUtil("PhoneNumber", phoneNumEditText.getText().toString());
    }

    @OnClick(R.id.getValidateButton)
    public void getValidateButtonDidPress() {
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("UserName");
        passWord = bundle.getString("PassWord");
        deviceId = bundle.getString("DeviceId");
        String PhoneNumber = phoneNumEditText.getText().toString();
        viewModel.getPhoneCheckCode(userName,passWord,deviceId,PhoneNumber);
    }

    @OnClick(R.id.validateButton)
    public void validateButtonDidPress() {
        viewModel.validatePhone(validateCodeEditText.getText().toString());
    }

    @Override
    protected void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();

    }
}
