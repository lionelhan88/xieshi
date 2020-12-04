package com.lessu.xieshi.module.meet.bean;

public class MeetingCompanyBean {

    /**
     * MemberCode : 0000
     * MemberName : 协会
     */

    private String MemberCode;
    private String MemberName;

    public MeetingCompanyBean() {
    }

    public MeetingCompanyBean(String memberCode, String memberName) {
        MemberCode = memberCode;
        MemberName = memberName;
    }

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
}
