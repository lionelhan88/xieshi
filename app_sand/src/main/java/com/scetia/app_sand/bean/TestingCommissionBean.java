package com.scetia.app_sand.bean;

import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * created by ljs
 * on 2020/11/10
 */
public class TestingCommissionBean {

    /**
     * id : 1f48fdce-64ca-4f84-a5f9-08d89be7fc99
     * sampleName : 混合砂
     * sampler : 取样员3
     * samplingTime : 2020-11-30
     * consignUnit : 上海山中玉实业有限公司
     * principal : 委托人姓名3
     * commissionDate : 2020-12-09
     * sampleStatus : 未送样
     * flowInfoId : 92e6dfca-2457-4b6f-85dd-08d89a4c4428.
     * detectionAgencyMemberCode : 0001
     * samplingMethod : 2
     * appointmentSamplingTime : 2020-11-24T11:00:00
     * samplingLocation : 2
     * samplingProcess :
     * samplerCerNo : 12345
     * ccNo : 9950000160
     */

    private String id;
    private String sampleName;
    @Expose
    private String sampler;
    @Expose
    private String samplingTime;
    private String consignUnit;
    @Expose
    private String principal;
    private String commissionDate;
    private String sampleStatus;
    @Expose
    private String flowInfoId;
    @Expose
    private String detectionAgencyMemberCode;
    @Expose
    private int samplingMethod=2;

    private String appointmentSamplingTime;
    @Expose
    private int samplingLocation;
    @Expose
    private String samplingProcess;
    @Expose
    private String samplerCerNo;
    @Expose
    private String ccNo="";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSampler() {
        return sampler;
    }

    public void setSampler(String sampler) {
        this.sampler = sampler;
    }

    public String getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(String samplingTime) {
        this.samplingTime = samplingTime;
    }

    public String getConsignUnit() {
        return consignUnit;
    }

    public void setConsignUnit(String consignUnit) {
        this.consignUnit = consignUnit;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getCommissionDate() {
        return commissionDate;
    }

    public void setCommissionDate(String commissionDate) {
        this.commissionDate = commissionDate;
    }

    public String getSampleStatus() {
        return sampleStatus;
    }

    public void setSampleStatus(String sampleStatus) {
        this.sampleStatus = sampleStatus;
    }

    public String getFlowInfoId() {
        return flowInfoId;
    }

    public void setFlowInfoId(String flowInfoId) {
        this.flowInfoId = flowInfoId;
    }

    public String getDetectionAgencyMemberCode() {
        return detectionAgencyMemberCode;
    }

    public void setDetectionAgencyMemberCode(String detectionAgencyMemberCode) {
        this.detectionAgencyMemberCode = detectionAgencyMemberCode;
    }

    public int getSamplingMethod() {
        return samplingMethod;
    }

    public void setSamplingMethod(int samplingMethod) {
        this.samplingMethod = samplingMethod;
    }

    public String getAppointmentSamplingTime() {
        return appointmentSamplingTime;
    }

    public void setAppointmentSamplingTime(String appointmentSamplingTime) {
        this.appointmentSamplingTime = appointmentSamplingTime;
    }

    public int getSamplingLocation() {
        return samplingLocation;
    }

    public void setSamplingLocation(int samplingLocation) {
        this.samplingLocation = samplingLocation;
    }

    public String getSamplingProcess() {
        return samplingProcess;
    }

    public void setSamplingProcess(String samplingProcess) {
        this.samplingProcess = samplingProcess;
    }

    public String getSamplerCerNo() {
        return samplerCerNo;
    }

    public void setSamplerCerNo(String samplerCerNo) {
        this.samplerCerNo = samplerCerNo;
    }

    public String getCcNo() {
        return ccNo;
    }

    public void setCcNo(String ccNo) {
        this.ccNo = ccNo;
    }

    public static class SampleLocation implements IPickerViewData {
        private String samplingLocationName;

        public String getSamplingLocationName() {
            return samplingLocationName;
        }

        public void setSamplingLocationName(String samplingLocationName) {
            this.samplingLocationName = samplingLocationName;
        }

        @Override
        public String getPickerViewText() {
            return samplingLocationName;
        }
    }
    public static class CCNoBean{
        @SerializedName("TWNo")
        String ccNoStr;

        public String getCcNoStr() {
            return ccNoStr;
        }

        public void setCcNoStr(String ccNoStr) {
            this.ccNoStr = ccNoStr;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestingCommissionBean bean = (TestingCommissionBean) o;
        return samplingMethod == bean.samplingMethod &&
                samplingLocation == bean.samplingLocation &&
                Objects.equals(id, bean.id) &&
                Objects.equals(sampleName, bean.sampleName) &&
                Objects.equals(sampler, bean.sampler) &&
                Objects.equals(samplingTime, bean.samplingTime) &&
                Objects.equals(consignUnit, bean.consignUnit) &&
                Objects.equals(principal, bean.principal) &&
                Objects.equals(commissionDate, bean.commissionDate) &&
                Objects.equals(sampleStatus, bean.sampleStatus) &&
                Objects.equals(flowInfoId, bean.flowInfoId) &&
                Objects.equals(detectionAgencyMemberCode, bean.detectionAgencyMemberCode) &&
                Objects.equals(appointmentSamplingTime, bean.appointmentSamplingTime) &&
                Objects.equals(samplingProcess, bean.samplingProcess) &&
                Objects.equals(samplerCerNo, bean.samplerCerNo) &&
                Objects.equals(ccNo, bean.ccNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sampleName, sampler, samplingTime, consignUnit, principal, commissionDate, sampleStatus, flowInfoId, detectionAgencyMemberCode, samplingMethod, appointmentSamplingTime, samplingLocation, samplingProcess, samplerCerNo, ccNo);
    }
}
