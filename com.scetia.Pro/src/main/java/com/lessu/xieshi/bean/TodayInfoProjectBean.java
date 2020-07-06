package com.lessu.xieshi.bean;

public class TodayInfoProjectBean {
    /**
     * 工程名称
     */
    private String ProjectName;
    /**
     * 工程性质
     */
    private String ProjectNature;
    /**
     * 工程地址
     */
    private String ProjectAddress;
    /**
     * 施工单位
     */
    private String ConstructUnitName;
    /**
     * 建设单位
     */
    private String BuildUnitName;
    /**
     * 监理单位
     */
    private String SuperviorUnitName;
    /**
     * 检测单位
     */
    private String DetectionUnitNames;

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getProjectNature() {
        return ProjectNature;
    }

    public void setProjectNature(String projectNature) {
        ProjectNature = projectNature;
    }

    public String getProjectAddress() {
        return ProjectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        ProjectAddress = projectAddress;
    }

    public String getConstructUnitName() {
        return ConstructUnitName;
    }

    public void setConstructUnitName(String constructUnitName) {
        ConstructUnitName = constructUnitName;
    }

    public String getBuildUnitName() {
        return BuildUnitName;
    }

    public void setBuildUnitName(String buildUnitName) {
        BuildUnitName = buildUnitName;
    }

    public String getSuperviorUnitName() {
        return SuperviorUnitName;
    }

    public void setSuperviorUnitName(String superviorUnitName) {
        SuperviorUnitName = superviorUnitName;
    }

    public String getDetectionUnitNames() {
        return DetectionUnitNames;
    }

    public void setDetectionUnitNames(String detectionUnitNames) {
        DetectionUnitNames = detectionUnitNames;
    }
}
