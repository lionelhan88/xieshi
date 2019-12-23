package com.lessu.xieshi.Utils;

import android.widget.Toast;

import com.lessu.xieshi.AppApplication;

/**
 * Created by fhm on 2017/2/14.
 */

public class MyToast {
    static Toast mToast = null;
    public static void showShort( String msg) {

        if (null == mToast) {
            //mToast=new Toast(AppApplication.getAppContext());
            mToast = Toast.makeText(AppApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
            // mToast.setGravity(Gravity.BOTTOM, 0, 350);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
