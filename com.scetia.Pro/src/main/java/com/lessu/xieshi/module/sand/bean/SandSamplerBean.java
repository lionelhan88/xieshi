package com.lessu.xieshi.module.sand.bean;

import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.annotations.SerializedName;

/**
 * created by ljs
 * on 2020/11/6
 * 取样人的实体类
 */
public class SandSamplerBean implements IPickerViewData {
    private String samplerName;
    private String samplerCerNo;

    public String getSamplerName() {
        return samplerName;
    }

    public void setSamplerName(String samplerName) {
        this.samplerName = samplerName;
    }

    public String getSamplerCerNo() {
        return samplerCerNo;
    }

    public void setSamplerCerNo(String samplerCerNo) {
        this.samplerCerNo = samplerCerNo;
    }

    @Override
    public String getPickerViewText() {
        return samplerName;
    }
}
