package com.lessu.xieshi.tianqi.bean;

import java.util.List;

/**
 * Created by fhm on 2017/10/26.
 */

public class Tenbean {

    /**
     * code : 1
     * msg : 上海 天气预报
     * data : {"report_date":"2017-10-25","city_name":"上海","items":[{"datatime":"2017-10-26","direction":"东北风","speed":"3-4级","tempe":"15~21℃","weather":"多云","weatherpic":"多云"},{"datatime":"2017-10-27","direction":"东北风","speed":"3-4级","tempe":"14~22℃","weather":"多云","weatherpic":"多云"},{"datatime":"2017-10-28","direction":"偏北风","speed":"4-5级","tempe":"15~22℃","weather":"晴到多云","weatherpic":"晴到多云"},{"datatime":"2017-10-29","direction":"偏北风","speed":"4-5阵风6级","tempe":"15~21℃","weather":"多云到阴","weatherpic":"多云到阴"},{"datatime":"2017-10-30","direction":"偏北风","speed":"4-5级","tempe":"13~17℃","weather":"多云","weatherpic":"多云"},{"datatime":"2017-10-31","direction":"偏北风","speed":"3-4级","tempe":"12~18℃","weather":"晴到多云","weatherpic":"晴到多云"},{"datatime":"2017-11-01","direction":"偏东风","speed":"3-4级","tempe":"13~20℃","weather":"多云","weatherpic":"多云"},{"datatime":"2017-11-02","direction":"偏北风","speed":"3-4级","tempe":"15~21℃","weather":"多云到阴有短时小雨","weatherpic":"多云到阴有短时小雨"},{"datatime":"2017-11-03","direction":"偏北风","speed":"3-4级","tempe":"14~21℃","weather":"多云到阴有短时小雨","weatherpic":"多云到阴有短时小雨"},{"datatime":"2017-11-04","direction":"偏北风","speed":"4-5级","tempe":"13~18℃","weather":"多云","weatherpic":"多云"}]}
     */

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
        /**
         * report_date : 2017-10-25
         * city_name : 上海
         * items : [{"datatime":"2017-10-26","direction":"东北风","speed":"3-4级","tempe":"15~21℃","weather":"多云","weatherpic":"多云"},{"datatime":"2017-10-27","direction":"东北风","speed":"3-4级","tempe":"14~22℃","weather":"多云","weatherpic":"多云"},{"datatime":"2017-10-28","direction":"偏北风","speed":"4-5级","tempe":"15~22℃","weather":"晴到多云","weatherpic":"晴到多云"},{"datatime":"2017-10-29","direction":"偏北风","speed":"4-5阵风6级","tempe":"15~21℃","weather":"多云到阴","weatherpic":"多云到阴"},{"datatime":"2017-10-30","direction":"偏北风","speed":"4-5级","tempe":"13~17℃","weather":"多云","weatherpic":"多云"},{"datatime":"2017-10-31","direction":"偏北风","speed":"3-4级","tempe":"12~18℃","weather":"晴到多云","weatherpic":"晴到多云"},{"datatime":"2017-11-01","direction":"偏东风","speed":"3-4级","tempe":"13~20℃","weather":"多云","weatherpic":"多云"},{"datatime":"2017-11-02","direction":"偏北风","speed":"3-4级","tempe":"15~21℃","weather":"多云到阴有短时小雨","weatherpic":"多云到阴有短时小雨"},{"datatime":"2017-11-03","direction":"偏北风","speed":"3-4级","tempe":"14~21℃","weather":"多云到阴有短时小雨","weatherpic":"多云到阴有短时小雨"},{"datatime":"2017-11-04","direction":"偏北风","speed":"4-5级","tempe":"13~18℃","weather":"多云","weatherpic":"多云"}]
         */

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
            /**
             * datatime : 2017-10-26
             * direction : 东北风
             * speed : 3-4级
             * tempe : 15~21℃
             * weather : 多云
             * weatherpic : 多云
             */

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
