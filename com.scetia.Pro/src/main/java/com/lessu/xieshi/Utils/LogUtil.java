package com.lessu.xieshi.Utils;

import android.util.Log;

/**
 * Created by fhm on 2018/10/23.
 */

public class LogUtil {
    private static final String TAG = "TAGSCETIA";
    private static boolean isShowLog = true;
    public static void showLogD(String logMsg){
        if(isShowLog) {
            Log.d(TAG, logMsg);
        }
    }
    public static void showLogE(String logMsg){
        if(isShowLog){
            Log.e(TAG,logMsg);
        }
    }
}
