package com.lessu.xieshi.module.mis.bean;

/**
 * Created by fhm on 2017/10/12.
 */

public class Pgdetailbean {
    /**
     * Success : true
     * Code : 1000
     * Message :
     * Data : {"MemberCode":"0603","MemberName":"[0603]上海航升建筑材料制造有限公司试验室","Status":"已批准","ApplicationType":"年审加扩项","CheckerName":"顾幽燕","CheckDate":"2014/5/27 12:24:04","InnerId":"PG-5535","Applicant":"何国伟","ApplicantDate":"2014/3/28 17:28:47","AccepterName":"杨兰英","ApplicationDate":"2014/4/3 10:52:23","AssessExecuteDate":"2014/3/28 0:00:00","AssessLeaderName":"华夏","AssessFinishDate":"2014/4/29 0:00:00","ApproverName":"韩跃红","ApproveDate":"2014/5/28 10:30:51","S1":"3","S2":"2016/5/31","KPID":"073d9a68-740b-44d0-94ef-de62a07882a9","MemberLevelType":"","LaboratoryTypeString":"","LaboratoryTypeStringInside":"","Items":"扩项项目：保水增稠材料，砂浆，"}
     */

    private boolean Success;
    private int Code;
    private String Message;
    private DataBean Data;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * MemberCode : 0603
         * MemberName : [0603]上海航升建筑材料制造有限公司试验室
         * Status : 已批准
         * ApplicationType : 年审加扩项
         * CheckerName : 顾幽燕
         * CheckDate : 2014/5/27 12:24:04
         * InnerId : PG-5535
         * Applicant : 何国伟
         * ApplicantDate : 2014/3/28 17:28:47
         * AccepterName : 杨兰英
         * ApplicationDate : 2014/4/3 10:52:23
         * AssessExecuteDate : 2014/3/28 0:00:00
         * AssessLeaderName : 华夏
         * AssessFinishDate : 2014/4/29 0:00:00
         * ApproverName : 韩跃红
         * ApproveDate : 2014/5/28 10:30:51
         * S1 : 3
         * S2 : 2016/5/31
         * KPID : 073d9a68-740b-44d0-94ef-de62a07882a9
         * MemberLevelType :
         * LaboratoryTypeString :
         * LaboratoryTypeStringInside :
         * Items : 扩项项目：保水增稠材料，砂浆，
         */

        private String MemberCode;
        private String MemberName;
        private String Status;
        private String ApplicationType;
        private String CheckerName;
        private String CheckDate;
        private String InnerId;
        private String Applicant;
        private String ApplicantDate;
        private String AccepterName;
        private String ApplicationDate;
        private String AssessExecuteDate;
        private String AssessLeaderName;
        private String AssessFinishDate;
        private String ApproverName;
        private String ApproveDate;
        private String S1;
        private String S2;
        private String KPID;
        private String MemberLevelType;
        private String LaboratoryTypeString;
        private String LaboratoryTypeStringInside;
        private String Items;

        public String getMemberCode() {
            return MemberCode;
        }

        public void setMemberCode(String MemberCode) {
            this.MemberCode = MemberCode;
        }

        public String getMemberName() {
            return MemberName;
        }

        public void setMemberName(String MemberName) {
            this.MemberName = MemberName;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getApplicationType() {
            return ApplicationType;
        }

        public void setApplicationType(String ApplicationType) {
            this.ApplicationType = ApplicationType;
        }

        public String getCheckerName() {
            return CheckerName;
        }

        public void setCheckerName(String CheckerName) {
            this.CheckerName = CheckerName;
        }

        public String getCheckDate() {
            return CheckDate;
        }

        public void setCheckDate(String CheckDate) {
            this.CheckDate = CheckDate;
        }

        public String getInnerId() {
            return InnerId;
        }

        public void setInnerId(String InnerId) {
            this.InnerId = InnerId;
        }

        public String getApplicant() {
            return Applicant;
        }

        public void setApplicant(String Applicant) {
            this.Applicant = Applicant;
        }

        public String getApplicantDate() {
            return ApplicantDate;
        }

        public void setApplicantDate(String ApplicantDate) {
            this.ApplicantDate = ApplicantDate;
        }

        public String getAccepterName() {
            return AccepterName;
        }

        public void setAccepterName(String AccepterName) {
            this.AccepterName = AccepterName;
        }

        public String getApplicationDate() {
            return ApplicationDate;
        }

        public void setApplicationDate(String ApplicationDate) {
            this.ApplicationDate = ApplicationDate;
        }

        public String getAssessExecuteDate() {
            return AssessExecuteDate;
        }

        public void setAssessExecuteDate(String AssessExecuteDate) {
            this.AssessExecuteDate = AssessExecuteDate;
        }

        public String getAssessLeaderName() {
            return AssessLeaderName;
        }

        public void setAssessLeaderName(String AssessLeaderName) {
            this.AssessLeaderName = AssessLeaderName;
        }

        public String getAssessFinishDate() {
            return AssessFinishDate;
        }

        public void setAssessFinishDate(String AssessFinishDate) {
            this.AssessFinishDate = AssessFinishDate;
        }

        public String getApproverName() {
            return ApproverName;
        }

        public void setApproverName(String ApproverName) {
            this.ApproverName = ApproverName;
        }

        public String getApproveDate() {
            return ApproveDate;
        }

        public void setApproveDate(String ApproveDate) {
            this.ApproveDate = ApproveDate;
        }

        public String getS1() {
            return S1;
        }

        public void setS1(String S1) {
            this.S1 = S1;
        }

        public String getS2() {
            return S2;
        }

        public void setS2(String S2) {
            this.S2 = S2;
        }

        public String getKPID() {
            return KPID;
        }

        public void setKPID(String KPID) {
            this.KPID = KPID;
        }

        public String getMemberLevelType() {
            return MemberLevelType;
        }

        public void setMemberLevelType(String MemberLevelType) {
            this.MemberLevelType = MemberLevelType;
        }

        public String getLaboratoryTypeString() {
            return LaboratoryTypeString;
        }

        public void setLaboratoryTypeString(String LaboratoryTypeString) {
            this.LaboratoryTypeString = LaboratoryTypeString;
        }

        public String getLaboratoryTypeStringInside() {
            return LaboratoryTypeStringInside;
        }

        public void setLaboratoryTypeStringInside(String LaboratoryTypeStringInside) {
            this.LaboratoryTypeStringInside = LaboratoryTypeStringInside;
        }

        public String getItems() {
            return Items;
        }

        public void setItems(String Items) {
            this.Items = Items;
        }
    }
}
