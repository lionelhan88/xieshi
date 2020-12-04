package com.lessu.xieshi.module.weather;

import com.lessu.xieshi.module.weather.bean.Hourbean;
import com.lessu.xieshi.module.weather.bean.Tenbean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * created by ljs
 * on 2020/12/2
 */
public interface WeatherApiService {
    @GET("ServiceWeather.asmx/GetDay")
    public Observable<Tenbean> getFutureTenDays(@Query("param") String param);

    @GET("ServiceWeather.asmx/GetHour")
    public Observable<Hourbean> getToadyHour(@Query("param") String param);
}
