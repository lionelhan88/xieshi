package com.lessu.xieshi.module.weather.bean;

import java.util.List;

/**
 * Created by fhm on 2017/10/26.
 */

public class Tenbean {
    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String report_date;
        private String city_name;
        private List<ItemsBean> items;

        public String getReport_date() {
            return report_date;
        }

        public void setReport_date(String report_date) {
            this.report_date = report_date;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {

            private String datatime;
            private String direction;
            private String speed;
            private String tempe;
            private String weather;
            private String weatherpic;

            public String getDatatime() {
                return datatime;
            }

            public void setDatatime(String datatime) {
                this.datatime = datatime;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getSpeed() {
                return speed;
            }

            public void setSpeed(String speed) {
                this.speed = speed;
            }

            public String getTempe() {
                return tempe;
            }

            public void setTempe(String tempe) {
                this.tempe = tempe;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getWeatherpic() {
                return weatherpic;
            }

            public void setWeatherpic(String weatherpic) {
                this.weatherpic = weatherpic;
            }
        }
    }
}
