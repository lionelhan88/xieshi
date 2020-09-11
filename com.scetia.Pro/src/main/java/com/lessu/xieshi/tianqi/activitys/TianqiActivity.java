package com.lessu.xieshi.tianqi.activitys;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ImageloaderUtil;
import com.lessu.xieshi.Utils.PicSize;
import com.lessu.xieshi.tianqi.DailyForecast;
import com.lessu.xieshi.tianqi.DailyForecastView;
import com.lessu.xieshi.tianqi.bean.Hourbean;
import com.lessu.xieshi.tianqi.bean.Tenbean;
import com.lessu.xieshi.tianqi.bean.Tqpicbean;
import com.lessu.xieshi.tianqi.contracts.ITianqicontract;
import com.lessu.xieshi.tianqi.customviews.MyScrollview;
import com.lessu.xieshi.tianqi.presenters.TianqiPresenter;
import com.lessu.xieshi.tianqi.utils.ColorShades;
import com.lessu.xieshi.tianqi.utils.Contenttianqi;
import com.lessu.xieshi.tianqi.utils.WeatherUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.lessu.xieshi.R.dimen.y210;

public class TianqiActivity extends NavigationActivity implements ITianqicontract.View{
    private ITianqicontract.Presenter presenter;

    private DailyForecastView df_tq;
    private LinearLayout ll_tq_addparent;
    private LinearLayout ll_tq_top;
    private int screenWidth;
    private int screenHeight;
    private MyScrollview sc_tq;

    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;
    private double latitude;
    private double longitude;
    private String address;
    private String lowtemps;
    private String hightemps;
    private int height;
    private int indexm;
    private double m1;
    private double m2;
    private String time;
    private TextView tv_tq_city;
    private TextView tv_tq_weather;
    private TextView tv_tq_temp;
    private TextView tv_tq_wind;
    private TextView tv_tq_shidu;
    private TextView tv_tq_today;
    private ImageView iv_tq_today;
    private TextView tv_tq_today_temp;
    private TextView tv_tq_today_weather;
    private TextView tv_tq_tomo;
    private ImageView iv_tq_tomo;
    private TextView tv_tq_tomo_temp;
    private TextView tv_tq_tomo_weather;
    private int zhexianheight;
    private int y166height;
    private LinearLayout ll_tq_all;
    private ImageView ll_tq_backfround;
    private LinearLayout ll_tq_shenlanse;
    private int y210height;
    private ColorShades shades;
    private ArrayList<String> qinglist;
    private ArrayList<String> bailist;
    private ArrayList<String> banglist;
    private ArrayList<String> wanlist;
    private DecimalFormat df;
    private String latiformat;
    private String longformat;
    private String gettoken;
    private RelativeLayout rlWeatherTitle;
    private ImageView ivWeatherBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tianqi);
        screenWidth = (int) PicSize.screenWidth(this);
        screenHeight =(int) PicSize.screenHeight(this);
        navigationBar.setVisibility(View.GONE);
        mLocationClient = new LocationClient(getApplicationContext());
        mBDLocationListener = new MyBDLocationListener();
        // 注册监听
        mLocationClient.registerLocationListener(mBDLocationListener);
        initView();
        initData();
        ImmersionBar.with(this).titleBarMarginTop(rlWeatherTitle)
                .navigationBarColor(com.lessu.uikit.R.color.light_gray)
                .navigationBarDarkIcon(true)
                .init();
    }

    @Override
    protected void initImmersionBar() {

    }

    private void initView() {
        initbackgroubdlist();
        y166height = getResources().getDimensionPixelSize(R.dimen.y166);
        y210height = getResources().getDimensionPixelSize(y210);

        tv_tq_city = (TextView) findViewById(R.id.tv_tq_city);

        tv_tq_weather = (TextView) findViewById(R.id.tv_tq_weather);
        tv_tq_temp = (TextView) findViewById(R.id.tv_tq_temp);
        tv_tq_wind = (TextView) findViewById(R.id.tv_tq_wind);
        tv_tq_shidu = (TextView) findViewById(R.id.tv_tq_shidu);
        tv_tq_today = (TextView) findViewById(R.id.tv_tq_today);
        iv_tq_today = (ImageView) findViewById(R.id.iv_tq_today);
        tv_tq_today_temp = (TextView) findViewById(R.id.tv_tq_today_temp);
        tv_tq_today_weather = (TextView) findViewById(R.id.tv_tq_today_weather);
        tv_tq_tomo = (TextView) findViewById(R.id.tv_tq_tomo);
        iv_tq_tomo = (ImageView) findViewById(R.id.iv_tq_tomo);
        tv_tq_tomo_temp = (TextView) findViewById(R.id.tv_tq_tomo_temp);
        tv_tq_tomo_weather = (TextView) findViewById(R.id.tv_tq_tomo_weather);


        ll_tq_shenlanse = (LinearLayout) findViewById(R.id.ll_tq_shenlanse);
        ll_tq_backfround = (ImageView) findViewById(R.id.ll_tq_backfround);
        ll_tq_all = (LinearLayout) findViewById(R.id.ll_tq_all);
        df_tq = (DailyForecastView) findViewById(R.id.df_tq);
        ll_tq_top = (LinearLayout) findViewById(R.id.ll_tq_top);
        sc_tq = (MyScrollview) findViewById(R.id.sc_tq);
        ll_tq_addparent = (LinearLayout) findViewById(R.id.ll_tq_addparent);

        ll_tq_all.getBackground().setAlpha(0);
        ll_tq_backfround.getBackground().setAlpha(255);
        ll_tq_shenlanse.getBackground().setAlpha(255);
        ivWeatherBack = (ImageView) findViewById(R.id.iv_weather_back);
        ivWeatherBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rlWeatherTitle = (RelativeLayout) findViewById(R.id.rl_weather_title);
    }

    private void initbackgroubdlist() {
        String baseqin="http://www.scetia.com/Scetia.AutoUpdate/weather/安卓/清晨/qin";
        String basebai="http://www.scetia.com/Scetia.AutoUpdate/weather/安卓/白天/bai";
        String basebang="http://www.scetia.com/Scetia.AutoUpdate/weather/安卓/傍晚/bang";
        String basewan="http://www.scetia.com/Scetia.AutoUpdate/weather/安卓/晚上/wan";
        qinglist = new ArrayList<>();
        bailist = new ArrayList<>();
        banglist = new ArrayList<>();
        wanlist = new ArrayList<>();
        qinglist.add(baseqin+"a.png");
        qinglist.add(baseqin+"b.png");
        qinglist.add(baseqin+"c.png");
        qinglist.add(baseqin+"d.png");
        qinglist.add(baseqin+"e.png");
        qinglist.add(baseqin+"f.png");
        qinglist.add(baseqin+"g.png");
        qinglist.add(baseqin+"h.png");
        qinglist.add(baseqin+"i.png");
        qinglist.add(baseqin+"j.png");
        qinglist.add(baseqin+"k.png");

        bailist.add(basebai+"a.png");
        bailist.add(basebai+"b.png");
        bailist.add(basebai+"c.png");
        bailist.add(basebai+"d.png");
        bailist.add(basebai+"e.png");
        bailist.add(basebai+"f.png");
        bailist.add(basebai+"g.png");
        bailist.add(basebai+"h.png");
        bailist.add(basebai+"i.png");
        bailist.add(basebai+"j.png");
        bailist.add(basebai+"k.png");


        banglist.add(basebang+"a.png");
        banglist.add(basebang+"b.png");
        banglist.add(basebang+"c.png");
        banglist.add(basebang+"d.png");
        banglist.add(basebang+"e.png");
        banglist.add(basebang+"f.png");
        banglist.add(basebang+"g.png");
        banglist.add(basebang+"h.png");
        banglist.add(basebang+"i.png");
        banglist.add(basebang+"j.png");
        banglist.add(basebang+"k.png");


        wanlist.add(basewan+"a.png");
        wanlist.add(basewan+"b.png");
        wanlist.add(basewan+"c.png");
        wanlist.add(basewan+"d.png");
        wanlist.add(basewan+"e.png");
        wanlist.add(basewan+"f.png");
        wanlist.add(basewan+"g.png");
        wanlist.add(basewan+"h.png");
        wanlist.add(basewan+"i.png");
        wanlist.add(basewan+"j.png");
        wanlist.add(basewan+"k.png");
    }

    private void setbackground(int minuteOfDay) {
        //根据天气或时间来设置背景
        String url="";
        if (minuteOfDay > 5 * 60 && minuteOfDay <= 7 * 60) {
            //清晨
            System.out.println("清晨");
            url=qinglist.get(new Random().nextInt(qinglist.size() - 1));
        } else if (minuteOfDay > 7 * 60 && minuteOfDay <= 17 * 60) {
            //白天
            System.out.println("白天");
            url=bailist.get(new Random().nextInt(bailist.size() - 1));
        }else if (minuteOfDay > 17 * 60 && minuteOfDay <= 19 * 60) {
            //傍晚
            System.out.println("傍晚");
            url=banglist.get(new Random().nextInt(banglist.size() - 1));
        }else{
            //晚上
            System.out.println("晚上");
            url=wanlist.get(new Random().nextInt(wanlist.size() - 1));
        }

        System.out.println("url......"+url);
        ImageLoader.getInstance().displayImage(url, ll_tq_backfround, ImageloaderUtil.imageconfigtianqi());
      /*  new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                System.out.println("IMAGE--------onLoadingStarted");
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                System.out.println("IMAGE--------onLoadingFailed");
                System.out.println("IMAGE--------onLoadingFailed"+s+"||||||||"+failReason.getCause());
                System.out.println("IMAGE--------onLoadingFailed"+failReason.getType());
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                System.out.println("IMAGE--------onLoadingComplete");
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                System.out.println("IMAGE--------onLoadingCancelled");
            }
        });*/
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            Rect outRect = new Rect();
            getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
            outRect.width() ;
            outRect.height();
            LinearLayout.LayoutParams lp;
            lp= (LinearLayout.LayoutParams) ll_tq_top.getLayoutParams();
            lp.height=outRect.height();
            ll_tq_top.setLayoutParams(lp);

            zhexianheight = outRect.height() - y166height - y210height;
            LinearLayout.LayoutParams lp1;
            lp1= (LinearLayout.LayoutParams) df_tq.getLayoutParams();
            lp1.height=zhexianheight;
            df_tq.setLayoutParams(lp1);
        }
    }

    private void initData() {
        getLocation();
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        System.out.println("min./....."+minuteOfDay);

        setbackground(minuteOfDay);


        presenter= new TianqiPresenter(this,this);
        gettoken = Contenttianqi.gettoken();
        presenter.getten(gettoken);
        df = new DecimalFormat("######0.00");
       // presenter.gethour(gettoken, longformat, latiformat);
        //presenter.gethour(gettoken, df.format(longitude), df.format(latitude));
        //presenter.gethour(gettoken,Double.toString(121.46),Double.toString(31.19));
    }

    @Override
    protected void onStart() {
        super.onStart();
        sc_tq.setOnScrollListener(new MyScrollview.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                height = ll_tq_top.getHeight();
                indexm = height-y166height;
                m1 = scrollY;
                m2 = indexm;
                System.out.println("height....."+height);
                System.out.println("indexm....."+indexm);
                System.out.println("scrollY...."+scrollY);
                int backapah = (int) (255 - 255 * m1 / m2);
                int allapha = (int) (255 * m1 / m2);
                System.out.println("backapah........."+backapah);

                System.out.println("allapha........."+allapha);

                if(allapha>255){
                    allapha=255;
                }
                if(allapha<0){
                    allapha=0;
                }
                if(backapah>255){
                    backapah=255;
                }
                if(backapah<0){
                    backapah=0;
                }
                ll_tq_all.getBackground().setAlpha(allapha);
                ll_tq_backfround.getBackground().setAlpha(backapah);
                ll_tq_shenlanse.getBackground().setAlpha(backapah);
                ll_tq_addparent.setAlpha((float)(m1 / m2));
                df_tq.setAlpha((float)(m1 / m2));
                System.out.println("sc_tq.getScrollY()........."+sc_tq.getScrollY());
                if(sc_tq.getScrollY()<=0){
                    ll_tq_all.getBackground().setAlpha(0);
                    ll_tq_backfround.getBackground().setAlpha(255);
                    ll_tq_shenlanse.getBackground().setAlpha(255);
                }
                if(sc_tq.getScrollY()>= indexm){
                    df_tq.setAlpha(1f);
                    ll_tq_backfround.getBackground().setAlpha(0);
                    ll_tq_all.getBackground().setAlpha(255);
                    ll_tq_shenlanse.getBackground().setAlpha(0);
                }

            }
        });
    }

    private int maxall;
    private int minall;
    @Override
    public void gettencall(boolean issucess, Tenbean tenbean) {
        if(issucess){
            ArrayList<Integer> almax=new ArrayList<>();
            ArrayList<Integer> almin=new ArrayList<>();
            ArrayList<DailyForecast> forecastList=new ArrayList<>();
            Tenbean.DataBean data = tenbean.getData();
            String city_name = data.getCity_name();
            String report_date = data.getReport_date();
            if(city_name.contains(" ")){
                String[] split = city_name.split(" ");
                city_name = split[1];
            }
            tv_tq_city.setText(city_name);
            Drawable dra= getResources().getDrawable(R.drawable.dingwei);
            dra.setBounds( 0, 0, dra.getMinimumWidth(),dra.getMinimumHeight());
            tv_tq_city.setCompoundDrawables(dra, null, null, null);
            List<Tenbean.DataBean.ItemsBean> items = data.getItems();
            Tenbean.DataBean.ItemsBean todaybean = items.get(0);
            Tenbean.DataBean.ItemsBean tomobean = items.get(1);
            String todaytemp = todaybean.getTempe();
            String todayweather = todaybean.getWeather();
            Tqpicbean gettodayweather = WeatherUtil.getweather(todayweather);
            String tomotemp = tomobean.getTempe();
            String tomoweather = tomobean.getWeather();
            Tqpicbean gettomoweather = WeatherUtil.getweather(tomoweather);
            if( todaytemp.contains("~")){
                todaytemp= todaytemp.replace("~", "/");
            }
            if( tomotemp.contains("~")){
                tomotemp= tomotemp.replace("~", "/");
            }
            tv_tq_today.setText("今天");
            tv_tq_tomo.setText("明天");
            if(gettodayweather.tianqimiaoshu.length()>10){
                tv_tq_today_weather.setTextSize(10);
            }else if(gettodayweather.tianqimiaoshu.length()>8){
                tv_tq_today_weather.setTextSize(14);
            }else{
                tv_tq_today_weather.setTextSize(16);
            }

            if(gettodayweather.tianqimiaoshu.length()>4){
                tv_tq_today_weather.setGravity(Gravity.CENTER);
            }else{
                tv_tq_today_weather.setGravity(Gravity.RIGHT);
            }


            if(gettomoweather.tianqimiaoshu.length()>4){
                tv_tq_tomo_weather.setGravity(Gravity.CENTER);
            }else{
                tv_tq_tomo_weather.setGravity(Gravity.RIGHT);
            }


            if(gettomoweather.tianqimiaoshu.length()>10){
                tv_tq_tomo_weather.setTextSize(10);
            }else if(gettomoweather.tianqimiaoshu.length()>8){
                tv_tq_tomo_weather.setTextSize(14);
            }else{
                tv_tq_tomo_weather.setTextSize(16);
            }
            tv_tq_today_temp.setText(todaytemp);
            tv_tq_today_weather.setText(gettodayweather.tianqimiaoshu);
            tv_tq_tomo_temp.setText(tomotemp);
            tv_tq_tomo_weather.setText(gettomoweather.tianqimiaoshu);

            String uritoday = "drawable://"+gettodayweather.pic1small;
            String uritomo = "drawable://"+gettomoweather.pic1small;
            System.out.println("uritoday........."+uritoday);
            ImageLoader.getInstance().displayImage(uritoday, iv_tq_today);
            ImageLoader.getInstance().displayImage(uritomo, iv_tq_tomo);

            for (int i = 0; i < items.size(); i++) {
                String datatime = items.get(i).getDatatime();
                String direction = items.get(i).getDirection();
                String speed = items.get(i).getSpeed();
                String tempe = items.get(i).getTempe();
                String weather = items.get(i).getWeather();
                String newtemp = tempe.replace("℃", "");

                Tqpicbean getweather = WeatherUtil.getweather(weather);
                if( newtemp.contains("~")){
                    String[] split = newtemp.split("~");
                    lowtemps = split[0];
                    hightemps = split[1];
                }else{
                    lowtemps = newtemp;
                    hightemps = newtemp;
                }
                int lowtemp = Integer.parseInt(lowtemps);
                int hightemp = Integer.parseInt(hightemps);
                almax.add(hightemp);
                almin.add(lowtemp);
                forecastList.add(new DailyForecast(0.0,-0.0,hightemp,lowtemp,datatime,direction,speed,getweather.pic1small,getweather.pic2small));
            }
            for (int i = 0; i < almax.size(); i++) {
                maxall=maxall+almax.get(i);
            }
            int maxver = maxall / almax.size();//高气温的平均数

            for (int i = 0; i < almin.size(); i++) {
                minall=minall+almin.get(i);
            }
            int minver = minall / almin.size();//低气温的平均数
            for (int i = 0; i < forecastList.size(); i++) {
                DailyForecast dailyForecast = forecastList.get(i);
                int maxper = (dailyForecast.maxtemp - maxver) / 10;
                int minper = (dailyForecast.mintemp - minver) / 10;
                dailyForecast.maxper=maxper;
                dailyForecast.minper=minper;
                System.out.println("forecastList.get(i).maxper"+forecastList.get(i).maxper);
            }
            df_tq.setData(forecastList,zhexianheight);
        }else{
            Toast.makeText(TianqiActivity.this,"未能获取到天气信息",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void gethourcall(boolean issucess, Hourbean hourbean) {
        if(issucess){
            List<Hourbean.DataBean> data = hourbean.getData();
            Hourbean.DataBean dataBean = data.get(0);
            String wthr = dataBean.getWthr();
            double temp = dataBean.getTemp();
            String dirc = dataBean.getDirc();
            String wind = dataBean.getWind();
            double rhrh = dataBean.getRhrh();
            if(dirc==null||wind!=null) {
                tv_tq_wind.setVisibility(View.INVISIBLE);
            }
            if(rhrh==0.0) {
                tv_tq_shidu.setVisibility(View.INVISIBLE);
            }
            tv_tq_weather.setText(wthr);
            tv_tq_temp.setText(temp+"℃");
            tv_tq_wind.setText(dirc + " " + wind);
            tv_tq_shidu.setText(rhrh + "%");

            Drawable fengxiang= getResources().getDrawable(R.drawable.fengxiang);
            fengxiang.setBounds( 0, 0, fengxiang.getMinimumWidth(),fengxiang.getMinimumHeight());
            tv_tq_wind.setCompoundDrawables(fengxiang, null, null, null);

            Drawable shidu= getResources().getDrawable(R.drawable.shidu);
            shidu.setBounds( 0, 0, shidu.getMinimumWidth(),shidu.getMinimumHeight());
            tv_tq_shidu.setCompoundDrawables(shidu, null, null, null);
            //由于每天天气数据描述太复杂，用实况天气来描述当天的天气情况 2019-09-20
            //显示今天的天气情况
            Tqpicbean todayweather = WeatherUtil.getweather(wthr);
            String uritoday = "drawable://"+todayweather.pic1small;
            tv_tq_today_weather.setText(wthr);
            ImageLoader.getInstance().displayImage(uritoday, iv_tq_today);
            for (int i = 0; i < data.size(); i++) {
                View topview = View.inflate(this, R.layout.tianqi_topview, null);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                        screenWidth*10/55,y210height
                );
                TextView tv_tqh_time = (TextView) topview.findViewById(R.id.tv_tqh_time);
                ImageView iv_tqh_pic1 = (ImageView) topview.findViewById(R.id.iv_tqh_pic1);
                TextView tv_tqh_weather = (TextView) topview.findViewById(R.id.tv_tqh_weather);
                TextView tv_tqh_temp =(TextView) topview.findViewById(R.id.tv_tqh_temp);

                String date_time = data.get(i).getDate_time();
                if(date_time.contains(" ")){
                    String[] split = date_time.split(" ");
                    time = split[1];
                }
                tv_tqh_time.setText(time);
                //iv_tqh_pic1.set
                tv_tqh_weather.setText(data.get(i).getWthr());
                tv_tqh_temp.setText(data.get(i).getTemp()+"℃");
                Tqpicbean gettomoweather = WeatherUtil.getweather(data.get(i).getWthr());
                String uri = "drawable://"+gettomoweather.pic1small;
                ImageLoader.getInstance().displayImage(uri, iv_tqh_pic1);
                ll_tq_addparent.addView(topview,p);
            }
        }else{
           // Toast.makeText(TianqiActivity.this,"未能获取到天气信息",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(ITianqicontract.Presenter presenter) {
        this.presenter=presenter;
    }



    /** 获得所在位置经纬度及详细地址 */
    public void getLocation() {
        // 声明定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(5000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        // 设置定位参数
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 取消监听函数
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
        }
    }

    private class MyBDLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 非空判断
            if (location != null) {
                // 根据BDLocation 对象获得经纬度以及详细地址信息
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                latiformat = df.format(latitude);
                longformat = df.format(longitude);
                address = location.getAddrStr();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.gethour(Contenttianqi.gettoken(), longformat, latiformat);
                    }
                });
//                tv_tq.setText("address:" + address + " latitude:" + latitude
//                        + " longitude:" + longitude + "---");
                System.out.println( "address:" + address + " latitude:" + latitude
                        + " longitude:" + longitude + "---");
                if (mLocationClient.isStarted()) {
                    // 获得位置之后停止定位
                    mLocationClient.stop();
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }



}
