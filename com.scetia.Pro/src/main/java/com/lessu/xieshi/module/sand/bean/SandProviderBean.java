package com.lessu.xieshi.module.sand.bean;

/**
 * created by ljs
 * on 2020/11/6
 * 供应商的实体类
 */
public class SandProviderBean {
    /**
     * 备案证名称
     */
    private String recordCardName;
    /**
     * 备案证编号
     */
    private String recordCardCode;

    public String getRecordCardName() {
        return recordCardName;
    }

    public void setRecordCardName(String recordCardName) {
        this.recordCardName = recordCardName;
    }

    public String getRecordCardCode() {
        return recordCardCode;
    }

    public void setRecordCardCode(String recordCardCode) {
        this.recordCardCode = recordCardCode;
    }
}
