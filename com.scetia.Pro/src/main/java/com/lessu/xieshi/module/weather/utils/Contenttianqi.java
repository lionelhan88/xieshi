package com.lessu.xieshi.module.weather.utils;

import com.lessu.foundation.LSUtil;

/**
 * Created by fhm on 2017/10/26.
 */

public class Contenttianqi {
    public  static String gettoken(){
        String token;
        token = LSUtil.valueStatic("Token");
       // token ="f1af7a4e-a927-42c7-9b9b-82e1feaa5c21";
       // token ="25dbc35a-ae8e-4847-957e-a502aafd0611";
       // token="1137cfb7-1c1d-4590-9b27-5867bb5bdf48";
        return token;
    }
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
