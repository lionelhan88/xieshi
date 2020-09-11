package com.lessu.xieshi.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by fhm on 2016/7/21.
 */
public class Common {
    public static final String USERPOWER="USERPOWER";
    public static final String USERNAME="USERNAME";
    public static final String PASSWORD="PASSWORD";
    public static final String DEVICEID="DEVICEID";
    public static final String PHONENUMBER="PHONENUMBER";
    public static final String PICNAME="PICNAME";
    //2020-06-01 存入userid，为了扫码登录
    public static final String USERID="user_id";
    /**
     * 用户单位信息
     */
    public static final String MEMBERINFOSTR="MemberInfoStr";

    public static final String DX_BASE_URL = "https://bgtj.o-learn.cn";
    public static final String DX_SCAN_LOGIN = DX_BASE_URL+"/thirdparty/jzjc/appInterfaceApi/scanQRCode";


    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }
}
