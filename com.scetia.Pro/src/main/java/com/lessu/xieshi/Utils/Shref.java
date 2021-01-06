package com.lessu.xieshi.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fhm on 2016/7/21.
 */
public class Shref {
    public static final String PREF_NAME = "config";
    public static final String BLUETOOTH_DEVICE="deviceaddress";
    public static final String AUTO_LOGIN_KEY="auto_login_key";

    public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }
    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setString(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }


}
