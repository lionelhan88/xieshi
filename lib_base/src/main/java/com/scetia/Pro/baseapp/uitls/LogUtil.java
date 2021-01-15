package com.scetia.Pro.baseapp.uitls;

import android.util.Log;

import com.scetia.Pro.baseapp.BuildConfig;

/**
 * Created by fhm on 2018/10/23.
 */

public class LogUtil {
    private static final String TAG = "TAG_SCETIA";
    public static void showLogD(String logMsg){
        if(BuildConfig.DEBUG) {
            Log.d(TAG, logMsg);
        }
    }
    public static void showLogE(String logMsg){
        if(BuildConfig.DEBUG){
            Log.e(TAG,logMsg);
        }
    }
}
