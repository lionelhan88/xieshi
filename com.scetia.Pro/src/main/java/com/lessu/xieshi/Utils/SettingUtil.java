package com.lessu.xieshi.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.lessu.xieshi.module.login.LoginActivity;
import com.scetia.Pro.common.Util.Constants;

/**
 * created by ljs
 * on 2021/1/22
 */
public class SettingUtil {
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

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
