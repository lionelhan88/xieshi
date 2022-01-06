package com.lessu.xieshi.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.lessu.xieshi.module.login.LoginActivity;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.SPUtil;

/**
 * created by ljs
 * on 2021/1/22
 */
public class DeviceUtil {
    /**
     * 退出登录
     */
    public static void loginOut(Context context) {
        Intent intentLoginOut = new Intent();
        intentLoginOut.setClass(context, LoginActivity.class);
        intentLoginOut.putExtra(Constants.Setting.EXIT, true);
        intentLoginOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentLoginOut);
    }

    /**
     * 获取当前版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前设备的唯一序列号
     */
    public static String getDeviceId(Context context) {
        //获取存在本地的deviceID
        String deviceId = SPUtil.getSPConfig(Constants.User.KEY_DEVICE_ID, "");
        /*如果本地已经存在deviceID，则使用本地的deviceID*/
        if (TextUtils.isEmpty(deviceId)) {
            //如果没有取到本地存储的device_id,就获取系统的IEMI
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //android9以后获取不到IEMI的编码了，可能会发出异常
            try {
                deviceId = telephonyManager.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return deviceId;
    }
}
