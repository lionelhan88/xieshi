package com.lessu.xieshi.bean;

import com.google.gson.annotations.SerializedName;

public class Project {
    String planNo;
    String projectCode;
    String innerPlanId;
    String memberId;

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getInnerPlanId() {
        return innerPlanId;
    }

    public void setInnerPlanId(String innerPlanId) {
        this.innerPlanId = innerPlanId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
