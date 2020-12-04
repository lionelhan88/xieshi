package com.lessu.xieshi.lifcycle;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lessu.xieshi.Utils.LogUtil;

/**
 * created by ljs
 * on 2020/11/4
 */
public class BaiduMapLifecycle implements LifecycleObserver {
    private LocationClient mLocClient;
    private BDLocationListener bdLocationListener;

    public void setBdLocationListener(BDLocationListener bdLocationListener) {
        this.bdLocationListener = bdLocationListener;
        mLocClient.registerLocationListener(bdLocationListener);
    }

    public BaiduMapLifecycle(Context context) {
        initClientLocal(context);
    }

    /**
     * 初始化badidu地图监听
     */
    public void initClientLocal(Context context){
        mLocClient = new LocationClient(context);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        mLocClient.setLocOption(option);
    }

    /**
     * 开启监听
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startLocation(){
        mLocClient.start();
    }

    public void stopLocation(){
        mLocClient.stop();
    }

    /**
     * 页面退出解除位置监听
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void stopBaiduMap(){
        if(mLocClient!=null){
            mLocClient.stop();
            mLocClient.unRegisterLocationListener(bdLocationListener);
            mLocClient = null;
        }
    }
}
