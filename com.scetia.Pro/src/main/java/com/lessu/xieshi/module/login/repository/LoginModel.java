package com.lessu.xieshi.module.login.repository;

import com.google.gson.JsonObject;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.base.BaseRetrofitManage;
import com.scetia.Pro.network.bean.XSResultData;
import com.lessu.xieshi.http.api.LoginApiService;
import com.lessu.xieshi.module.login.bean.LoginUserBean;
import com.scetia.Pro.network.manage.XSRetrofit;

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
