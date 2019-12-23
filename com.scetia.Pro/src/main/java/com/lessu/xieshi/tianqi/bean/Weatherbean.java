package com.lessu.xieshi.tianqi.bean;

/**
 * Created by fhm on 2017/11/6.
 */

public class Weatherbean {

    public String weather;

    public int dengji;

    public int weathersmall;//小图标

    public int weatherbig;//大图标


    public Weatherbean(String weather, int dengji, int weathersmall, int weatherbig) {
        this.weather = weather;
        this.dengji = dengji;
        this.weathersmall = weathersmall;
        this.weatherbig = weatherbig;
    }
}
