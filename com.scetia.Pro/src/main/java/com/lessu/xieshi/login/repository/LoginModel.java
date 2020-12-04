package com.lessu.xieshi.login.repository;

import com.google.gson.JsonObject;
import com.lessu.xieshi.http.ResponseObserver;
import com.lessu.xieshi.http.XSResultData;
import com.lessu.xieshi.base.BaseRetrofitManage;
import com.lessu.xieshi.http.XSRetrofit;
import com.lessu.xieshi.login.LoginApiService;
import com.lessu.xieshi.login.bean.LoginUserBean;

/**
 * created by ljs
 * on 2020/11/19
 */
public class LoginModel  {

    public void login(String userName, String password, String deviceId, ResponseObserver<LoginUserBean> callBack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserName", userName);
        jsonObject.addProperty("PassWord", password);
        jsonObject.addProperty("DeviceId", deviceId);
        XSRetrofit.getInstance().getService(LoginApiService.class).login(jsonObject.toString())
                .compose(BaseRetrofitManage.<XSResultData<LoginUserBean>, LoginUserBean>applyTransformer())
                .subscribe(callBack);
    }
}
