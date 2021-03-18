package com.lessu.xieshi.module.login.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.login.bean.LoginUser;
import com.lessu.xieshi.module.login.repository.LoginRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * created by ljs
 * on 2020/11/19
 */
public class LoginViewModel extends BaseViewModel {
    public static final String TO_ACTIVITY = "to_activity";
    private LoginRepository model = new LoginRepository();
    private MutableLiveData<Map<String,Object>> mapLiveData = new MutableLiveData<>();
    private String curDeviceId;
    private String curPassword;
    public LoginViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<Map<String, Object>> getMapLiveData() {
        return mapLiveData;
    }

    public void login(String userName, String password) {
        if(TextUtils.isEmpty(userName) ||TextUtils.isEmpty(password)){
            loadState.setValue(LoadState.FAILURE.setMessage("账号或密码不能为空"));
            return;
        }
        loadState.postValue(LoadState.LOADING);
        curDeviceId = getDeviceId();
        model.login(userName, password, curDeviceId, new ResponseObserver<LoginUser>() {
            @Override
            public void success(LoginUser loginUser) {
                curPassword = password;
                loadState.postValue(LoadState.SUCCESS);
                checkIsFirstLogin(loginUser);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE.setCode(throwable.code).setMessage(throwable.message));
            }
        });
    }

    /**
     * 检查是否是第一次登陆
     */
    private void checkIsFirstLogin(LoginUser loginUser){
        Map<String,Object> map = new HashMap<>();
        String userPower = loginUser.getUserPower().equals("（待定）") ? "1" : loginUser.getUserPower();
        String userName = loginUser.getUserName();
        String token = loginUser.getToken();
        String PhoneNumber = loginUser.getPhoneNumber();
        String userId = loginUser.getUserId();
        String MemberInfoStr = loginUser.getMemberInfoStr();
        boolean isFirstLogin = loginUser.isIsFirstLogin();
        SPUtil.setSPLSUtil("PhoneNumber", PhoneNumber);
        SPUtil.setSPLSUtil("UserName", userName);
        //TODO:判断是否是第一次登陆，如果是第一次登陆就进入手机验证界面
        if (isFirstLogin) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            bundle.putString("UserName", userName);
            bundle.putString("PassWord", curPassword);
            bundle.putString("DeviceId", curDeviceId);
            bundle.putString("UserPower", userPower);
            map.put(TO_ACTIVITY,bundle);
        } else {
            LoginUser.setLoginUser(loginUser);
            SPUtil.setSPLSUtil(Constants.User.XS_TOKEN, token);
            SPUtil.setSPConfig(Constants.User.KEY_USER_NAME, userName);
            SPUtil.setSPConfig(Constants.User.KEY_PASSWORD, curPassword);
            SPUtil.setSPConfig(Constants.User.KEY_DEVICE_ID, curDeviceId);
            SPUtil.setSPConfig(Constants.User.KEY_USER_POWER, userPower);
            SPUtil.setSPConfig(Constants.User.USER_ID, userId);
            SPUtil.setSPConfig(Constants.User.MEMBER_INFO_STR, MemberInfoStr);
            Constants.User.setBoundUnitId(loginUser.getBoundToUnitID());

            //存放jwtToken
            SPUtil.setSPLSUtil(Constants.User.JWT_KEY, loginUser.getJwt());
            SPUtil.setSPConfig(Constants.User.KEY_USER_FULL_NAME, loginUser.getUserFullName());
            //这里将自动登录取消，避免用户从登录页面进入后，再次在主菜单界面登录一次
            SPUtil.setSPConfig(SPUtil.AUTO_LOGIN_KEY, false);
            map.put(TO_ACTIVITY,userPower);
        }
        mapLiveData.setValue(map);
    }

    /**
     * 获取设备的uid号
     */
    private String getDeviceId() {
        //获取存在本地的deviceID
        String deviceId = SPUtil.getSPConfig(Constants.User.KEY_DEVICE_ID, "");
        /*如果本地已经存在deviceID，则使用本地的deviceID*/
        if (TextUtils.isEmpty(deviceId)) {
            //如果没有取到本地存储的device_id,就获取系统的IEMI
            TelephonyManager telephonyManager = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
            //android9以后获取不到IEMI的编码了，可能会发出异常
            try {
                deviceId = telephonyManager.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = Settings.System.getString(getApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return deviceId;
    }
}
