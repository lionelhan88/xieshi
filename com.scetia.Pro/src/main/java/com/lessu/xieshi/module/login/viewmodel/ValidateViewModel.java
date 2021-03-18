package com.lessu.xieshi.module.login.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.foundation.ValidateHelper;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.module.login.bean.LoginUser;
import com.lessu.xieshi.module.login.bean.ValidateCodeBean;
import com.lessu.xieshi.module.login.repository.ValidateRepository;

/**
 * created by ljs
 * on 2020/12/8
 */
public class ValidateViewModel extends BaseViewModel {
    private ValidateRepository repository = new ValidateRepository();
    private MutableLiveData<ValidateCodeBean> validateCodeLiveData = new MutableLiveData<>();
    private MutableLiveData<LoginUser> loginUserLiveData = new MutableLiveData<>();
    public ValidateViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ValidateCodeBean> getValidateCodeLiveData() {
        return validateCodeLiveData;
    }

    public MutableLiveData<LoginUser> getLoginUserLiveData() {
        return loginUserLiveData;
    }

    /**
     * 获取手机验证码
     * @param userName
     * @param password
     * @param deviceId
     * @param phoneNumber
     */
    public void getPhoneCheckCode(String userName,String password,String deviceId,String phoneNumber){
        if (!ValidateHelper.validatePhone(phoneNumber)) {
            loadState.setValue(LoadState.FAILURE.setMessage("手机号输入有误！请重新输入"));
            return;
        }
        loadState.setValue(LoadState.LOADING.setMessage("正在获取验证码..."));
        repository.getPhoneCheckCode(userName, password, deviceId, phoneNumber, new ResponseObserver<ValidateCodeBean>() {
            @Override
            public void success(ValidateCodeBean validateCodeBean) {
                loadState.setValue(LoadState.SUCCESS);
                validateCodeLiveData.postValue(validateCodeBean);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }

    /**
     * 验证手机
     */
    public void validatePhone(String checkCode){
        if (TextUtils.isEmpty(checkCode)) {
            loadState.setValue(LoadState.FAILURE.setMessage("请输入验证码"));
            return;
        }
        if(TextUtils.isEmpty( Constants.User.GET_TOKEN())){
            loadState.setValue(LoadState.FAILURE.setMessage("请重新获取验证码"));
            return;
        }
        loadState.setValue(LoadState.LOADING.setMessage("正在登陆..."));
        repository.validatePhone(checkCode, new ResponseObserver<LoginUser>() {
            @Override
            public void success(LoginUser loginUser) {
                loadState.setValue(LoadState.SUCCESS);
                loginUserLiveData.postValue(loginUser);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.setValue(LoadState.FAILURE.setMessage(throwable.message));
            }
        });
    }
}
