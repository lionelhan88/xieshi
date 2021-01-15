package com.lessu.xieshi.module.login.repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.base.BaseRetrofitManage;
import com.scetia.Pro.network.bean.XSResultData;
import com.lessu.xieshi.http.api.LoginApiService;
import com.lessu.xieshi.module.login.bean.LoginUserBean;
import com.lessu.xieshi.module.weather.WeatherApiService;
import com.lessu.xieshi.module.weather.bean.Hourbean;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.util.HashMap;

/**
 * created by ljs
 * on 2020/11/20
 */
public class FirstModel {
    public void login(String userName, String password, String deviceId, ResponseObserver<LoginUserBean> callBack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserName", userName);
        jsonObject.addProperty("PassWord", password);
        jsonObject.addProperty("DeviceId", deviceId);
        XSRetrofit.getInstance().getService(LoginApiService.class).login(jsonObject.toString())
                .compose(BaseRetrofitManage.<XSResultData<LoginUserBean>, LoginUserBean>applyTransformer())
                .subscribe(callBack);
    }

    public void getHourWeather(String token, String JD, String WD, ResponseObserver<Hourbean> callBack) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        params.put("JD", JD);
        params.put("WD", WD);
        XSRetrofit.getInstance().getService(WeatherApiService.class)
                .getToadyHour(new Gson().toJson(params))
                .compose(XSRetrofit.<Hourbean,Hourbean>applyTransformer())
                .subscribe(callBack);
    }
}
