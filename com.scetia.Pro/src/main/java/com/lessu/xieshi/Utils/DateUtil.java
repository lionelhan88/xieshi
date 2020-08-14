package com.lessu.xieshi.Utils;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static SimpleDateFormat sdf =new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static SimpleDateFormat sdfDate =new  SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public static void datePicker(Context context,OnTimeSelectListener timeSelectListener){
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(context,timeSelectListener).build();
        // pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    public static String getTime(Date date){
        return sdf.format(date);
    }

    public static String getDate(Date date){
        return sdfDate.format(date);
    }
    public static String getDayAgo(int ago){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,ago);
        return sdfDate.format(calendar.getTime());
    }


}
