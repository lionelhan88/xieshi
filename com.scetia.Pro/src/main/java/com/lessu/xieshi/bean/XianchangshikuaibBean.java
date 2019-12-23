package com.lessu.xieshi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fhm on 2018/4/8.
 */

public class XianchangshikuaibBean implements Serializable{


    /**
     * Success : true
     * Code : 1000
     * Message :
     * Data : {"ProjectId":"4588b535-780e-4643-bbcd-4edd2ea1bd4d","SampleInProjectDetailList":[{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785867~1112785872","CreateDateTime":"2017-04-25","Molding_Date":"2017-03-30","GradeName":"水下C30"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785873~1112785878","CreateDateTime":"2017-04-25","Molding_Date":"2017-03-30","GradeName":"水下C30"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785879~1112785884","CreateDateTime":"2017-04-25","Molding_Date":"2017-03-30","GradeName":"水下C30"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785861~1112785863","CreateDateTime":"2017-05-19","Molding_Date":"2017-04-21","GradeName":"I类"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785918~1112785920","CreateDateTime":"2017-05-19","Molding_Date":"2017-04-27","GradeName":"I类"},{"ItemName":"混凝土抗渗","CoreCodeNo":"1610245956~1610245961","CreateDateTime":"2017-08-24","Molding_Date":"2017-07-30","GradeName":"C50"},{"ItemName":"混凝土抗压","CoreCodeNo":"1211133939~1211133941","CreateDateTime":"2017-01-19","Molding_Date":"2017-01-18","GradeName":"C35"},{"ItemName":"混凝土抗压","CoreCodeNo":"1211133861~1211133863","CreateDateTime":"2017-01-17","Molding_Date":"2017-01-16","GradeName":"C35"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785885~1112785890","CreateDateTime":"2017-05-03","Molding_Date":"2017-04-05","GradeName":"水下C30"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785864~1112785866","CreateDateTime":"2017-05-22","Molding_Date":"2017-04-24","GradeName":"I类"}],"SampleInProjectSumm":[{"SummItemName":"灌浆料抗压","SummSampleCount":7},{"SummItemName":"混凝土抗渗","SummSampleCount":1},{"SummItemName":"混凝土抗压","SummSampleCount":2}]}
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
         * ProjectId : 4588b535-780e-4643-bbcd-4edd2ea1bd4d
         * SampleInProjectDetailList : [{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785867~1112785872","CreateDateTime":"2017-04-25","Molding_Date":"2017-03-30","GradeName":"水下C30"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785873~1112785878","CreateDateTime":"2017-04-25","Molding_Date":"2017-03-30","GradeName":"水下C30"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785879~1112785884","CreateDateTime":"2017-04-25","Molding_Date":"2017-03-30","GradeName":"水下C30"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785861~1112785863","CreateDateTime":"2017-05-19","Molding_Date":"2017-04-21","GradeName":"I类"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785918~1112785920","CreateDateTime":"2017-05-19","Molding_Date":"2017-04-27","GradeName":"I类"},{"ItemName":"混凝土抗渗","CoreCodeNo":"1610245956~1610245961","CreateDateTime":"2017-08-24","Molding_Date":"2017-07-30","GradeName":"C50"},{"ItemName":"混凝土抗压","CoreCodeNo":"1211133939~1211133941","CreateDateTime":"2017-01-19","Molding_Date":"2017-01-18","GradeName":"C35"},{"ItemName":"混凝土抗压","CoreCodeNo":"1211133861~1211133863","CreateDateTime":"2017-01-17","Molding_Date":"2017-01-16","GradeName":"C35"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785885~1112785890","CreateDateTime":"2017-05-03","Molding_Date":"2017-04-05","GradeName":"水下C30"},{"ItemName":"灌浆料抗压","CoreCodeNo":"1112785864~1112785866","CreateDateTime":"2017-05-22","Molding_Date":"2017-04-24","GradeName":"I类"}]
         * SampleInProjectSumm : [{"SummItemName":"灌浆料抗压","SummSampleCount":7},{"SummItemName":"混凝土抗渗","SummSampleCount":1},{"SummItemName":"混凝土抗压","SummSampleCount":2}]
         */

        private String ProjectId;
        private List<SampleInProjectDetailListBean> SampleInProjectDetailList;
        private List<SampleInProjectSummBean> SampleInProjectSumm;

        public String getProjectId() {
            return ProjectId;
        }

        public void setProjectId(String ProjectId) {
            this.ProjectId = ProjectId;
        }

        public List<SampleInProjectDetailListBean> getSampleInProjectDetailList() {
            return SampleInProjectDetailList;
        }

        public void setSampleInProjectDetailList(List<SampleInProjectDetailListBean> SampleInProjectDetailList) {
            this.SampleInProjectDetailList = SampleInProjectDetailList;
        }

        public List<SampleInProjectSummBean> getSampleInProjectSumm() {
            return SampleInProjectSumm;
        }

        public void setSampleInProjectSumm(List<SampleInProjectSummBean> SampleInProjectSumm) {
            this.SampleInProjectSumm = SampleInProjectSumm;
        }

        public static class SampleInProjectDetailListBean implements Serializable{
            /**
             * ItemName : 灌浆料抗压
             * CoreCodeNo : 1112785867~1112785872
             * CreateDateTime : 2017-04-25
             * Molding_Date : 2017-03-30
             * GradeName : 水下C30
             */

            private String ItemName;
            private String CoreCodeNo;
            private String CreateDateTime;
            private String Molding_Date;
            private String GradeName;

            public String getItemName() {
                return ItemName;
            }

            public void setItemName(String ItemName) {
                this.ItemName = ItemName;
            }

            public String getCoreCodeNo() {
                return CoreCodeNo;
            }

            public void setCoreCodeNo(String CoreCodeNo) {
                this.CoreCodeNo = CoreCodeNo;
            }

            public String getCreateDateTime() {
                return CreateDateTime;
            }

            public void setCreateDateTime(String CreateDateTime) {
                this.CreateDateTime = CreateDateTime;
            }

            public String getMolding_Date() {
                return Molding_Date;
            }

            public void setMolding_Date(String Molding_Date) {
                this.Molding_Date = Molding_Date;
            }

            public String getGradeName() {
                return GradeName;
            }

            public void setGradeName(String GradeName) {
                this.GradeName = GradeName;
            }
        }

        public static class SampleInProjectSummBean implements Serializable{
            /**
             * SummItemName : 灌浆料抗压
             * SummSampleCount : 7
             */

            private String SummItemName;
            private int SummSampleCount;

            public String getSummItemName() {
                return SummItemName;
            }

            public void setSummItemName(String SummItemName) {
                this.SummItemName = SummItemName;
            }

            public int getSummSampleCount() {
                return SummSampleCount;
            }

            public void setSummSampleCount(int SummSampleCount) {
                this.SummSampleCount = SummSampleCount;
            }
        }
    }
}
