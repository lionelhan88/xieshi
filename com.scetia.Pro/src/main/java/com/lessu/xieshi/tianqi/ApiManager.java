package com.lessu.xieshi.tianqi;

import android.annotation.SuppressLint;

import java.util.Calendar;

//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.util.EntityUtils;

@SuppressLint("SimpleDateFormat") @SuppressWarnings("deprecation")
public class ApiManager {

	/**
	 * 转换日期2015-11-05为今天、明天、昨天，或者是星期几
	 *
	 * @param date
	 * @return
	 */
	public static String prettyDate(String date) {
		try {
			final String[] strs = date.split("-");
			final int year = Integer.valueOf(strs[0]);
			final int month = Integer.valueOf(strs[1]);
			final int day = Integer.valueOf(strs[2]);
			Calendar c = Calendar.getInstance();
			int curYear = c.get(Calendar.YEAR);
			int curMonth = c.get(Calendar.MONTH) + 1;// Java月份从0月开始
			int curDay = c.get(Calendar.DAY_OF_MONTH);
			if (curYear == year && curMonth == month) {
				if (curDay == day) {
					return "今天";
				} else if ((curDay + 1) == day) {
					return "明天";
				} else if ((curDay - 1) == day) {
					return "昨天";
				}
			}
			c.set(year, month - 1, day);
			// http://www.tuicool.com/articles/Avqauq
			// 一周第一天是否为星期天
			boolean isFirstSunday = (c.getFirstDayOfWeek() == Calendar.SUNDAY);
			// 获取周几
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			// 若一周第一天为星期天，则-1
			if (isFirstSunday) {
				dayOfWeek = dayOfWeek - 1;
				if (dayOfWeek == 0) {
					dayOfWeek = 7;
				}
			}
			// 打印周几
			// System.out.println(weekDay);

			// 若当天为2014年10月13日（星期一），则打印输出：1
			// 若当天为2014年10月17日（星期五），则打印输出：5
			// 若当天为2014年10月19日（星期日），则打印输出：7
			switch (dayOfWeek) {
			case 1:
				return "周一";
			case 2:
				return "周二";
			case 3:
				return "周三";
			case 4:
				return "周四";
			case 5:
				return "周五";
			case 6:
				return "周六";
			case 7:
				return "周日";
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}



}
