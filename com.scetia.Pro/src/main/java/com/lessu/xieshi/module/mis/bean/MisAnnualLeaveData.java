package com.lessu.xieshi.module.mis.bean;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/2
 */
public class MisAnnualLeaveData {
    /**
     * NJApproveBtn : 0
     * PageCount : 1
     * PageSize : 5
     * CurrentPageNo : 1
     */
    private String NJApproveBtn;
    private int PageCount;
    private int PageSize;
    private int CurrentPageNo;
    private List<AnnualLeaveBean> ListContent;

    public String getNJApproveBtn() {
        return NJApproveBtn;
    }

    public void setNJApproveBtn(String NJApproveBtn) {
        this.NJApproveBtn = NJApproveBtn;
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

    public List<AnnualLeaveBean> getListContent() {
        return ListContent;
    }

    public void setListContent(List<AnnualLeaveBean> ListContent) {
        this.ListContent = ListContent;
    }

    public static class AnnualLeaveBean {
        /**
         * mId : f9a10b5f-47a9-4f2e-9262-d567b1ebd817
         * xm : 林记帅
         * Status : 已批准
         * LeaveType : 年假
         * LeaveTime : 2020/1/22-2020/1/23
         * Days : 2
         * LeaveReason :
         * ApplyTime : 2019/12/24 13:39:20
         * DepartApprove : 郑建
         * DepartTime : 2019/12/30 8:56:53
         */

        private String mId;
        private String xm;
        private String Status;
        private String LeaveType;
        private String LeaveTime;
        private String Days;
        private String LeaveReason;
        private String ApplyTime;
        private String DepartApprove;
        private String DepartTime;

        public String getMId() {
            return mId;
        }

        public void setMId(String mId) {
            this.mId = mId;
        }

        public String getXm() {
            return xm;
        }

        public void setXm(String xm) {
            this.xm = xm;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getLeaveType() {
            return LeaveType;
        }

        public void setLeaveType(String LeaveType) {
            this.LeaveType = LeaveType;
        }

        public String getLeaveTime() {
            return LeaveTime;
        }

        public void setLeaveTime(String LeaveTime) {
            this.LeaveTime = LeaveTime;
        }

        public String getDays() {
            return Days;
        }

        public void setDays(String Days) {
            this.Days = Days;
        }

        public String getLeaveReason() {
            return LeaveReason;
        }

        public void setLeaveReason(String LeaveReason) {
            this.LeaveReason = LeaveReason;
        }

        public String getApplyTime() {
            return ApplyTime;
        }

        public void setApplyTime(String ApplyTime) {
            this.ApplyTime = ApplyTime;
        }

        public String getDepartApprove() {
            return DepartApprove;
        }

        public void setDepartApprove(String DepartApprove) {
            this.DepartApprove = DepartApprove;
        }

        public String getDepartTime() {
            return DepartTime;
        }

        public void setDepartTime(String DepartTime) {
            this.DepartTime = DepartTime;
        }
    }
}
