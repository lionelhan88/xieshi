package com.scetia.Pro.lib_map;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapView;


/**
 * created by ljs
 * on 2020/11/4
 */
public class BaiduMapLifecycle implements LifecycleObserver {
    private LocationClient mLocClient;
    private BDLocationListener bdLocationListener;
    private LocationClientOption option;
    private MapView mapView;

    public BaiduMapLifecycle(Context context) {
        initClientLocal(context);
    }

    public BaiduMapLifecycle(Context context, MapView mapView) {
        initClientLocal(context);
        this.mapView = mapView;
    }

    /**
     * 初始化badidu地图监听
     */
    public void initClientLocal(Context context) {
        mLocClient = new LocationClient(context);
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        mLocClient.setLocOption(option);
    }

    /**
     * 谁知坐标获取监听事件
     *
     * @param bdLocationListener 监听回调接口
     */
    public void setBdLocationListener(BDLocationListener bdLocationListener) {
        this.bdLocationListener = bdLocationListener;
        mLocClient.registerLocationListener(bdLocationListener);
    }

    /**
     * 设置获取坐标的时间间隔
     *
     * @param timeMillion 时间（毫秒）
     */
    public void setOptionScanSpan(int timeMillion) {
        option.setScanSpan(timeMillion);
    }

    /**
     * 开启监听
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startLocation() {
        mLocClient.start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
    }

    public void stopLocation() {
        mLocClient.stop();
    }

    /**
     * 页面退出解除位置监听
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void stopBaiduMap() {
        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient.unRegisterLocationListener(bdLocationListener);
            bdLocationListener = null;
            mLocClient = null;
        }
    }
}
