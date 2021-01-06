package com.lessu.xieshi.module.unqualified.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/17
 */
public class ReportConclusionBean {

    /**
     * Sample_Id : 2020040216
     * Exam_Result : 不合格
     */
    @SerializedName("Sample_Id")
    private String sampleId;
    @SerializedName("Exam_Result")
    private String examResult;

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }


    public String getSampleId() {
        return sampleId;
    }

    public String getExamResult() {
        return examResult;
    }

    public void setExamResult(String examResult) {
        this.examResult = examResult;
    }
}
