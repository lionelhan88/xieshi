package com.lessu.xieshi.module.mis.activitys;

import com.lessu.foundation.LSUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by fhm on 2017/10/12.
 */

public class Content {
    public static String getToken() {
        String token;
        token = LSUtil.valueStatic("Token");//自己的token
        return token;
    }
}
