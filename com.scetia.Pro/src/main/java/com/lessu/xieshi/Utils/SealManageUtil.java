package com.lessu.xieshi.Utils;

import android.content.Context;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.Constants;

/**
 * created by Lollipop
 * on 2021/3/5
 */
public class SealManageUtil {

    /**
     * 根据不同的状态显示不同的颜色
     *
     * @param state
     * @return
     */
    public static int getStateTextColorByState(Context context,int state) {
        int color = 0;
        switch (state) {
            case Constants.SealManage.STATE_APPLYING:
                //申请中
                color = ContextCompat.getColor(context, R.color.seal_matter_state_applying);
                break;
            //已审核
            case Constants.SealManage.STATE_REVIEWED:
                color = ContextCompat.getColor(context, R.color.seal_matter_state_review);
                break;
            //已批准
            case Constants.SealManage.STATE_APPROVED:
                color = ContextCompat.getColor(context, R.color.seal_matter_state_approved);
                break;
            //已盖章
            case Constants.SealManage.STATE_STAMP:
                color = ContextCompat.getColor(context, R.color.seal_matter_state_stamp);
                break;
                //批准或者审核未通过
            case Constants.SealManage.STATE_UN_APPROVED:
            case Constants.SealManage.STATE_UN_REVIEWED:
                color = ContextCompat.getColor(context, R.color.red_dark);
                break;
        }
        return color;
    }
    /**
     * 处理时间格式
     *
     * @param date
     * @return
     */
    public static String formatDate(String date) {
        if (TextUtils.isEmpty(date)) {
            return "暂无";
        }
        if (date.contains("T")) {
            date = date.replace("T", " ");
        }
        if (date.contains(".")) {
            date = date.substring(0, date.lastIndexOf("."));
        }
        return date;
    }

    public static String formatStr(String str){
        if(TextUtils.isEmpty(str)){
            return "暂无";
        }
        return str;
    }
}
