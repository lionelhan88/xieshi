package com.lessu.xieshi.module.sand.bean;

/**
 * created by ljs
 * on 2020/12/18
 * 供应商
 */

public class SandSupplierBean {


    /**
     * productionUnitName : 上海山中玉实业有限公司
     * putOnRecordsPassport : BW(砂)-01-20200063
     * deadline : 2022-03-26
     */
    //备案企业名称
    private String productionUnitName;
    //备案证号
    private String putOnRecordsPassport;

    //有效日期
    private String deadline;

    public SandSupplierBean(String productionUnitName, String putOnRecordsPassport) {
        this.productionUnitName = productionUnitName;
        this.putOnRecordsPassport = putOnRecordsPassport;
    }

    public SandSupplierBean() {
    }

    public String getProductionUnitName() {
        return productionUnitName;
    }

    public void setProductionUnitName(String productionUnitName) {
        this.productionUnitName = productionUnitName;
    }

    public String getPutOnRecordsPassport() {
        return putOnRecordsPassport;
    }

    public void setPutOnRecordsPassport(String putOnRecordsPassport) {
        this.putOnRecordsPassport = putOnRecordsPassport;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
