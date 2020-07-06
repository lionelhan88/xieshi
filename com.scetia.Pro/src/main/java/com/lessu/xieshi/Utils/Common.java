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

    public static int betweenDate(Date cureDate,Date endDate){
       /* Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTime(cureDate);
        Calendar endCalendar = Calendar.getInstance();*/
       int day = -1;
        if(endDate.getTime()>=cureDate.getTime()){
            long l = endDate.getTime() - cureDate.getTime();
             day = (int) (l/1000/3600/24);
        }
        return day;

    }
}
