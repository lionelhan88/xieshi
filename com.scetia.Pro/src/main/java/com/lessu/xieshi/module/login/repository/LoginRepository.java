package com.lessu.xieshi.module.login.repository;

import com.google.gson.JsonObject;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.module.weather.WeatherApiService;
import com.lessu.xieshi.module.weather.bean.Hourbean;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.scetia.Pro.network.base.BaseRepository;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.base.BaseRetrofitManage;
import com.scetia.Pro.network.bean.XSResultData;
import com.lessu.xieshi.http.service.LoginApiService;
import com.lessu.xieshi.module.login.bean.LoginUser;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.util.HashMap;

/**
 * created by ljs
 * on 2020/11/19
 */
public class LoginRepository extends BaseRepository {

    public void login(String userName, String password, String deviceId, ResponseObserver<LoginUser> callBack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserName", userName);
        jsonObject.addProperty("PassWord", password);
        jsonObject.addProperty("DeviceId", deviceId);
        XSRetrofit.getInstance().getService(LoginApiService.class).login(jsonObject.toString())
                .compose(BaseRetrofitManage.<XSResultData<LoginUser>, LoginUser>applyTransformer())
                .subscribe(callBack);
    }

    public void getHourWeather(String token, String JD, String WD, ResponseObserver<Hourbean> callBack) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        params.put("JD", JD);
        params.put("WD", WD);
        requestApi(XSRetrofit.getInstance().getService(WeatherApiService.class).getToadyHour(GsonUtil.mapToJsonStr(params)),callBack);
    }
}
