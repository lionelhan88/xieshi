package com.lessu.xieshi.bean;

import java.util.List;

/**
 * Created by fhm on 2016/7/26.
 */
public class ExamineSample {

    /**
     * Sample_Id : 2010001355
     * SampleID : 122502
     * SampleName : 细集料(沥青混合料)
     * SpecName : 细砂
     * GradeName : 快速路、主干路
     * DelegateQuan :
     * ProjectPart :
     * ProduceFactory :
     * Record_Certificate :
     * DetectionDate : 2010-12-27
     * Exam_Result : 合格
     * AccessRuleCode : JTG E42-2005
     * DetectonRule : DGJ 08-118-2005
     * ParamInfo : [{"UCStandardValue":"","UCTestValue":"","UCResultValue":"","ParameterName":"砂当量"}]
     */

    private String Sample_Id;
    private String SampleID;
    private String SampleName;
    private String SpecName;
    private String GradeName;
    private String DelegateQuan;
    private String ProjectPart;
    private String ProduceFactory;
    private String Record_Certificate;
    private String DetectionDate;
    private String Exam_Result;
    private String AccessRuleCode;
    private String DetectonRule;
    /**
     * UCStandardValue :
     * UCTestValue :
     * UCResultValue :
     * ParameterName : 砂当量
     */

    private List<ParamInfoBean> ParamInfo;

    public String getSample_Id() {
        return Sample_Id;
    }

    public void setSample_Id(String Sample_Id) {
        this.Sample_Id = Sample_Id;
    }

    public String getSampleID() {
        return SampleID;
    }

    public void setSampleID(String SampleID) {
        this.SampleID = SampleID;
    }

    public String getSampleName() {
        return SampleName;
    }

    public void setSampleName(String SampleName) {
        this.SampleName = SampleName;
    }

    public String getSpecName() {
        return SpecName;
    }

    public void setSpecName(String SpecName) {
        this.SpecName = SpecName;
    }

    public String getGradeName() {
        return GradeName;
    }

    public void setGradeName(String GradeName) {
        this.GradeName = GradeName;
    }

    public String getDelegateQuan() {
        return DelegateQuan;
    }

    public void setDelegateQuan(String DelegateQuan) {
        this.DelegateQuan = DelegateQuan;
    }

    public String getProjectPart() {
        return ProjectPart;
    }

    public void setProjectPart(String ProjectPart) {
        this.ProjectPart = ProjectPart;
    }

    public String getProduceFactory() {
        return ProduceFactory;
    }

    public void setProduceFactory(String ProduceFactory) {
        this.ProduceFactory = ProduceFactory;
    }

    public String getRecord_Certificate() {
        return Record_Certificate;
    }

    public void setRecord_Certificate(String Record_Certificate) {
        this.Record_Certificate = Record_Certificate;
    }

    public String getDetectionDate() {
        return DetectionDate;
    }

    public void setDetectionDate(String DetectionDate) {
        this.DetectionDate = DetectionDate;
    }

    public String getExam_Result() {
        return Exam_Result;
    }

    public void setExam_Result(String Exam_Result) {
        this.Exam_Result = Exam_Result;
    }

    public String getAccessRuleCode() {
        return AccessRuleCode;
    }

    public void setAccessRuleCode(String AccessRuleCode) {
        this.AccessRuleCode = AccessRuleCode;
    }

    public String getDetectonRule() {
        return DetectonRule;
    }

    public void setDetectonRule(String DetectonRule) {
        this.DetectonRule = DetectonRule;
    }

    public List<ParamInfoBean> getParamInfo() {
        return ParamInfo;
    }

    public void setParamInfo(List<ParamInfoBean> ParamInfo) {
        this.ParamInfo = ParamInfo;
    }

    public static class ParamInfoBean {
        private String UCStandardValue;
        private String UCTestValue;
        private String UCResultValue;
        private String ParameterName;

        public String getUCStandardValue() {
            return UCStandardValue;
        }

        public void setUCStandardValue(String UCStandardValue) {
            this.UCStandardValue = UCStandardValue;
        }

        public String getUCTestValue() {
            return UCTestValue;
        }

        public void setUCTestValue(String UCTestValue) {
            this.UCTestValue = UCTestValue;
        }

        public String getUCResultValue() {
            return UCResultValue;
        }

        public void setUCResultValue(String UCResultValue) {
            this.UCResultValue = UCResultValue;
        }

        public String getParameterName() {
            return ParameterName;
        }

        public void setParameterName(String ParameterName) {
            this.ParameterName = ParameterName;
        }
    }
}
