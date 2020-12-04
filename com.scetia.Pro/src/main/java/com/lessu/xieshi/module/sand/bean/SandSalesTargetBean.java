package com.lessu.xieshi.module.sand.bean;

/**
 * created by ljs
 * on 2020/11/6
 * 销售对象实体类
 */
public class SandSalesTargetBean {

    private int num;
    private String salesTargetName;
    private String getSalesTargetNature;
    private boolean isSelect;
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getSalesTargetName() {
        return salesTargetName;
    }

    public void setSalesTargetName(String salesTargetName) {
        this.salesTargetName = salesTargetName;
    }

    public String getGetSalesTargetNature() {
        return getSalesTargetNature;
    }

    public void setGetSalesTargetNature(String getSalesTargetNature) {
        this.getSalesTargetNature = getSalesTargetNature;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
