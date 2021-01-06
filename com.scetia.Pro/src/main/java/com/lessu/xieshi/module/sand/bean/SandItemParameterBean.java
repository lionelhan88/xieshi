package com.lessu.xieshi.module.sand.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.HashMap;
import java.util.List;

/**
 * created by ljs
 * on 2020/12/24
 * 建设用砂检测参数实体
 */
public class SandItemParameterBean implements IPickerViewData {
    /**
     * parameterID : 110304101
     * parameterName : 细度模数
     * isDefault : true
     * mustBeDetectFlag : 1
     * groupDetectFlag : 1
     * rejectDetectFlag : 0
     */

    private String parameterID;
    private String parameterName;
    private boolean isDefault;
    private int mustBeDetectFlag;
    private int groupDetectFlag;
    private int rejectDetectFlag;
    private boolean isSelect;

    private int selectedNum = 0;

    public int getSelectedNum() {
        return selectedNum;
    }

    public void setSelectedNum(int selectedNum) {
        this.selectedNum = selectedNum;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getParameterID() {
        return parameterID;
    }

    public void setParameterID(String parameterID) {
        this.parameterID = parameterID;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public int getMustBeDetectFlag() {
        return mustBeDetectFlag;
    }

    public void setMustBeDetectFlag(int mustBeDetectFlag) {
        this.mustBeDetectFlag = mustBeDetectFlag;
    }

    public int getGroupDetectFlag() {
        return groupDetectFlag;
    }

    public void setGroupDetectFlag(int groupDetectFlag) {
        this.groupDetectFlag = groupDetectFlag;
    }

    public int getRejectDetectFlag() {
        return rejectDetectFlag;
    }

    public void setRejectDetectFlag(int rejectDetectFlag) {
        this.rejectDetectFlag = rejectDetectFlag;
    }

    @Override
    public String getPickerViewText() {
        return parameterName;
    }

    /**
     * 转换成map集合
     */
    public static HashMap<String,SandItemParameterBean> parseToMap(List<SandItemParameterBean> sandItemParameterBeans){
        HashMap<String,SandItemParameterBean> map = new HashMap<>();
        for (SandItemParameterBean bean :sandItemParameterBeans){
            map.put(bean.getParameterID(),bean);
        }
        return map;
    }
}
