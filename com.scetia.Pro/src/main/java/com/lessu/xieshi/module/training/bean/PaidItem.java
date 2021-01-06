package com.lessu.xieshi.module.training.bean;

public class PaidItem {
    public PaidItem(String projectName, String projectCode, String beginDate, String endDate) {
        this.projectName = projectName;
        this.projectCode = projectCode;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    /**
     * 课程名称
     */
    private String projectName;
    /**
     * 课程编号
     */
    private String projectCode;

    private String beginDate;
    /**
     * 课程结束时间
     */
    private String endDate;
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
