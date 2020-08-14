package com.lessu.xieshi.bean;

public class CourseScore {
    /**
     * 用户标识
     */
    private String userId;
    /**
     * 课程代码
     */
    private String projectCode;
    /**
     * 课程名称
     */
    private String projectName;
    /**
     * 学期
     */
    private String planNo;
    /**
     * 视频成绩
     */
    private String videoScore;
    /**
     * 作业成绩
     */
    private String homeworkScore;
    /**
     * 作业总数
     */
    private String homeworkTotal;
    /**
     * 已提交作业数
     */
    private String homeworkSubmit;
    /**
     * 最终成绩
     */
    private String totalScore;
    /**
     * 首次学习时间
     */
    private String firstEnterTime;
    /**
     * 是否符合要求 0为不符合要求 1为符合要求
     */
    private boolean isFinish;
    private String innerPlanId;
    private String memberId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getVideoScore() {
        return videoScore;
    }

    public void setVideoScore(String videoScore) {
        this.videoScore = videoScore;
    }

    public String getHomeworkScore() {
        return homeworkScore;
    }

    public void setHomeworkScore(String homeworkScore) {
        this.homeworkScore = homeworkScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getFirstEnterTime() {
        return firstEnterTime;
    }

    public void setFirstEnterTime(String firstEnterTime) {
        this.firstEnterTime = firstEnterTime;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
