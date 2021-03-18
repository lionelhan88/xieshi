package com.lessu.xieshi.module.weather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.scetia.Pro.baseapp.basepage.BaseViewModel;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.lib_map.BaiduMapLifecycle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.weather.bean.Hourbean;
import com.lessu.xieshi.module.weather.bean.Tenbean;
import com.lessu.xieshi.module.weather.utils.Contenttianqi;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by fhm on 2017/10/26.
 */
public class WeatherViewModel extends BaseViewModel {
    private WeatherRepository repository = new WeatherRepository();
    private BaiduMapLifecycle baiduMapLifecycle;
    private static final String BASE_QIN_URL = "http://www.scetia.com/Scetia.AutoUpdate/weather/安卓/清晨/qin";
    private static final String BASE_BAI_URL = "http://www.scetia.com/Scetia.AutoUpdate/weather/安卓/白天/bai";
    private static final String BASE_BANG_URL = "http://www.scetia.com/Scetia.AutoUpdate/weather/安卓/傍晚/bang";
    private static final String BASE_WAN_URL = "http://www.scetia.com/Scetia.AutoUpdate/weather/安卓/晚上/wan";
    private static final String[] A_K = {"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k"};


    private MutableLiveData<Tenbean> tenBeanData = new MutableLiveData<>();
    private MutableLiveData<Hourbean> hourBeanData = new MutableLiveData<>();
    private MutableLiveData<String> backgroundUrl = new MutableLiveData<>();

    public WeatherViewModel(@NonNull Application application, LifecycleOwner owner) {
        super(application);
        baiduMapLifecycle = new BaiduMapLifecycle(application);
        owner.getLifecycle().addObserver(this);
        owner.getLifecycle().addObserver(baiduMapLifecycle);
    }

    public MutableLiveData<Tenbean> getTenBeanData() {
        return tenBeanData;
    }

    public MutableLiveData<String> getBackgroundUrl() {
        return backgroundUrl;
    }

    public MutableLiveData<Hourbean> getHourBeanData() {
        return hourBeanData;
    }


    @Override
    public void onCreate() {
        //一些初始化操作
        baiduMapLifecycle.setBdLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                // 非空判断
                if (bdLocation != null) {
                    // 根据BDLocation 对象获得经纬度以及详细地址信息
                    double latitude = bdLocation.getLatitude();
                    double longitude = bdLocation.getLongitude();
                    String address = bdLocation.getAddrStr();
                    LogUtil.showLogE(latitude + longitude + address);
                    getToadyHour(Constants.User.GET_TOKEN(), String.valueOf(longitude), String.valueOf(latitude));
                }
                baiduMapLifecycle.stopLocation();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
    }

    @Override
    public void onStart() {
        loadData();
    }

    public void refresh() {
        baiduMapLifecycle.startLocation();
        loadData();
    }

    /**
     * 根据时间设置当前背景图片
     */
    private void setBackground() {
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        //根据天气或时间来设置背景
        String url = "";
        if (minuteOfDay > 5 * 60 && minuteOfDay <= 7 * 60) {
            //清晨
            url = BASE_QIN_URL + A_K[new Random().nextInt(A_K.length - 1)] + ".png";
        } else if (minuteOfDay > 7 * 60 && minuteOfDay <= 17 * 60) {
            //白天
            url = BASE_BAI_URL + A_K[new Random().nextInt(A_K.length - 1)] + ".png";
        } else if (minuteOfDay > 17 * 60 && minuteOfDay <= 19 * 60) {
            //傍晚
            url = BASE_BANG_URL + A_K[new Random().nextInt(A_K.length - 1)] + ".png";
        } else {
            //晚上
            url = BASE_WAN_URL + A_K[new Random().nextInt(A_K.length - 1)] + ".png";
        }
        backgroundUrl.postValue(url);
    }

    /**
     * 加载数据
     */
    public void loadData() {
        getFutureTenDays(Constants.User.GET_TOKEN());
        loadState.postValue(LoadState.LOADING);
    }

    public void getFutureTenDays(String token) {
        repository.getFutureTenDays(token, new ResponseObserver<Tenbean>() {
            @Override
            public void success(Tenbean tenbean) {
                setBackground();
                tenBeanData.postValue(tenbean);
                loadState.postValue(LoadState.SUCCESS);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE.setMessage("天气数据获取失败"));
            }
        });
    }

    public void getToadyHour(String token, String JD, String WD) {
        repository.getToadyHour(token, JD, WD, new ResponseObserver<Hourbean>() {
            @Override
            public void success(Hourbean hourbean) {
                hourBeanData.postValue(hourbean);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
            }
        });
    }

    @Override
    public void onDestroy() {
        repository.cancelAllRequest();
        super.onDestroy();
    }
}
