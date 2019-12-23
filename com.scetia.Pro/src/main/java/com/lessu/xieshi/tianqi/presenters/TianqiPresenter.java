package com.lessu.xieshi.tianqi.presenters;

import android.app.Activity;

import com.google.gson.JsonElement;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.tianqi.bean.Hourbean;
import com.lessu.xieshi.tianqi.bean.Tenbean;
import com.lessu.xieshi.tianqi.contracts.ITianqicontract;

import java.util.HashMap;

/**
 * Created by fhm on 2017/10/26.
 */

public class TianqiPresenter implements ITianqicontract.Presenter {
    private ITianqicontract.View view;
    private Activity activity;

    public TianqiPresenter(ITianqicontract.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Override
    public void getten(String token) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Token", token);
        System.out.println("params.............."+params);
        EasyAPI.apiConnectionAsync(activity, true, false, ApiMethodDescription.get("/ServiceWeather.asmx/GetDay"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                // TODO Auto-generated method stub
                System.out.println(result);
                Tenbean tenbean = GsonUtil.JsonToObject(result.toString(), Tenbean.class);
                view.gettencall(true,tenbean);
            }
            @Override
            public String onFailed(ApiError error) {
                System.out.println("shibai......"+error.errorMeesage);
                view.gettencall(false,null);
                return null;
            }
        });
    }

    @Override
    public void gethour(String token, String JD, String WD) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Token", token);
        params.put("JD", JD);
        params.put("WD", WD);
        System.out.println("params.............."+params);
        EasyAPI.apiConnectionAsync(activity, true, false, ApiMethodDescription.get("/ServiceWeather.asmx/GetHour"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                // TODO Auto-generated method stub
                System.out.println(result);
                Hourbean hourbean = GsonUtil.JsonToObject(result.toString(), Hourbean.class);
                view.gethourcall(true,hourbean);
            }
            @Override
            public String onFailed(ApiError error) {
                System.out.println("shibai......"+error.errorMeesage);
                view.gethourcall(false,null);
                return null;
            }
        });
    }
}
