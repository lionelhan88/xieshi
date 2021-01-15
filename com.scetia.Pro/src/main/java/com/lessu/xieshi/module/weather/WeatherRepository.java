package com.lessu.xieshi.module.weather;

import com.google.gson.Gson;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.module.weather.bean.Hourbean;
import com.lessu.xieshi.module.weather.bean.Tenbean;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.util.HashMap;

/**
 * created by ljs
 * on 2020/12/2
 */
public class WeatherRepository {
    public void getFutureTenDays(String token, ResponseObserver<Tenbean> callBack) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        XSRetrofit.getInstance().getService(WeatherApiService.class)
                .getFutureTenDays(new Gson().toJson(params))
                .compose(XSRetrofit.<Tenbean, Tenbean>applyTransformer())
                .subscribe(callBack);
    }

    public void getToadyHour(String token, String JD, String WD, ResponseObserver<Hourbean> callBack) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        params.put("JD", JD);
        params.put("WD", WD);
        XSRetrofit.getInstance().getService(WeatherApiService.class)
                .getToadyHour(new Gson().toJson(params))
                .compose(XSRetrofit.<Hourbean, Hourbean>applyTransformer())
                .subscribe(callBack);
    }
}
