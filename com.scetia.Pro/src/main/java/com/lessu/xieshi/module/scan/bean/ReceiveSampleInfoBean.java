package com.lessu.xieshi.module.scan.bean;

import java.io.Serializable;

/**
 * Created by fhm on 2017/2/13.
 */

public class ReceiveSampleInfoBean implements Serializable{
    public String Contract_SignNo;// 合同登记号1
    public String ConSign_ID;// 委托编号2
    public String Sample_ID;// 样品编号3
    public String Sample_BsId;// 标识编号4
    public String reportNumber;// 报告编号5
    public String buildingReportNumber;//6
    public String sample_Status;// 样品状态7
    public String exam_Result;// 检测结果8
    public String ProjectName;//工程名称9
    public String ProJect_Part;// 工程部位10
    public String projectAddress;// 工程地址11
    public String areaKey;// 所属区县12
    public String KindName;// 样品类别名称//13
    public String ItemName;// 项目名称//14
    public String SampleName;//样品名称//15
    public String sampleJudge;// 标准号//16
    public String Exam_Parameter_Cn;// 参数中文//17
    public String SpecName;// 规格名称//18
    public String GradeName;// 强度名称//19
    public String BuildUnitName;// 施工单位//20
    public String constructUnitName;// 建设单位名称//21
    public String superviseUnitName;// 监理单位名称//22
    public String detectionUnitName;// 检测单位名称//23
    public String Record_Certificate;// 备案证//24
    public String Produce_Factory;// 生产厂家//25
    public String Molding_Date;// 制作日期//26
    public String AgeTime;//龄期//27
    public String createDateTime;// 生成时间//28
    public String DetectonDate;// 委托日期//29
    public String reportDate;// 报告日期//30
    public String Memo;// 备注//31

    public int RetStatus;////0为正常1为未登记 2为已确认 3异常 4 缺失（数量为RETSTATUS 第2位开始截取）
    public String memberCode;// 会员编号
    private String codeNumber;

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getContract_SignNo() {
        return Contract_SignNo;
    }

    public void setContract_SignNo(String contract_SignNo) {
        Contract_SignNo = contract_SignNo;
    }

    public String getBuildUnitName() {
        return BuildUnitName;
    }

    public void setBuildUnitName(String buildUnitName) {
        BuildUnitName = buildUnitName;
    }

    public String getSample_BsId() {
        return Sample_BsId;
    }

    public void setSample_BsId(String sample_BsId) {
        Sample_BsId = sample_BsId;
    }

    public String getKindName() {
        return KindName;
    }

    public void setKindName(String kindName) {
        KindName = kindName;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getSampleName() {
        return SampleName;
    }

    public void setSampleName(String sampleName) {
        SampleName = sampleName;
    }

    public String getSpecName() {
        return SpecName;
    }

    public void setSpecName(String specName) {
        SpecName = specName;
    }

    public String getGradeName() {
        return GradeName;
    }

    public void setGradeName(String gradeName) {
        GradeName = gradeName;
    }

    public String getRecord_Certificate() {
        return Record_Certificate;
    }

    public void setRecord_Certificate(String record_Certificate) {
        Record_Certificate = record_Certificate;
    }

    public String getProduce_Factory() {
        return Produce_Factory;
    }

    public void setProduce_Factory(String produce_Factory) {
        Produce_Factory = produce_Factory;
    }

    public int getRetStatus() {
        return RetStatus;
    }

    public void setRetStatus(int retStatus) {
        RetStatus = retStatus;
    }

    public String getProJect_Part() {
        return ProJect_Part;
    }

    public void setProJect_Part(String proJect_Part) {
        ProJect_Part = proJect_Part;
    }

    public String getMolding_Date() {
        return Molding_Date;
    }

    public void setMolding_Date(String molding_Date) {
        Molding_Date = molding_Date;
    }

    public String getAgeTime() {
        return AgeTime;
    }

    public void setAgeTime(String ageTime) {
        AgeTime = ageTime;
    }

    public String getExam_Parameter_Cn() {
        return Exam_Parameter_Cn;
    }

    public void setExam_Parameter_Cn(String exam_Parameter_Cn) {
        Exam_Parameter_Cn = exam_Parameter_Cn;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public String getConSign_ID() {
        return ConSign_ID;
    }

    public void setConSign_ID(String conSign_ID) {
        ConSign_ID = conSign_ID;
    }

    public String getSample_ID() {
        return Sample_ID;
    }

    public void setSample_ID(String sample_ID) {
        Sample_ID = sample_ID;
    }

    public String getDetectonDate() {
        return DetectonDate;
    }

    public void setDetectonDate(String detectonDate) {
        DetectonDate = detectonDate;
    }

    public String getSample_Status() {
        return sample_Status;
    }

    public void setSample_Status(String sample_Status) {
        this.sample_Status = sample_Status;
    }

    public String getSampleJudge() {
        return sampleJudge;
    }

    public void setSampleJudge(String sampleJudge) {
        this.sampleJudge = sampleJudge;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getExam_Result() {
        return exam_Result;
    }

    public void setExam_Result(String exam_Result) {
        this.exam_Result = exam_Result;
    }

    public String getDetectionUnitName() {
        return detectionUnitName;
    }

    public void setDetectionUnitName(String detectionUnitName) {
        this.detectionUnitName = detectionUnitName;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getAreaKey() {
        return areaKey;
    }

    public void setAreaKey(String areaKey) {
        this.areaKey = areaKey;
    }

    public String getConstructUnitName() {
        return constructUnitName;
    }

    public void setConstructUnitName(String constructUnitName) {
        this.constructUnitName = constructUnitName;
    }

    public String getSuperviseUnitName() {
        return superviseUnitName;
    }

    public void setSuperviseUnitName(String superviseUnitName) {
        this.superviseUnitName = superviseUnitName;
    }

    public String getBuildingReportNumber() {
        return buildingReportNumber;
    }

    public void setBuildingReportNumber(String buildingReportNumber) {
        this.buildingReportNumber = buildingReportNumber;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }



}
