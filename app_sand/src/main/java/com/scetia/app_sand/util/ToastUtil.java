package com.scetia.app_sand.util;

import android.widget.Toast;

import com.scetia.Pro.baseapp.ShareableApplication;

/**
 * Created by fhm on 2017/2/14.
 */

public class ToastUtil {
    private static Toast mToast = null;
    public static void showShort(String msg) {
        if (null == mToast) {
            mToast = Toast.makeText(ShareableApplication.getInstance(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
