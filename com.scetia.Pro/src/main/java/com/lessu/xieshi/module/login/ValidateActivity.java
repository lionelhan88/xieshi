package com.lessu.xieshi.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.EditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.scetia.Pro.baseapp.uitls.LoadMoreState;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.common.Util.SPUtil;
import com.lessu.xieshi.module.login.bean.LoginUserBean;
import com.lessu.xieshi.module.login.bean.ValidateCodeBean;
import com.lessu.xieshi.module.login.viewmodel.ValidateViewModel;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;

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
    private String deviceId;
    private CountDownTimer countDownTimer;
    private ValidateViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validate_activity);
        setTitle("绑定");
        ButterKnife.bind(this);
        initDataListener();
        String phoneNumber = SPUtil.getSPLSUtil("PhoneNumber","");
        if (!phoneNumber.equals("")) {
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

    private void initDataListener() {
        viewModel = new ViewModelProvider(this).get(ValidateViewModel.class);
        viewModel.getLoadMoreState().observe(this, new Observer<LoadMoreState>() {
            @Override
            public void onChanged(LoadMoreState loadMoreState) {
                if(loadMoreState.loadState==LoadState.LOADING){
                    String message = loadMoreState.loadType==0?"正在获取验证码":"正在登录";
                    LSAlert.showProgressHud(ValidateActivity.this,message);
                }else{
                    LSAlert.dismissProgressHud();
                }
            }
        });

        viewModel.getThrowable().observe(this, new Observer<ExceptionHandle.ResponseThrowable>() {
            @Override
            public void onChanged(ExceptionHandle.ResponseThrowable throwable) {
                LSAlert.showAlert(ValidateActivity.this, throwable.message);
            }
        });

        //获取验证码成功
        viewModel.getValidateCodeLiveData().observe(this, new Observer<ValidateCodeBean>() {
            @Override
            public void onChanged(ValidateCodeBean validateCodeBean) {
               SPUtil.setSPLSUtil("Token", validateCodeBean.getToken());
                SPUtil.setSPLSUtil("PhoneNumber", validateCodeBean.getPhoneNumber());
                //开始进入验证码倒计时时间
                getValidateButton.setBackgroundResource(R.drawable.yanzhengma);
                getValidateButton.setEnabled(false);
                countDownTimer.start();
            }
        });

        //绑定成功
        viewModel.getLoginUserLiveData().observe(this, new Observer<LoginUserBean>() {
            @Override
            public void onChanged(LoginUserBean loginUserBean) {
                Intent intent = new Intent();
                intent.putExtra("userName", userName);
                intent.putExtra("password", passWord);
                setResult(RESULT_OK, intent);
                finish();
            }
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
}
