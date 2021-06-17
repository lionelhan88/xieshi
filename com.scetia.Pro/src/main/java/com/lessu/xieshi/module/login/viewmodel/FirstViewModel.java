package com.lessu.xieshi.module.login.viewmodel;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.lessu.xieshi.module.login.repository.LoginRepository;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.lib_map.BaiduMapLifecycle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.lessu.xieshi.module.weather.bean.Hourbean;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * created by ljs
 * on 2020/11/20
 */
public class FirstViewModel extends LoginViewModel {
    private LoginRepository model = new LoginRepository();
    private BaiduMapLifecycle baiduMapLifecycle;
    private MutableLiveData<Hourbean.DataBean> hourBeanData = new MutableLiveData<>();

    public FirstViewModel(Application application, LifecycleOwner owner) {
        super(application);
        baiduMapLifecycle = new BaiduMapLifecycle(application);
        owner.getLifecycle().addObserver(this);
        owner.getLifecycle().addObserver(baiduMapLifecycle);
    }

    public MutableLiveData<Hourbean.DataBean> getHourBeanData() {
        return hourBeanData;
    }

    @Override
    public void onCreate() {
        baiduMapLifecycle.setBdLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                // 非空判断
                if (bdLocation != null) {
                    DecimalFormat df = new DecimalFormat("######0.00");
                    // 根据BDLocation 对象获得经纬度以及详细地址信息
                    double latitude = bdLocation.getLatitude();
                    double longitude = bdLocation.getLongitude();
                    final String latiformat = df.format(latitude);
                    final String longformat = df.format(longitude);
                    final String city = bdLocation.getCity().substring(0, bdLocation.getCity().length() - 1);
                    LogUtil.showLogE("firstActivity---"+bdLocation.getAddrStr());
                    getHourWeather(city, Constants.User.GET_TOKEN(), longformat, latiformat);
                }
                baiduMapLifecycle.stopLocation();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
    }

    /**
     * 获取当前时间天气数据
     * @param cityName
     * @param token
     * @param longgitude
     * @param latiformat
     */
    public void getHourWeather(String cityName, String token, String longgitude, String latiformat) {
        model.getHourWeather(token, longgitude, latiformat, new ResponseObserver<Hourbean>() {
            @Override
            public void success(Hourbean hourbean) {
                List<Hourbean.DataBean> data = hourbean.getData();
                if(data.size()>0){
                    Hourbean.DataBean dataBean = data.get(0);
                    dataBean.setCityName(cityName);
                    hourBeanData.postValue(dataBean);
                }
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                //首页的天气数据如果获取失败，不必处理
            }
        });
    }

    @Override
    public void onDestroy() {
        model.cancelAllRequest();
        super.onDestroy();
    }

}
