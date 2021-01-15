package com.lessu.xieshi.module.weather;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.scetia.Pro.common.Util.DensityUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.weather.bean.DailyForecast;
import com.lessu.xieshi.module.weather.bean.Hourbean;
import com.lessu.xieshi.module.weather.bean.Tenbean;
import com.lessu.xieshi.module.weather.bean.Tqpicbean;
import com.lessu.xieshi.module.weather.customviews.DailyForecastView;
import com.lessu.xieshi.module.weather.customviews.ScrollViewEx;
import com.lessu.xieshi.module.weather.utils.WeatherUtil;
import com.scetia.Pro.common.Util.GlideUtil;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lessu.xieshi.R.dimen.y210;

public class WeatherDetailActivity extends NavigationActivity {
    @BindView(R.id.ll_tq_backfround)
    ImageView llTqBackfround;
    @BindView(R.id.tv_tq_city)
    TextView tvTqCity;
    @BindView(R.id.iv_weather_back)
    ImageView ivWeatherBack;
    @BindView(R.id.rl_weather_title)
    RelativeLayout rlWeatherTitle;
    @BindView(R.id.tv_tq_weather)
    TextView tvTqWeather;
    @BindView(R.id.tv_tq_temp)
    TextView tvTqTemp;
    @BindView(R.id.tv_tq_wind)
    TextView tvTqWind;
    @BindView(R.id.tv_tq_shidu)
    TextView tvTqShidu;
    @BindView(R.id.tv_tq_today)
    TextView tvTqToday;
    @BindView(R.id.iv_tq_today)
    ImageView ivTqToday;
    @BindView(R.id.tv_tq_today_temp)
    TextView tvTqTodayTemp;
    @BindView(R.id.tv_tq_today_weather)
    TextView tvTqTodayWeather;
    @BindView(R.id.tv_tq_tomo)
    TextView tvTqTomo;
    @BindView(R.id.iv_tq_tomo)
    ImageView ivTqTomo;
    @BindView(R.id.tv_tq_tomo_temp)
    TextView tvTqTomoTemp;
    @BindView(R.id.tv_tq_tomo_weather)
    TextView tvTqTomoWeather;
    @BindView(R.id.ll_tq_shenlanse)
    LinearLayout llTqShenlanse;
    @BindView(R.id.ll_tq_top)
    LinearLayout llTqTop;
    @BindView(R.id.ll_tq_addparent)
    LinearLayout llTqAddparent;
    @BindView(R.id.df_tq)
    DailyForecastView dfTq;
    @BindView(R.id.ll_tq_all)
    LinearLayout llTqAll;
    @BindView(R.id.sc_tq)
    ScrollViewEx scTq;
    @BindView(R.id.weather_detail_refresh)
    SwipeRefreshLayout weatherDetailRefresh;
    private WeatherViewModel viewModel;
    private int weatherHourLineHeight;
    private int screenWidth, screenHeight;
    private int y166height;
    private int y210height;
    private int maxall, minall;
    private int height;
    private int indexm;
    private double m1;
    private double m2;
    private String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tianqi);
        ButterKnife.bind(this);
        screenWidth = (int) DensityUtil.screenWidth(this);
        screenHeight = (int) DensityUtil.screenHeight(this);
        navigationBar.setVisibility(View.GONE);
        initDataListener();
        initView();
        ImmersionBar.with(this).titleBarMarginTop(rlWeatherTitle)
                .navigationBarColor(com.lessu.uikit.R.color.light_gray)
                .navigationBarDarkIcon(true)
                .init();
    }

    @Override
    protected void initImmersionBar() {

    }

    /**
     * 初始化控件
     */
    private void initView() {
        y166height = getResources().getDimensionPixelSize(R.dimen.y166);
        y210height = getResources().getDimensionPixelSize(y210);
        llTqAll.getBackground().setAlpha(0);
        llTqBackfround.getBackground().setAlpha(255);
        llTqShenlanse.getBackground().setAlpha(255);
        ivWeatherBack.setOnClickListener(view -> finish());
        weatherDetailRefresh.setColorSchemeResources(R.color.blue_light1, R.color.blue_normal1, R.color.blue_normal2);
        //设置加载提示按钮缩放
        weatherDetailRefresh.setProgressViewOffset(true, 100, screenHeight / 3);
        weatherDetailRefresh.setOnRefreshListener(() -> {
            viewModel.refresh();
        });

    }
    /**
     * 初始化数据监听，更新UI
     */
    private void initDataListener(){
        viewModel =new  ViewModelProvider(this,new WeatherModelFactory(this.getApplication(),this)).get(WeatherViewModel.class);
        viewModel.getLoadState().observe(this, new Observer<LoadState>() {
            @Override
            public void onChanged(LoadState loadState) {
                switch (loadState){
                    case LOADING:
                        weatherDetailRefresh.setRefreshing(true);
                        break;
                    case SUCCESS:
                    case FAILURE:
                        weatherDetailRefresh.setRefreshing(false);
                        break;
                }
            }
        });
        viewModel.getThrowable().observe(this, new Observer<ExceptionHandle.ResponseThrowable>() {
            @Override
            public void onChanged(ExceptionHandle.ResponseThrowable throwable) {
                ToastUtil.showShort(throwable.message);
            }
        });
        //监听背景图片变换，更新UI
        viewModel.getBackgroundUrl().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                GlideUtil.showImageView(WeatherDetailActivity.this, s, llTqBackfround);
            }
        });
        viewModel.getTenBeanData().observe(this, new Observer<Tenbean>() {
            @Override
            public void onChanged(Tenbean tenbean) {
                showFutureTenDays(tenbean);
            }
        });
        viewModel.getHourBeanData().observe(this, new Observer<Hourbean>() {
            @Override
            public void onChanged(Hourbean hourbean) {
                showToadyHour(hourbean);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            Rect outRect = new Rect();
            getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
            outRect.width();
            outRect.height();
            LinearLayout.LayoutParams lp;
            lp = (LinearLayout.LayoutParams) llTqTop.getLayoutParams();
            lp.height = outRect.height();
            llTqTop.setLayoutParams(lp);
            weatherHourLineHeight = outRect.height() - y166height - y210height;
            LinearLayout.LayoutParams lp1;
            lp1 = (LinearLayout.LayoutParams) dfTq.getLayoutParams();
            lp1.height = weatherHourLineHeight;
            dfTq.setLayoutParams(lp1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        scTq.setOnScrollListener(scrollY -> {
            height = llTqTop.getHeight();
            indexm = height - y166height;
            m1 = scrollY;
            m2 = indexm;
            int backapah = (int) (255 - 255 * m1 / m2);
            int allapha = (int) (255 * m1 / m2);
            if (allapha > 255) {
                allapha = 255;
            }
            if (allapha < 0) {
                allapha = 0;
            }
            if (backapah > 255) {
                backapah = 255;
            }
            if (backapah < 0) {
                backapah = 0;
            }
            llTqAll.getBackground().setAlpha(allapha);
            llTqBackfround.getBackground().setAlpha(backapah);
            llTqShenlanse.getBackground().setAlpha(backapah);
            llTqAddparent.setAlpha((float) (m1 / m2));
            dfTq.setAlpha((float) (m1 / m2));
            if (scTq.getScrollY() <= 0) {
                llTqAll.getBackground().setAlpha(0);
                llTqBackfround.getBackground().setAlpha(255);
                llTqShenlanse.getBackground().setAlpha(255);
            }
            if (scTq.getScrollY() >= indexm) {
                dfTq.setAlpha(1f);
                llTqBackfround.getBackground().setAlpha(0);
                llTqAll.getBackground().setAlpha(255);
                llTqShenlanse.getBackground().setAlpha(0);
            }
        });
    }

    /**
     * 显示未来十天的天气状况
     * @param tenbean
     */
    public void showFutureTenDays(Tenbean tenbean) {
        ArrayList<Integer> almax = new ArrayList<>();
        ArrayList<Integer> almin = new ArrayList<>();
        ArrayList<DailyForecast> forecastList = new ArrayList<>();
        Tenbean.DataBean data = tenbean.getData();
        String city_name = data.getCity_name();
        String report_date = data.getReport_date();
        if (city_name.contains(" ")) {
            String[] split = city_name.split(" ");
            city_name = split[1];
        }
        tvTqCity.setText(city_name);
        Drawable dra = ContextCompat.getDrawable(this, R.drawable.dingwei);
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
        tvTqCity.setCompoundDrawables(dra, null, null, null);
        List<Tenbean.DataBean.ItemsBean> items = data.getItems();
        Tenbean.DataBean.ItemsBean todaybean = items.get(0);
        Tenbean.DataBean.ItemsBean tomobean = items.get(1);
        String todaytemp = todaybean.getTempe();
        String todayweather = todaybean.getWeather();
        Tqpicbean gettodayweather = WeatherUtil.getweather(todayweather);
        String tomotemp = tomobean.getTempe();
        String tomoweather = tomobean.getWeather();
        Tqpicbean gettomoweather = WeatherUtil.getweather(tomoweather);
        if (todaytemp.contains("~")) {
            todaytemp = todaytemp.replace("~", "/");
        }
        if (tomotemp.contains("~")) {
            tomotemp = tomotemp.replace("~", "/");
        }
        tvTqToday.setText("今天");
        tvTqTomo.setText("明天");
        int todayWeatherTextSize = 16;
        int tomoyWeatherTextSize = 16;
        if (gettodayweather.tianqimiaoshu.length() > 10) {
            todayWeatherTextSize = 10;
        } else if (gettodayweather.tianqimiaoshu.length() > 8) {
            tvTqTodayWeather.setTextSize(14);
            todayWeatherTextSize = 14;
        }
        if (gettodayweather.tianqimiaoshu.length() > 4) {
            tvTqTodayWeather.setGravity(Gravity.CENTER);
        } else {
            tvTqTodayWeather.setGravity(Gravity.RIGHT);
        }
        tvTqTodayWeather.setTextSize(todayWeatherTextSize);

        if (gettomoweather.tianqimiaoshu.length() > 10) {
            tomoyWeatherTextSize = 10;
        } else if (gettomoweather.tianqimiaoshu.length() > 8) {
            tomoyWeatherTextSize = 14;
        }
        if (gettomoweather.tianqimiaoshu.length() > 4) {
            tvTqTomoWeather.setGravity(Gravity.CENTER);
        } else {
            tvTqTomoWeather.setGravity(Gravity.RIGHT);
        }
        tvTqTomoWeather.setTextSize(tomoyWeatherTextSize);

        tvTqTodayTemp.setText(todaytemp);
        tvTqTodayWeather.setText(gettodayweather.tianqimiaoshu);
        tvTqTomoTemp.setText(tomotemp);
        tvTqTomoWeather.setText(gettomoweather.tianqimiaoshu);

        GlideUtil.showDrawableResourceId(this,gettodayweather.pic1small,ivTqToday);
        GlideUtil.showDrawableResourceId(this,gettomoweather.pic1small,ivTqTomo);

        for (int i = 0; i < items.size(); i++) {
            Tenbean.DataBean.ItemsBean itemsBean = items.get(i);
            String datatime = itemsBean.getDatatime();
            String direction = itemsBean.getDirection();
            String speed = itemsBean.getSpeed();
            String tempe = itemsBean.getTempe();
            String weather =itemsBean.getWeather();
            String newtemp = tempe.replace("℃", "");
            Tqpicbean getweather = WeatherUtil.getweather(weather);
            String lowtemps;
            String hightemps;
            if (newtemp.contains("~")) {
                String[] split = newtemp.split("~");
                lowtemps = split[0];
                hightemps = split[1];
            } else {
                lowtemps = newtemp;
                hightemps = newtemp;
            }
            int lowtemp = Integer.parseInt(lowtemps);
            int hightemp = Integer.parseInt(hightemps);
            almax.add(hightemp);
            almin.add(lowtemp);
            forecastList.add(new DailyForecast(0.0, -0.0, hightemp, lowtemp, datatime, direction, speed, getweather.pic1small, getweather.pic2small));
        }
        for (int i = 0; i < almax.size(); i++) {
            maxall = maxall + almax.get(i);
        }
        int maxver = maxall / almax.size();//高气温的平均数

        for (int i = 0; i < almin.size(); i++) {
            minall = minall + almin.get(i);
        }
        int minver = minall / almin.size();//低气温的平均数
        for (int i = 0; i < forecastList.size(); i++) {
            DailyForecast dailyForecast = forecastList.get(i);
            int maxper = (dailyForecast.maxtemp - maxver) / 10;
            int minper = (dailyForecast.mintemp - minver) / 10;
            dailyForecast.maxper = maxper;
            dailyForecast.minper = minper;
            System.out.println("forecastList.get(i).maxper" + forecastList.get(i).maxper);
        }
        dfTq.setData(forecastList, weatherHourLineHeight);
    }

    /**
     * 显示当前每个小时的天气状况
     * @param hourbean
     */
    public void showToadyHour(Hourbean hourbean) {
        List<Hourbean.DataBean> data = hourbean.getData();
        Hourbean.DataBean dataBean = data.get(0);
        String wthr = dataBean.getWthr();
        double temp = dataBean.getTemp();
        String dirc = dataBean.getDirc()==null?"":dataBean.getDirc();
        String wind = dataBean.getWind()==null?"":dataBean.getWind();
        double rhrh = dataBean.getRhrh();
        String weatherDir = (dirc + " " + wind).equals(" ") ?"未知":dirc+" "+wind;

        tvTqWeather.setText(wthr);
        tvTqTemp.setText(String.format(getString(R.string.wether_today_now_temp_text),temp));
        tvTqWind.setText(weatherDir);
        tvTqShidu.setText(String.format(getString(R.string.wether_today_now_sd_text),rhrh));
        //由于每天天气数据描述太复杂，用实况天气来描述当天的天气情况 2019-09-20
        //显示今天的天气情况
        Tqpicbean todayWeather = WeatherUtil.getweather(wthr);
        tvTqTodayWeather.setText(wthr);
        Glide.with(this).load(todayWeather==null?"":todayWeather.pic1small).into(ivTqToday);
        for (int i = 0; i < data.size(); i++) {
            View topview = View.inflate(this, R.layout.tianqi_topview, null);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(screenWidth * 10 / 55, y210height);
            TextView tv_tqh_time = topview.findViewById(R.id.tv_tqh_time);
            ImageView iv_tqh_pic1 = topview.findViewById(R.id.iv_tqh_pic1);
            TextView tv_tqh_weather = topview.findViewById(R.id.tv_tqh_weather);
            TextView tv_tqh_temp = topview.findViewById(R.id.tv_tqh_temp);

            String date_time = data.get(i).getDate_time();
            if (date_time.contains(" ")) {
                String[] split = date_time.split(" ");
                time = split[1];
            }
            tv_tqh_time.setText(time);
            tv_tqh_weather.setText(data.get(i).getWthr());
            tv_tqh_temp.setText(data.get(i).getTemp() + "℃");
            Tqpicbean gettomoweather = WeatherUtil.getweather(data.get(i).getWthr());
            if(gettomoweather!=null) {
                GlideUtil.showDrawableResourceId(this, gettomoweather.pic1small, iv_tqh_pic1);
            }
            llTqAddparent.addView(topview, p);
        }
    }
}
