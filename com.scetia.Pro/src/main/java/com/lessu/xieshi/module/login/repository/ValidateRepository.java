package com.lessu.xieshi.module.login.repository;

import com.google.gson.JsonObject;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.bean.XSResultData;
import com.lessu.xieshi.http.api.LoginApiService;
import com.lessu.xieshi.module.login.bean.LoginUserBean;
import com.lessu.xieshi.module.login.bean.ValidateCodeBean;
import com.lessu.xieshi.module.mis.activitys.Content;
import com.scetia.Pro.network.manage.XSRetrofit;

/**
 * created by ljs
 * on 2020/12/8
 */
public class ValidateRepository {
    public void getPhoneCheckCode(String userName, String password, String deviceId, String phoneNumber,
                                  ResponseObserver<ValidateCodeBean> callBack){
        JsonObject paramJson = new JsonObject();
        paramJson.addProperty("UserName", userName);
        paramJson.addProperty("PassWord", password);
        paramJson.addProperty("DeviceId", deviceId);
        paramJson.addProperty("PhoneNumber", phoneNumber);
        XSRetrofit.getInstance().getService(LoginApiService.class)
                .getVerificationCode(paramJson.toString())
                .compose(XSRetrofit.<XSResultData<ValidateCodeBean>, ValidateCodeBean>applyTransformer())
                .subscribe(callBack);
    }

    public void validatePhone(String checkCode, ResponseObserver<LoginUserBean> callBack){
        JsonObject paramJson = new JsonObject();
        paramJson.addProperty("Token", Content.getToken());
        paramJson.addProperty("CheckCode", checkCode);
        XSRetrofit.getInstance().getService(LoginApiService.class)
                .validateVerificationCode(paramJson.toString())
                .compose(XSRetrofit.<XSResultData<LoginUserBean>, LoginUserBean>applyTransformer())
                .subscribe(callBack);
    }

}
