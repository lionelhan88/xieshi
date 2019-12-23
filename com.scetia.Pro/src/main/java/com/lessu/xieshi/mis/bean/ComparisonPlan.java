package com.lessu.xieshi.mis.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fhm on 2018/10/16.
 */

public class ComparisonPlan {
    //编号
    @SerializedName("s1")
    private String comparisonNum;
    //比对项目
    @SerializedName("s2")
    private String comparisonName;
    //计划日期
    @SerializedName("s3")
    private String comparisonDate;
    //状态
    @SerializedName("s4")
    private String comparisonStatus;

    public String getComparisonNum() {
        return comparisonNum;
    }

    public void setComparisonNum(String comparisonNum) {
        this.comparisonNum = comparisonNum;
    }

    public String getComparisonName() {
        return comparisonName;
    }

    public void setComparisonName(String comparisonName) {
        this.comparisonName = comparisonName;
    }

    public String getComparisonDate() {
        return comparisonDate;
    }

    public void setComparisonDate(String comparisonDate) {
        this.comparisonDate = comparisonDate;
    }

    public String getComparisonStatus() {
        return comparisonStatus;
    }

    public void setComparisonStatus(String comparisonStatus) {
        this.comparisonStatus = comparisonStatus;
    }
}
