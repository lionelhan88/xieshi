package com.lessu.xieshi.module.mis.activitys;

import com.scetia.Pro.common.Util.SPUtil;

/**
 * Created by fhm on 2017/10/12.
 */

public class Content {
    public static String getToken() {
        String token;
        token = SPUtil.getSPLSUtil("Token","");//自己的token
        return token;
    }
}
