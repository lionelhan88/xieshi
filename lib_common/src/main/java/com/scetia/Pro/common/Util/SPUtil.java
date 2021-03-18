package com.scetia.Pro.common.Util;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.tencent.mmkv.MMKV;

/**
 * created by ljs
 * on 2021/1/6
 * <p>
 * 轻量数据存储工具
 */
public class SPUtil {
    private static final String SP_LSUTIL = "LSUtil";
    private static final String SP_CONFIG = "config";

    public static final String BLUETOOTH_DEVICE = "deviceaddress";
    public static final String AUTO_LOGIN_KEY = "auto_login_key";

    private static MMKV mmkv1, mmkv2;

    public static void init(Context context) {
        MMKV.initialize(context);
        mmkv1 = MMKV.mmkvWithID(SP_LSUTIL);
        mmkv2 = MMKV.mmkvWithID(SP_CONFIG);
        SharedPreferences spLSUtil = context.getSharedPreferences(SP_LSUTIL, Context.MODE_PRIVATE);
        SharedPreferences spConfig = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE);

        mmkv1.importFromSharedPreferences(spLSUtil);
        mmkv2.importFromSharedPreferences(spConfig);
        spLSUtil.edit().clear().commit();
        spConfig.edit().clear().commit();
    }

    public static <E> void setSPLSUtil(@NonNull String key, @NonNull E value) {
        set(SP_LSUTIL, key, value);
    }

    public static <E> void setSPConfig(@NonNull String key, @NonNull E value) {
        set(SP_CONFIG, key, value);
    }

    public static <E> E getSPLSUtil(@NonNull String key, @NonNull E value) {
        return get(SP_LSUTIL, key, value);
    }

    public static <E> E getSPConfig(@NonNull String key, @NonNull E value) {
        return get(SP_CONFIG, key, value);
    }


    private static <E> E get(String preferName, @NonNull String key, @NonNull E defaultValue) {
        if (defaultValue instanceof String) {
            String value = null;
            if (preferName.equals(SP_LSUTIL)) {
                value = mmkv1.getString(key, String.valueOf(defaultValue));
            } else if (preferName.equals(SP_CONFIG)) {
                value = mmkv2.getString(key, String.valueOf(defaultValue));
            }
            return (E) value;
        } else if (defaultValue instanceof Boolean) {
            Boolean value = false;
            if (preferName.equals(SP_LSUTIL)) {
                value = mmkv1.getBoolean(key, (Boolean) defaultValue);
            } else if (preferName.equals(SP_CONFIG)) {
                value = mmkv2.getBoolean(key, (Boolean) defaultValue);
            }
            return (E) value;
        }
        return null;
    }

    private static <E> void set(String preferName, @NonNull String key, @NonNull E value) {
        SharedPreferences.Editor editor = null;
        if (preferName.equals(SP_LSUTIL)) {
            editor = mmkv1.edit();
        } else if (preferName.equals(SP_CONFIG)) {
            editor = mmkv2.edit();
        }
        if (value instanceof String) {
            editor.putString(key, String.valueOf(value));
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
    }

    public static String GET_SERVICE_API(){
        return getSPLSUtil(Constants.Setting.SERVICE,"");
    }

    public static void clearData(){
        mmkv1.clear().clear();
        mmkv2.clear().clear();
    }

}
