package com.lessu.xieshi.Utils;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.HashMap;

/**
 * created by ljs
 * on 2020/11/19
 */
public class BDMapManage {
    private LocationClient mLocClient;
    private BDLocationListener bdLocationListener;
    //初始化 locationClient的对象
    public BDMapManage(Context context){
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
     * 设置BDLocationListener
     */
    public void setBDLocationListener(BDLocationListener listener){
        mLocClient.registerLocationListener(listener);
        bdLocationListener = listener;
    }

    /**
     * 设置定位监听的一些属性
     */
    public void setOptions(LocationClientOption options){
        mLocClient.setLocOption(options);
    }

    /**
     * 开启定位监听
     */
    public void startLocation(){
        mLocClient.start();
    }

    /**
     * 停止定位监听
     */
    public void stopLocation(){
        mLocClient.stop();
    }

    /**
     * 回收资源，解除监听
     */
    public void recycleBDLocation(){
        if(bdLocationListener!=null) {
            mLocClient.unRegisterLocationListener(bdLocationListener);
        }
        bdLocationListener = null;
        mLocClient = null;
    }


}
