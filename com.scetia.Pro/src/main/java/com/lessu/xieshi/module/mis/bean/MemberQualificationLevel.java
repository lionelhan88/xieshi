package com.lessu.xieshi.module.mis.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * created by Lollipop
 * on 2022/1/14
 */
public class MemberQualificationLevel {
    @SerializedName("LevelType")
    private String levelType;
    @SerializedName("BuildingMatcrial")
    private String buildingMatcrial;
    @SerializedName("BaiscFoundation")
    private String baiscFoundation;
    @SerializedName("MajorStructure")
    private String majorStructure;
    @SerializedName("SteelStructure")
    private String steelStructure;
    @SerializedName("IndoorClimate")
    private String indoorClimate;
    @SerializedName("EnergySavingOfBuilding")
    private String energySavingOfBuilding;
    @SerializedName("DoorWindowCurtainWall")
    private String doorWindowCurtainWall;
    @SerializedName("VentilatedAirCondition")
    private String ventilatedAirCondition;
    private boolean s1;
    private boolean s2;
    private boolean s3;
    private boolean s4;
    private boolean s5;

    public String getLevelType() {
        return levelType;
    }

    public String getBuildingMatcrial() {
        return buildingMatcrial;
    }

    public String getBaiscFoundation() {
        return baiscFoundation;
    }

    public String getMajorStructure() {
        return majorStructure;
    }

    public String getSteelStructure() {
        return steelStructure;
    }

    public String getIndoorClimate() {
        return indoorClimate;
    }

    public String getEnergySavingOfBuilding() {
        return energySavingOfBuilding;
    }

    public String getDoorWindowCurtainWall() {
        return doorWindowCurtainWall;
    }

    public String getVentilatedAirCondition() {
        return ventilatedAirCondition;
    }

    public Boolean getS1() {
        return s1;
    }

    public Boolean getS2() {
        return s2;
    }

    public Boolean getS3() {
        return s3;
    }

    public Boolean getS4() {
        return s4;
    }

    public Boolean getS5() {
        return s5;
    }
}
