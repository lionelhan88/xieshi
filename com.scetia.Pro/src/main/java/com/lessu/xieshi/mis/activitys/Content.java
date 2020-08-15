package com.lessu.xieshi.mis.activitys;

import com.lessu.foundation.LSUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by fhm on 2017/10/12.
 */

public  class Content {
    public  static String gettoken(){
        String token;
        token = LSUtil.valueStatic("Token");//自己的token
        //token ="f1af7a4e-a927-42c7-9b9b-82e1feaa5c21";//肖珍珠token
        //token ="AEE833D2-C56F-4E1B-8248-65E13FCE5F54";//大领导token
        //token ="c51a249e-8c4c-4e52-9ba5-110555e331fc";//领导token
        //token = "2321cbe7-3cfa-4741-b289-89b55b9e8265";
        return token;
    }



    public static double getbetweendatas(String beginTime, String endTime){
        String str1 = beginTime;  //"yyyyMMdd"格式 如 20131022

        String str2 = endTime;  //"yyyyMMdd"格式 如 20131022
        System.out.println("\n结束时间:"+str1+"......."+str2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");//输入日期的格式
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        double dayCount = (cal2.getTimeInMillis()-cal1.getTimeInMillis())/(1000*3600*24);//从间隔毫秒变成间隔天数
        dayCount++;
        System.out.println("\n相差"+dayCount+"天");
        return dayCount;
    }
}
