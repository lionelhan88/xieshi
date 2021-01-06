package com.lessu.xieshi.module.sand.bean;

import com.google.gson.annotations.Expose;

/**
 * created by ljs
 * on 2020/12/23
 */
public class AddedTestingCompanyBean {
    /**
     * id : 79c5f2dd-f571-48e1-c9ba-08d89a498e7f
     * detectionAgencyUnitName : 上海同济检测技术有限公司
     * detectionAgencyCounties : 杨浦区
     * detectionAgencyUnitAddress : 上海市杨浦区阜新路281号
     * detectionAgencyContactPerson : 王奇慧
     * detectionAgencyContactPersonPhoneNo : 13764049390
     */

    private String id;
    @Expose
    private String detectionAgencyMemberCode;
    @Expose
    private String detectionAgencyUnitName;
    @Expose
    private String detectionAgencyCounties;
    @Expose
    private String detectionAgencyUnitAddress;
    @Expose
    private String detectionAgencyContactPerson;
    @Expose
    private String detectionAgencyContactPersonPhoneNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetectionAgencyMemberCode() {
        return detectionAgencyMemberCode;
    }

    public void setDetectionAgencyMemberCode(String detectionAgencyMemberCode) {
        this.detectionAgencyMemberCode = detectionAgencyMemberCode;
    }

    public String getDetectionAgencyUnitName() {
        return detectionAgencyUnitName;
    }

    public void setDetectionAgencyUnitName(String detectionAgencyUnitName) {
        this.detectionAgencyUnitName = detectionAgencyUnitName;
    }

    public String getDetectionAgencyCounties() {
        return detectionAgencyCounties;
    }

    public void setDetectionAgencyCounties(String detectionAgencyCounties) {
        this.detectionAgencyCounties = detectionAgencyCounties;
    }

    public String getDetectionAgencyUnitAddress() {
        return detectionAgencyUnitAddress;
    }

    public void setDetectionAgencyUnitAddress(String detectionAgencyUnitAddress) {
        this.detectionAgencyUnitAddress = detectionAgencyUnitAddress;
    }

    public String getDetectionAgencyContactPerson() {
        return detectionAgencyContactPerson;
    }

    public void setDetectionAgencyContactPerson(String detectionAgencyContactPerson) {
        this.detectionAgencyContactPerson = detectionAgencyContactPerson;
    }

    public String getDetectionAgencyContactPersonPhoneNo() {
        return detectionAgencyContactPersonPhoneNo;
    }

    public void setDetectionAgencyContactPersonPhoneNo(String detectionAgencyContactPersonPhoneNo) {
        this.detectionAgencyContactPersonPhoneNo = detectionAgencyContactPersonPhoneNo;
    }
}
