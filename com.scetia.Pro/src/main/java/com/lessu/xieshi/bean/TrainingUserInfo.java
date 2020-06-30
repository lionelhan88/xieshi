package com.lessu.xieshi.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在线培训用户信息
 */
public class TrainingUserInfo {
    private String fullName;
    /**
     * 上岗证号
     */
    private String certificateNo;
    private String planName;
    private List<PaidItem> paidItemNames;
    private PushToDx pushToDx;
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public List<PaidItem> getPaidItemNames() {
        return paidItemNames;
    }

    public void setPaidItemNames(List<PaidItem> paidItemNames) {
        this.paidItemNames = paidItemNames;
    }

    public PushToDx getPushToDx() {
        return pushToDx;
    }

    public void setPushToDx(PushToDx pushToDx) {
        this.pushToDx = pushToDx;
    }

}
