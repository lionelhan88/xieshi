package com.lessu.xieshi.module.unqualified.bean;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/15
 */
public class TestingReportData {

    /**
     * Type : 0
     * ListContent
     * PageCount : 1
     * PageSize : 10
     * CurrentPageNo : 1
     */

    private int Type;
    private int PageCount;
    private int PageSize;
    private int CurrentPageNo;
    private List<TestingReportBean> ListContent;

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int PageCount) {
        this.PageCount = PageCount;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public int getCurrentPageNo() {
        return CurrentPageNo;
    }

    public void setCurrentPageNo(int CurrentPageNo) {
        this.CurrentPageNo = CurrentPageNo;
    }

    public List<TestingReportBean> getListContent() {
        return ListContent;
    }

    public void setListContent(List<TestingReportBean> ListContent) {
        this.ListContent = ListContent;
    }

    public static class TestingReportBean {
        /**
         * Report_ID : SXC202000051
         * ProJectName : 大型居住社区浦江配套商品房鲁汇基地市政配套工程
         * UqExecStatus : 0
         * IdentifyingCode : 122100036451
         */

        private String Report_ID;
        private String ProJectName;
        private int UqExecStatus;
        private String IdentifyingCode;

        public String getReport_ID() {
            return Report_ID;
        }

        public void setReport_ID(String Report_ID) {
            this.Report_ID = Report_ID;
        }

        public String getProJectName() {
            return ProJectName;
        }

        public void setProJectName(String ProJectName) {
            this.ProJectName = ProJectName;
        }

        public int getUqExecStatus() {
            return UqExecStatus;
        }

        public void setUqExecStatus(int UqExecStatus) {
            this.UqExecStatus = UqExecStatus;
        }

        public String getIdentifyingCode() {
            return IdentifyingCode;
        }

        public void setIdentifyingCode(String IdentifyingCode) {
            this.IdentifyingCode = IdentifyingCode;
        }
    }
}
