package com.scetia.app_sand.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * created by ljs
 * on 2020/10/29
 */
public class FlowDeclarationBean {
/**
 *  "putOnRecordsPassport": "BW(砂)-01-20200063",
 *     "productionUnitName": "上海山中玉实业有限公司",
 *     "shipName": "船名（车号）船名（车号）123a!",
 *     "terminalName": "码头名称码头名称123a!",
 *     "sampleID": "110305",
 *     "specID": "110305303",
 *     "parameterIDs": "110305101;110305102;110305103;110305108;110305109;110305110;110305117",
 *     "customerUnitMemberCode": "0013",
 *     "SalesVolume": "900"
 */

    /**
     * id : 6f8812d8-1293-44fd-85dc-08d89a4c4428
     * flowInfoNo : 2020000001
     * customerUnitName : 上海锦奉混凝土有限公司
     * shipName : 船名（车号）船名（车号）123a!
     * salesVolume : 900
     * sampleName : 混合砂
     * flowInfoStatus : 未委托
     * createDatetime : 2020-12-07
     */



    private String id;
    private String flowInfoNo;
    private String customerUnitName;
    @Expose
    private String shipName;
    private String salesVolume;
    private String sampleName;
    private String flowInfoStatus;
    private String createDatetime;
    /**
     * putOnRecordsPassport : BW(砂)-01-20200063
     * productionUnitName : 上海山中玉实业有限公司
     * terminalName : 码头名称码头名称123a!
     * sampleID : 110305
     * specID : 110305303
     * parameterIDs : 110305101;110305102;110305103;110305108;110305109;110305110;110305117
     * customerUnitMemberCode : 0013
     * SalesVolume : 900
     */
    @Expose
    private String putOnRecordsPassport;
    @Expose
    private String productionUnitName;
    @Expose
    private String terminalName;
    @Expose
    private String sampleID;
    @Expose
    private String specID;
    @Expose
    private String parameterIDs;
    @Expose
    private String customerUnitMemberCode;
    @Expose
    @SerializedName("SalesVolume")
    private String SalesVolumePost;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlowInfoNo() {
        return flowInfoNo;
    }

    public void setFlowInfoNo(String flowInfoNo) {
        this.flowInfoNo = flowInfoNo;
    }

    public String getCustomerUnitName() {
        return customerUnitName;
    }

    public void setCustomerUnitName(String customerUnitName) {
        this.customerUnitName = customerUnitName;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getSalesVolumePost() {
        return SalesVolumePost;
    }

    public void setSalesVolumePost(String salesVolumePost) {
        SalesVolumePost = salesVolumePost;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getFlowInfoStatus() {
        return flowInfoStatus;
    }

    public void setFlowInfoStatus(String flowInfoStatus) {
        this.flowInfoStatus = flowInfoStatus;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getPutOnRecordsPassport() {
        return putOnRecordsPassport;
    }

    public void setPutOnRecordsPassport(String putOnRecordsPassport) {
        this.putOnRecordsPassport = putOnRecordsPassport;
    }

    public String getProductionUnitName() {
        return productionUnitName;
    }

    public void setProductionUnitName(String productionUnitName) {
        this.productionUnitName = productionUnitName;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getSampleID() {
        return sampleID;
    }

    public void setSampleID(String sampleID) {
        this.sampleID = sampleID;
    }

    public String getSpecID() {
        return specID;
    }

    public void setSpecID(String specID) {
        this.specID = specID;
    }

    public String getParameterIDs() {
        return parameterIDs;
    }

    public void setParameterIDs(String parameterIDs) {
        this.parameterIDs = parameterIDs;
    }

    public String getCustomerUnitMemberCode() {
        return customerUnitMemberCode;
    }

    public void setCustomerUnitMemberCode(String customerUnitMemberCode) {
        this.customerUnitMemberCode = customerUnitMemberCode;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowDeclarationBean that = (FlowDeclarationBean) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(flowInfoNo, that.flowInfoNo) &&
                Objects.equals(customerUnitName, that.customerUnitName) &&
                Objects.equals(shipName, that.shipName) &&
                Objects.equals(salesVolume, that.salesVolume) &&
                Objects.equals(sampleName, that.sampleName) &&
                Objects.equals(flowInfoStatus, that.flowInfoStatus) &&
                Objects.equals(createDatetime, that.createDatetime) &&
                Objects.equals(putOnRecordsPassport, that.putOnRecordsPassport) &&
                Objects.equals(productionUnitName, that.productionUnitName) &&
                Objects.equals(terminalName, that.terminalName) &&
                Objects.equals(sampleID, that.sampleID) &&
                Objects.equals(specID, that.specID) &&
                Objects.equals(parameterIDs, that.parameterIDs) &&
                Objects.equals(customerUnitMemberCode, that.customerUnitMemberCode) &&
                Objects.equals(SalesVolumePost, that.SalesVolumePost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flowInfoNo, customerUnitName, shipName, salesVolume, sampleName, flowInfoStatus, createDatetime, putOnRecordsPassport, productionUnitName, terminalName, sampleID, specID, parameterIDs, customerUnitMemberCode, SalesVolumePost);
    }
}
