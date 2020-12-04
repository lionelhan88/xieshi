package com.lessu.xieshi.module.weather.bean;

/**
 * Created by fhm on 2017/9/27.
 */

public class DailyForecast {

    public double maxper;   //最高温度的平均值
    public double minper;   //最低温度的平均值
    public int maxtemp;     //高的温度
    public int mintemp;     //低的温度
    public String date;     //日期
    public String tianqi;   //天气
    public String wind;     //风力

    public int picup;
    public int picdown;


    public DailyForecast(double maxper, double minper, int maxtemp, int mintemp, String date, String tianqi, String wind, int picup, int picdown) {
        this.maxper = maxper;
        this.minper = minper;
        this.maxtemp = maxtemp;
        this.mintemp = mintemp;
        this.tianqi = tianqi;
        this.date = date;
        this.wind = wind;
        this.picup = picup;
        this.picdown = picdown;
    }

}
