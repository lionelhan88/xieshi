package com.lessu.xieshi.module.login.repository;

import com.google.gson.JsonObject;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.network.base.BaseRepository;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.bean.XSResultData;
import com.lessu.xieshi.http.service.LoginApiService;
import com.lessu.xieshi.module.login.bean.LoginUser;
import com.lessu.xieshi.module.login.bean.ValidateCodeBean;
import com.scetia.Pro.network.manage.XSRetrofit;

/**
 * created by ljs
 * on 2020/12/8
 */
public class ValidateRepository extends BaseRepository {
    public void getPhoneCheckCode(String userName, String password, String deviceId, String phoneNumber,
                                  ResponseObserver<ValidateCodeBean> callBack){
        JsonObject paramJson = new JsonObject();
        paramJson.addProperty("UserName", userName);
        paramJson.addProperty("PassWord", password);
        paramJson.addProperty("DeviceId", deviceId);
        paramJson.addProperty("PhoneNumber", phoneNumber);
        requestApi(  XSRetrofit.getInstance().getService(LoginApiService.class)
                .getVerificationCode(paramJson.toString()),callBack);
    }

    public void validatePhone(String checkCode, ResponseObserver<LoginUser> callBack){
        JsonObject paramJson = new JsonObject();
        paramJson.addProperty("Token",  Constants.User.GET_TOKEN());
        paramJson.addProperty("CheckCode", checkCode);
        requestApi( XSRetrofit.getInstance().getService(LoginApiService.class)
                .validateVerificationCode(paramJson.toString()),callBack);
    }

}
