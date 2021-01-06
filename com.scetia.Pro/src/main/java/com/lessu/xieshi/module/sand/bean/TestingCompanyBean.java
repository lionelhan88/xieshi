package com.lessu.xieshi.module.sand.bean;

/**
 * created by ljs
 * on 2020/11/9
 */
public class TestingCompanyBean {

    /**
     * unitName : 上海同标质量检测技术有限公司
     * counties : 杨浦区
     * unitAddress : 上海市杨树浦路2300号B115室
     * contactPerson : 武景林
     * contactPersonPhoneNo : 13918093800
     * memberCode : 0658
     */

    private String unitName;
    private String counties;
    private String unitAddress;
    private String contactPerson;
    private String contactPersonPhoneNo;
    private String memberCode;
    private boolean isSelect;
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCounties() {
        return counties;
    }

    public void setCounties(String counties) {
        this.counties = counties;
    }

    public String getUnitAddress() {
        return unitAddress;
    }

    public void setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPersonPhoneNo() {
        return contactPersonPhoneNo;
    }

    public void setContactPersonPhoneNo(String contactPersonPhoneNo) {
        this.contactPersonPhoneNo = contactPersonPhoneNo;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }
}
