package com.lessu.xieshi.module.login.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.foundation.ValidateHelper;
import com.lessu.xieshi.base.BaseViewModel;
import com.lessu.xieshi.bean.LoadMoreState;
import com.lessu.data.LoadState;
import com.lessu.xieshi.http.exceptionhandle.ExceptionHandle;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.module.login.bean.LoginUserBean;
import com.lessu.xieshi.module.login.bean.ValidateCodeBean;
import com.lessu.xieshi.module.login.repository.ValidateRepository;
import com.lessu.xieshi.module.mis.activitys.Content;

/**
 * created by ljs
 * on 2020/12/8
 */
public class ValidateViewModel extends BaseViewModel {
    private ValidateRepository repository = new ValidateRepository();
    private MutableLiveData<ValidateCodeBean> validateCodeLiveData = new MutableLiveData<>();
    private MutableLiveData<LoginUserBean> loginUserLiveData = new MutableLiveData<>();
    public ValidateViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ValidateCodeBean> getValidateCodeLiveData() {
        return validateCodeLiveData;
    }

    public MutableLiveData<LoginUserBean> getLoginUserLiveData() {
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
            throwableLiveData.postValue(new ExceptionHandle.ResponseThrowable(ExceptionHandle.LOCAL_ERROR,"手机号输入有误！请重新输入"));
            return;
        }
        loadMoreState.postValue(new LoadMoreState(0,LoadState.LOADING));
        repository.getPhoneCheckCode(userName, password, deviceId, phoneNumber, new ResponseObserver<ValidateCodeBean>() {
            @Override
            public void success(ValidateCodeBean validateCodeBean) {
                loadMoreState.postValue(new LoadMoreState(0,LoadState.SUCCESS));
                validateCodeLiveData.postValue(validateCodeBean);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadMoreState.postValue(new LoadMoreState(0,LoadState.FAILURE));
                throwableLiveData.postValue(throwable);
            }
        });
    }

    /**
     * 验证手机
     */
    public void validatePhone(String checkCode){
        if (TextUtils.isEmpty(checkCode)) {
            throwableLiveData.postValue(new ExceptionHandle.ResponseThrowable(ExceptionHandle.LOCAL_ERROR,"请输入验证码"));
            return;
        }
        if(TextUtils.isEmpty(Content.getToken())){
            throwableLiveData.postValue(new ExceptionHandle.ResponseThrowable(ExceptionHandle.LOCAL_ERROR,"请重新获取验证码"));
            return;
        }
        loadMoreState.postValue(new LoadMoreState(1,LoadState.LOADING));
        repository.validatePhone(checkCode, new ResponseObserver<LoginUserBean>() {
            @Override
            public void success(LoginUserBean loginUserBean) {
                loadMoreState.postValue(new LoadMoreState(1,LoadState.SUCCESS));
                loginUserLiveData.postValue(loginUserBean);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadMoreState.postValue(new LoadMoreState(1,LoadState.SUCCESS));
                throwableLiveData.postValue(throwable);
            }
        });
    }
}
