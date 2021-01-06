package com.lessu.xieshi.module.sand.bean;

import android.os.Build;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.collections.MapsKt;

/**
 * created by ljs
 * on 2020/12/18
 * 建设用砂规格
 */
public class SandSpecBean implements IPickerViewData {

    /**
     * specID : 110304301
     * specName : 特细砂
     */

    private String specID;
    private String specName;

    public SandSpecBean(String specID, String specName) {
        this.specID = specID;
        this.specName = specName;
    }

    public SandSpecBean() {
    }

    public String getSpecID() {
        return specID;
    }

    public void setSpecID(String specID) {
        this.specID = specID;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    @Override
    public String getPickerViewText() {
        return specName;
    }

    /**
     * 转换成map集合
     */
    public static HashMap<String,SandSpecBean> parseToMap(List<SandSpecBean> sandSpecBeans){
        HashMap<String,SandSpecBean> map = new HashMap<>();
        for (SandSpecBean bean :sandSpecBeans){
            map.put(bean.getSpecID(),bean);
        }
        return map;
    }
}
