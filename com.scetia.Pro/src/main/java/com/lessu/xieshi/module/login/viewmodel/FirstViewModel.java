package com.lessu.xieshi.module.login.viewmodel;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.lessu.xieshi.base.BaseViewModel;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.login.bean.LoginUserBean;
import com.lessu.xieshi.module.login.repository.FirstModel;
import com.lessu.xieshi.lifcycle.BaiduMapLifecycle;
import com.lessu.xieshi.module.weather.bean.Hourbean;
import com.lessu.xieshi.module.weather.utils.Contenttianqi;

import java.text.DecimalFormat;
import java.util.List;

/**
 * created by ljs
 * on 2020/11/20
 */
public class FirstViewModel extends BaseViewModel {
    private FirstModel model = new FirstModel();
    private BaiduMapLifecycle baiduMapLifecycle;
    public FirstViewModel(Application application, LifecycleOwner owner) {
        super(application);
        baiduMapLifecycle = new BaiduMapLifecycle(application);
        owner.getLifecycle().addObserver(this);
        owner.getLifecycle().addObserver(baiduMapLifecycle);
    }

    private MutableLiveData<LoginUserBean> userBeanData = new MutableLiveData<>();
    private MutableLiveData<Hourbean.DataBean> hourBeanData = new MutableLiveData<>();

    public MutableLiveData<LoginUserBean> getUserBeanData() {
        return userBeanData;
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
                    getHourWeather(city, Contenttianqi.gettoken(), longformat, latiformat);
                }
                baiduMapLifecycle.stopLocation();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
    }

    /**
     * 执行登录
     * @param userName
     * @param password
     * @param deviceId
     */
    public void login(String userName, String password, String deviceId) {
        loadState.postValue(LoadState.LOADING);
        model.login(userName, password, deviceId, new ResponseObserver<LoginUserBean>() {
            @Override
            public void success(LoginUserBean loginUserBean) {
                loadState.postValue(LoadState.SUCCESS);
                userBeanData.postValue(loginUserBean);
            }

            @Override
            public void failure(ExceptionHandle.ResponseThrowable throwable) {
                loadState.postValue(LoadState.FAILURE);
                throwableLiveData.postValue(throwable);
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
}
