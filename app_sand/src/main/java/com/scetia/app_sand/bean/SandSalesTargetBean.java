package com.scetia.app_sand.bean;

/**
 * created by ljs
 * on 2020/11/6
 * 销售对象实体类
 */
public class SandSalesTargetBean {
    //会员编号
    private String serialNo;
    //企业名称
    private String unitName;
    //企业性质
    private String unitType;
    private boolean isSelect;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
