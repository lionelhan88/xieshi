package com.lessu.xieshi.module.mis.bean;

import java.util.List;
import java.util.Objects;

/**
 * created by ljs
 * on 2020/11/26
 * 会员查询返回数据实体类
 */
public class MisMemberSearchResultData {

    /**
     * ListContent : [{"rowNum":"1","MemberId":"0000","MemberName":"协会","MemberType":"对外检测单位",
     * "MemberStatus":"正常","JoinDate":"2017/5/4 14:38:20","CertificateId":"SCETIA123",
     * "CertificateExpirationDate":"2031/6/30","Fzr":"44/5828888881/+86-66",
     * "PhoneNumber":"123单位电话公司电话使用的地方","ContactAddress":"联系地址123456"}]
     * PageCount : 344
     * PageSize : 5
     * CurrentPageNo : 1
     */

    private int PageCount;
    private int PageSize;
    private int CurrentPageNo;
    private List<ListContentBean> ListContent;

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

    public List<ListContentBean> getListContent() {
        return ListContent;
    }

    public void setListContent(List<ListContentBean> ListContent) {
        this.ListContent = ListContent;
    }

    public static class ListContentBean {

        /**
         * rowNum : 1
         * MemberId : 0000
         * MemberName : 协会
         * MemberType : 对外检测单位
         * MemberStatus : 正常
         * JoinDate : 2017/5/4 14:38:20
         * CertificateId : SCETIA123
         * CertificateExpirationDate : 2031/6/30
         * Fzr : 44/5828888881/+86-66
         * PhoneNumber : 123单位电话公司电话使用的地方
         * ContactAddress : 联系地址123456
         */

        private String rowNum;
        private String MemberId;
        private String MemberName;
        private String MemberType;
        private String MemberStatus;
        private String JoinDate;
        private String CertificateId;
        private String CertificateExpirationDate;
        private String Fzr;
        private String PhoneNumber;
        private String ContactAddress;

        public String getRowNum() {
            return rowNum;
        }

        public void setRowNum(String rowNum) {
            this.rowNum = rowNum;
        }

        public String getMemberId() {
            return MemberId;
        }

        public void setMemberId(String MemberId) {
            this.MemberId = MemberId;
        }

        public String getMemberName() {
            return MemberName;
        }

        public void setMemberName(String MemberName) {
            this.MemberName = MemberName;
        }

        public String getMemberType() {
            return MemberType;
        }

        public void setMemberType(String MemberType) {
            this.MemberType = MemberType;
        }

        public String getMemberStatus() {
            return MemberStatus;
        }

        public void setMemberStatus(String MemberStatus) {
            this.MemberStatus = MemberStatus;
        }

        public String getJoinDate() {
            return JoinDate;
        }

        public void setJoinDate(String JoinDate) {
            this.JoinDate = JoinDate;
        }

        public String getCertificateId() {
            return CertificateId;
        }

        public void setCertificateId(String CertificateId) {
            this.CertificateId = CertificateId;
        }

        public String getCertificateExpirationDate() {
            return CertificateExpirationDate;
        }

        public void setCertificateExpirationDate(String CertificateExpirationDate) {
            this.CertificateExpirationDate = CertificateExpirationDate;
        }

        public String getFzr() {
            return Fzr;
        }

        public void setFzr(String Fzr) {
            this.Fzr = Fzr;
        }

        public String getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(String PhoneNumber) {
            this.PhoneNumber = PhoneNumber;
        }

        public String getContactAddress() {
            return ContactAddress;
        }

        public void setContactAddress(String ContactAddress) {
            this.ContactAddress = ContactAddress;
        }
    }

}
