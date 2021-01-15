package com.lessu.xieshi.module.login.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.lessu.xieshi.base.BaseViewModel;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.login.bean.LoginUserBean;
import com.lessu.xieshi.module.login.repository.LoginModel;

/**
 * created by ljs
 * on 2020/11/19
 */
public class LoginViewModel extends BaseViewModel {
    private LoginModel model = new LoginModel();

    private MutableLiveData<LoginUserBean> userBeanData = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<LoginUserBean> getUserBeanData() {
        return userBeanData;
    }

    public void login(String userName, String password, String deviceId) {
        if(userName.isEmpty()||password.isEmpty()){
            throwableLiveData.postValue(new ExceptionHandle.ResponseThrowable(0,"账号或密码不能为空"));
            return;
        }
        loadState.postValue(LoadState.LOADING);
        model.login(userName, password, deviceId, new ResponseObserver<LoginUserBean>() {
            @Override
            public void success(LoginUserBean loginUserBean) {
                loadState.postValue(LoadState.SUCCESS);
                userBeanData.postValue(loginUserBean);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE);
                throwableLiveData.postValue(throwable);
            }
        });
    }
}
