package com.scetia.Pro.common.Util;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DateUtil {
    public static SimpleDateFormat sdfYearDate =new  SimpleDateFormat("yyyy", Locale.CHINA);
    public static SimpleDateFormat sdfDate =new  SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static SimpleDateFormat xsDate = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss",Locale.CHINA);
    /**
     * 时间选择器
     * @param context 当前上下文
     * @param timeSelectListener 选择日期的回调监听事件
     */
    public static void datePicker(Context context,OnTimeSelectListener timeSelectListener){
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(context,timeSelectListener)
                .setLineSpacingMultiplier(2.0f)
                .build();
        pvTime.show();
    }

    /**
     * 底部滑动式选择菜单
     * @param context 当前上下文
     * @param items 传入的菜单内从数组
     * @param listener 选择监听回调
     */
    public static <T> void itemMenuPicker(Context context, List<T> items,OnOptionsSelectListener listener){
        OptionsPickerView<T> build = new OptionsPickerBuilder(context,listener).build();
        build.setPicker(items);
        build.show();
    }

    public static String pictureDate(Date date){
        return xsDate.format(date);
    }

    /**
     * 格式化当前日期
     * @param date 当前需要格式化的date对象
     * @return
     */
    public static String getDate(Date date){
        return sdfDate.format(date);
    }

    /**
     * 获取当前日期之前 几天的日期
     * @param ago 之前的天数
     * @return
     */
    public static String getDayAgo(int ago){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,ago);
        return sdfDate.format(calendar.getTime());
    }

    /**
     * 获取当前日期之前的几个月
     * @return
     */
    public static String getMonthAgo(int monthAgo){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,monthAgo);
        return sdfDate.format(calendar.getTime());
    }


    /**
     * 计算两个日期之间的间隔天数
     * @param startDate 开始日期 Date
     * @param endDate 截止日期 Date
     * @return
     */
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 计算两个日期之间的间隔天数
     * @param beginTime 开始日期 String
     * @param endTime   截止日期 String
     * @return
     */
    public static double getGapCount(String beginTime, String endTime) {
        String str1 = beginTime;  //"yyyyMMdd"格式 如 20131022
        String str2 = endTime;  //"yyyyMMdd"格式 如 20131022
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");//输入日期的格式
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);//从间隔毫秒变成间隔天数
        dayCount++;
        System.out.println("\n相差" + dayCount + "天");
        return dayCount;
    }
}
