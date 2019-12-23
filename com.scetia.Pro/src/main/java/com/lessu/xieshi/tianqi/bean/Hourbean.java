package com.lessu.xieshi.tianqi.bean;

import java.util.List;

/**
 * Created by fhm on 2017/11/6.
 */

public class Hourbean {

    /**
     * code : 1
     * msg :
     * data : [{"temp":22.3,"rhrh":57.5,"dirc":"南东南","wind":"微风","rain":0,"wthr":"多云","date_time":"2017-11-06 13:11","weather_icon":""},{"temp":21.5,"rhrh":61.8,"dirc":"南东南","wind":"微风","rain":0,"wthr":"多云","date_time":"2017-11-06 14:11","weather_icon":""},{"temp":20.8,"rhrh":66.1,"dirc":"南东南","wind":"轻风","rain":0,"wthr":"多云","date_time":"2017-11-06 15:11","weather_icon":""},{"temp":20,"rhrh":70.5,"dirc":"东东南","wind":"软风","rain":0,"wthr":"多云","date_time":"2017-11-06 16:11","weather_icon":""},{"temp":19.5,"rhrh":73.6,"dirc":"东东南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-06 17:11","weather_icon":""},{"temp":19,"rhrh":76.7,"dirc":"东东南","wind":"轻风","rain":0,"wthr":"阴","date_time":"2017-11-06 18:11","weather_icon":""},{"temp":18.5,"rhrh":79.9,"dirc":"东东南","wind":"轻风","rain":0,"wthr":"阴","date_time":"2017-11-06 19:11","weather_icon":""},{"temp":18.4,"rhrh":80.9,"dirc":"东东南","wind":"轻风","rain":0,"wthr":"阴","date_time":"2017-11-06 20:11","weather_icon":""},{"temp":18.2,"rhrh":81.9,"dirc":"东南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-06 21:11","weather_icon":""},{"temp":18.1,"rhrh":82.8,"dirc":"南东南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-06 22:11","weather_icon":""},{"temp":17.8,"rhrh":86.3,"dirc":"南东南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-06 23:11","weather_icon":""},{"temp":17.4,"rhrh":89.9,"dirc":"东南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-07 00:11","weather_icon":""},{"temp":17.1,"rhrh":93.4,"dirc":"东南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-07 01:11","weather_icon":""},{"temp":17,"rhrh":93.9,"dirc":"东南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-07 02:11","weather_icon":""},{"temp":16.9,"rhrh":94.5,"dirc":"东南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-07 03:11","weather_icon":""},{"temp":16.9,"rhrh":95.1,"dirc":"东南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-07 04:11","weather_icon":""},{"temp":17.2,"rhrh":93,"dirc":"南东南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-07 05:11","weather_icon":""},{"temp":17.5,"rhrh":91,"dirc":"南西南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-07 06:11","weather_icon":""},{"temp":17.9,"rhrh":88.9,"dirc":"西南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-07 07:11","weather_icon":""},{"temp":18.8,"rhrh":85.1,"dirc":"南西南","wind":"软风","rain":0,"wthr":"阴","date_time":"2017-11-07 08:11","weather_icon":""},{"temp":19.7,"rhrh":81.2,"dirc":"南西南","wind":"轻风","rain":0,"wthr":"阴","date_time":"2017-11-07 09:11","weather_icon":""},{"temp":20.6,"rhrh":77.4,"dirc":"南西南","wind":"轻风","rain":0,"wthr":"阴","date_time":"2017-11-07 10:11","weather_icon":""},{"temp":22.5,"rhrh":57.8,"dirc":"南东南","wind":"微风","rain":0,"wthr":"多云","date_time":"2017-11-07 11:11","weather_icon":""},{"temp":22.9,"rhrh":58.7,"dirc":"东南","wind":"轻风","rain":0,"wthr":"多云","date_time":"2017-11-07 12:11","weather_icon":""}]
     */

    private String code;
    private String msg;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * temp : 22.3
         * rhrh : 57.5
         * dirc : 南东南
         * wind : 微风
         * rain : 0.0
         * wthr : 多云
         * date_time : 2017-11-06 13:11
         * weather_icon :
         */

        private double temp;
        private double rhrh;
        private String dirc;
        private String wind;
        private double rain;
        private String wthr;
        private String date_time;
        private String weather_icon;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getRhrh() {
            return rhrh;
        }

        public void setRhrh(double rhrh) {
            this.rhrh = rhrh;
        }

        public String getDirc() {
            return dirc;
        }

        public void setDirc(String dirc) {
            this.dirc = dirc;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public double getRain() {
            return rain;
        }

        public void setRain(double rain) {
            this.rain = rain;
        }

        public String getWthr() {
            return wthr;
        }

        public void setWthr(String wthr) {
            this.wthr = wthr;
        }

        public String getDate_time() {
            return date_time;
        }

        public void setDate_time(String date_time) {
            this.date_time = date_time;
        }

        public String getWeather_icon() {
            return weather_icon;
        }

        public void setWeather_icon(String weather_icon) {
            this.weather_icon = weather_icon;
        }
    }
}
