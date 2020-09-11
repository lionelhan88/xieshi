package com.lessu.xieshi.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.meet.MeetingDetailActivity;

/**
 * Created by fhm on 2017/2/14.
 */

public class ToastUtil {
    static Toast mToast = null;
    public static void showShort( String msg) {

        if (null == mToast) {
            mToast = Toast.makeText(AppApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showSignedSuccess(Context context,String hyCodeStr,String hyNameStr,String hyUserName ){
        View view= LayoutInflater.from(context).inflate(R.layout.meeting_sign_success_dialog_layout,null,false);
        TextView hyCode = view.findViewById(R.id.meeting_success_dialog_hy_code);
        TextView hyName = view.findViewById(R.id.meeting_success_dialog_hy_dw);
        TextView hyUser = view.findViewById(R.id.meeting_success_dialog_user);
        hyCode.setText(hyCodeStr);
        hyName.setText(hyNameStr);
        hyUser.setText(hyUserName);
        LSAlert.showAlert(context, "提示", view, "确定",null);
    }
}
