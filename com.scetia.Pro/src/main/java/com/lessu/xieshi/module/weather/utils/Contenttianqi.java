package com.lessu.xieshi.module.weather.utils;

import com.scetia.Pro.common.Util.SPUtil;

/**
 * Created by fhm on 2017/10/26.
 */

public class Contenttianqi {

    public static String getwinddengji(String wind){
        if("静风".equals(wind)){
            return "0";
        }else if("软风".equals(wind)){
            return "1";
        }else if("轻风".equals(wind)){
            return "2";
        }else if("微风".equals(wind)){
            return "4";
        }else if("和风".equals(wind)){
            return "7";
        }else if("清劲风".equals(wind)){
            return "9";
        }else if("强风".equals(wind)){
            return "12";
        }else if("疾风".equals(wind)){
            return "16";
        }else if("大风".equals(wind)){
            return "19";
        }else if("烈风".equals(wind)){
            return "23";
        }else if("狂风".equals(wind)){
            return "26";
        }else if("暴风".equals(wind)){
            return "31";
        }else if("飓风".equals(wind)){
            return "23";
        }else{
            return "";
        }
    }


}
