package com.lessu.xieshi.tianqi.utils;

import com.lessu.xieshi.R;
import com.lessu.xieshi.tianqi.bean.IandWbean;
import com.lessu.xieshi.tianqi.bean.Tqpicbean;
import com.lessu.xieshi.tianqi.bean.Weatherbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by fhm on 2017/11/6.
 */

public  class WeatherUtil {
    static ArrayList<Weatherbean> al=new ArrayList();
    private static ArrayList<IandWbean> iandwal= new ArrayList();
    private static ArrayList<String> tianqilistlist= new ArrayList();
    private static ArrayList<Weatherbean> endindokweatherlist= new ArrayList();
    private static Comparator<IandWbean> comparator;
    private static String yuanshiweather;
    private static String okweather;
    private static long gjzindex=0;
    private static String daiti;

    private static Comparator<Weatherbean> okweathercompare;

    //有 有时有 有零星 有短时 或 转 到
    public static Tqpicbean getweather(String weatherder){
        gjzindex=0;
        endindokweatherlist.clear();
        okweather=null;
        al.clear();
        iandwal.clear();
        tianqilistlist.clear();
        yuanshiweather = weatherder;
        inital();
        System.out.println("al............."+al.size());
        for (int i = 0; i < al.size(); i++) {
            String weather = al.get(i).weather;
            if( weatherder.contains(weather)){
                daiti="";
                int index = weatherder.indexOf(weather);
                System.out.println("index......."+index);
                String substring = weatherder.substring(index);
                System.out.println("subString/................."+substring);
                int length = weather.length();
                for (int j = 0; j < length; j++) {
                    daiti = daiti+" ";
                }
                weatherder= weatherder.replace(weather, daiti);
                iandwal.add(new IandWbean(index,weather));
            }
        }
        Collections.sort(iandwal,comparator);
        okweather=iandwal.get(0).weather;
        for (int i = 0; i < iandwal.size(); i++) {
            System.out.println("a.............a.............."+iandwal.get(i).index+iandwal.get(i).weather);
                if(i<iandwal.size()-1) {
                String laststring = iandwal.get(i).weather;
                String endstring = iandwal.get(i+1).weather;

                int startindex = iandwal.get(i).index + laststring.length();
                int endindex = iandwal.get(i + 1).index;
                String isgjz = yuanshiweather.substring(startindex, endindex);
                System.out.println("isgjz..." + isgjz);
                //如果这些中间的字符串是那些指定的关键字
                    //tianqilistlist.contains(isgjz)
                if(tianqilistlist.contains(isgjz)){
                    okweather=okweather+isgjz+endstring;
                    gjzindex++;
                }else {
                    if (gjzindex == 0) {
                        okweather=endstring;
                    }else{
                        System.out.println("elseokweather............"+okweather);

                        return endingokweather(okweather);
                    }
                }

            }
//// al.............28
//            01-26 09:12:11.544 10018-10018/com.scetia.Pro I/System.out: index.......6
//            01-26 09:12:11.544 10018-10018/com.scetia.Pro I/System.out: subString/.................小雪转阴天
//            01-26 09:12:11.544 10018-10018/com.scetia.Pro I/System.out: index.......0
//            01-26 09:12:11.544 10018-10018/com.scetia.Pro I/System.out: subString/.................阴局部地区有  转阴天
//            01-26 09:12:11.544 10018-10018/com.scetia.Pro I/System.out: a.............a..............0阴
//            01-26 09:12:11.544 10018-10018/com.scetia.Pro I/System.out: isgjz...局部地区有
//            01-26 09:12:11.544 10018-10018/com.scetia.Pro I/System.out: a.............a..............6小雪
//            01-26 09:12:11.544 10018-10018/com.scetia.Pro I/System.out: okweather............小雪
        }
        System.out.println("okweather............"+okweather);
        return endingokweather(okweather);
    }

    private static Tqpicbean endingokweather(String okweather) {
        for (int j = 0; j < al.size(); j++) {
            if(okweather.contains(al.get(j).weather)) {
                endindokweatherlist.add(al.get(j));
            }
        }
        if(endindokweatherlist.size()==1){
            return  new Tqpicbean(okweather,endindokweatherlist.get(0).weathersmall,endindokweatherlist.get(0).weatherbig,endindokweatherlist.get(0).weathersmall,endindokweatherlist.get(0).weatherbig);
        }else if(endindokweatherlist.size()==2){
            return  new Tqpicbean(okweather,endindokweatherlist.get(0).weathersmall,endindokweatherlist.get(0).weatherbig,endindokweatherlist.get(1).weathersmall,endindokweatherlist.get(1).weatherbig);
        }else if(endindokweatherlist.size()>=3){
            Collections.sort(endindokweatherlist,okweathercompare);

            System.out.println(endindokweatherlist.get(0).weather+"-----------------------"+endindokweatherlist.get(1).weather);
            return new Tqpicbean(okweather,endindokweatherlist.get(0).weathersmall,endindokweatherlist.get(0).weatherbig,endindokweatherlist.get(1).weathersmall,endindokweatherlist.get(1).weatherbig);
        }else{
            return null;
        }
    }

    private static void inital() {
        //初始化comparator
        comparator = new Comparator<IandWbean>(){
            public int compare(IandWbean iandw1, IandWbean iandw2) {
                if(iandw1.index!=iandw2.index){
                    return iandw1.index-iandw2.index;
                }
                else{
                    if(!iandw1.weather.equals(iandw2.weather)){
                        return iandw1.weather.compareTo(iandw2.weather);
                    }
                    else{
                        return 0;
                    }
                }
            }
        };

        okweathercompare=new Comparator<Weatherbean>() {
            @Override
            public int compare(Weatherbean weatherbean, Weatherbean t1) {
                if (weatherbean.dengji != t1.dengji) {
                    return weatherbean.dengji - t1.dengji;
                } else {
                    if (!weatherbean.weather.equals(t1.weather)) {
                        return t1.weather.compareTo(weatherbean.weather);
                    } else {
                        return 0;
                    }
                }
            }
        };

//初始化天气list
        al.add(new Weatherbean("强沙尘暴",1, R.drawable.qiangshachenbaox,R.drawable.qiangshachenbaod));
        al.add(new Weatherbean("扬沙",2,R.drawable.yangshax,R.drawable.yangshad));
        al.add(new Weatherbean("浮尘",3,R.drawable.fuchenx,R.drawable.fuchend));
        al.add(new Weatherbean("雾",4,R.drawable.wux,R.drawable.wud));
        al.add(new Weatherbean("沙尘暴",5,R.drawable.shachenbaox,R.drawable.shachenbaod));
        al.add(new Weatherbean("冰雹",6,R.drawable.bingbaox,R.drawable.bingbaod));
        al.add(new Weatherbean("特大暴雨",7,R.drawable.tedabaoyux,R.drawable.tedabaoyud));
        al.add(new Weatherbean("大暴雨",8,R.drawable.dabaoyux,R.drawable.dabaoyud));
        al.add(new Weatherbean("暴雪",9,R.drawable.baoxuex,R.drawable.baoxued));
        al.add(new Weatherbean("暴雨",10,R.drawable.baoyux,R.drawable.baoyud));
        al.add(new Weatherbean("大雪",11,R.drawable.daxuex,R.drawable.daxued));
        al.add(new Weatherbean("大雨",12,R.drawable.dayux,R.drawable.dayud));
        al.add(new Weatherbean("小雨夹雪",13,R.drawable.yujiaxuex,R.drawable.yujiaxued));
        al.add(new Weatherbean("雨夹雪",14,R.drawable.yujiaxuex,R.drawable.yujiaxued));
        al.add(new Weatherbean("雷阵雨",15,R.drawable.leizhenyux,R.drawable.leizhenyud));
        al.add(new Weatherbean("中雪",16,R.drawable.zhongxuex,R.drawable.zhongxued));
        al.add(new Weatherbean("中雨",17,R.drawable.zhongyux,R.drawable.zhongyud));
        al.add(new Weatherbean("阵雪",18,R.drawable.zhenxuex,R.drawable.zhenxued));
        al.add(new Weatherbean("雷雨",19,R.drawable.leiyux,R.drawable.leiyud));
        al.add(new Weatherbean("阵雨",20,R.drawable.zhenyux,R.drawable.zhenyud));
        al.add(new Weatherbean("小雪",21,R.drawable.xiaoxuex,R.drawable.xiaoxued));
        al.add(new Weatherbean("小雨",22,R.drawable.xiaoyux,R.drawable.xiaoyud));
        al.add(new Weatherbean("阴",23,R.drawable.yinx,R.drawable.yind));
        al.add(new Weatherbean("多云",24,R.drawable.duoyunx,R.drawable.duoyund));
        al.add(new Weatherbean("晴",25,R.drawable.qingx,R.drawable.qingd));
        al.add(new Weatherbean("雨",26,R.drawable.xiaoyux,R.drawable.xiaoyud));
        al.add(new Weatherbean("雪",27,R.drawable.xiaoxuex,R.drawable.xiaoxued));
        al.add(new Weatherbean("冻雨",28,R.drawable.dongyux,R.drawable.dongyud));
        al.add(new Weatherbean("霾",29,R.drawable.maix,R.drawable.maid));



        //有 有时有 有零星 有短时 或 转 到
        //初始化关键字list
        tianqilistlist.add("有");
        tianqilistlist.add("有时有");
        tianqilistlist.add("有零星");
        tianqilistlist.add("有短时");
        tianqilistlist.add("或");
        tianqilistlist.add("转");
        tianqilistlist.add("到");
    }
    private static boolean isHasKey(String msg){
        for (String s:tianqilistlist){
            if (msg.contains(s))
                return true;
        }
        return false;
    }
}
