package com.scetia.Pro.common.Util;

import android.content.Context;
import android.content.pm.PackageManager;


import java.util.HashMap;

/**
 * Created by fhm on 2016/7/21.
 */
public class Common {
    private static HashMap<String, String> serviceMap;
    public static String SCAN_TYPE = "";
    /**在扫码入口 需要识别具体的扫码动作*/
    public static final String SCAN_MEETING_SIGNED = "scan_meeting_signed";
    public static final String USERPOWER="USERPOWER";
    public static final String USERNAME="USERNAME";
    public static final String USER_FULL_NAME="USERFULLNAME";
    public static final String PASSWORD="PASSWORD";
    public static final String DEVICEID="DEVICEID";
    public static final String PHONENUMBER="PHONENUMBER";
    public static final String PICNAME="PICNAME";
    //存放jwt的值
    public static final String JWT_KEY = "jwt_key";
    //2020-06-01 存入userid，为了扫码登录
    public static final String USERID="user_id";
    /**用户单位信息*/
    public static final String MEMBERINFOSTR="MemberInfoStr";

    public static final String TELECOM_SERVICE = "telecom";
    public static final String UNICOM_SERVICE = "unicom";


    public static String getVersionName(Context context){
        try {
             return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setService(String key,String value){
        if(serviceMap==null){
            serviceMap = new HashMap<>();
        }
        serviceMap.put(key,value);
    }

    public static String getService(String key){
        return serviceMap.get(key);
    }
}
