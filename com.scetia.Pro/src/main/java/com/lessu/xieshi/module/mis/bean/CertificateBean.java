package com.lessu.xieshi.module.mis.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fhm on 2017/10/11.
 * 证书信息实体类
 */
public class CertificateBean implements Serializable {
    /**
     * CertificateNumber : 10042
     * CertificateState : 正常
     * Name : 范珏
     * Sex : 女
     * MemberName : [0001]上海同济检测技术有限公司
     * IdentityCardNumber : 310101641123204
     * MobilePhoneNumber : 13701684888
     * SchoolName : 中国农民大学
     * Speciality : 管理
     * EducationBackground : 大专
     * TitleOfATechnicalPost : 无
     * CertificateItemItemNames : [{"CertificateItemName":"[到期日期：2017-12-31] [环境（放射性）]"},{"CertificateItemName":"[到期日期：2018-12-31] [测量设备管理]"},{"CertificateItemName":"[到期日期：2011-12-30] [节能材料检测]"},{"CertificateItemName":"[到期日期：2018-12-31] [环境（色谱）]"},{"CertificateItemName":"[到期日期：2018-12-31] [化学（环境）]"}]
     */

    private String CertificateNumber;
    private String CertificateState;
    private String Name;
    private String Sex;
    private String MemberName;
    private String IdentityCardNumber;
    private String MobilePhoneNumber;
    private String SchoolName;
    private String Speciality;
    private String EducationBackground;
    private String TitleOfATechnicalPost;
    private List<CertificateItemItemNamesBean> CertificateItemItemNames;

    public String getCertificateNumber() {
        return CertificateNumber;
    }

    public void setCertificateNumber(String CertificateNumber) {
        this.CertificateNumber = CertificateNumber;
    }

    public String getCertificateState() {
        return CertificateState;
    }

    public void setCertificateState(String CertificateState) {
        this.CertificateState = CertificateState;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String MemberName) {
        this.MemberName = MemberName;
    }

    public String getIdentityCardNumber() {
        return IdentityCardNumber;
    }

    public void setIdentityCardNumber(String IdentityCardNumber) {
        this.IdentityCardNumber = IdentityCardNumber;
    }

    public String getMobilePhoneNumber() {
        return MobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String MobilePhoneNumber) {
        this.MobilePhoneNumber = MobilePhoneNumber;
    }

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String SchoolName) {
        this.SchoolName = SchoolName;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String Speciality) {
        this.Speciality = Speciality;
    }

    public String getEducationBackground() {
        return EducationBackground;
    }

    public void setEducationBackground(String EducationBackground) {
        this.EducationBackground = EducationBackground;
    }

    public String getTitleOfATechnicalPost() {
        return TitleOfATechnicalPost;
    }

    public void setTitleOfATechnicalPost(String TitleOfATechnicalPost) {
        this.TitleOfATechnicalPost = TitleOfATechnicalPost;
    }

    public List<CertificateItemItemNamesBean> getCertificateItemItemNames() {
        return CertificateItemItemNames;
    }

    public void setCertificateItemItemNames(List<CertificateItemItemNamesBean> CertificateItemItemNames) {
        this.CertificateItemItemNames = CertificateItemItemNames;
    }

    public static class CertificateItemItemNamesBean implements Serializable {
        /**
         * CertificateItemName : [到期日期：2017-12-31] [环境（放射性）]
         */

        private String CertificateItemName;

        public String getCertificateItemName() {
            return CertificateItemName;
        }

        public void setCertificateItemName(String CertificateItemName) {
            this.CertificateItemName = CertificateItemName;
        }
    }
    //}
}
