package com.scetia.app_sand.bean;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * created by ljs
 * on 2020/12/24
 * 建设用砂样品实体
 */
public class SandSampleBean  implements IPickerViewData {

    /**
     * sampleId : 110304
     * sampleName : 人工砂
     */

    private String sampleId;
    private String sampleName;

    public SandSampleBean(String sampleId, String sampleName) {
        this.sampleId = sampleId;
        this.sampleName = sampleName;
    }

    public SandSampleBean() {
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    @Override
    public String getPickerViewText() {
        return sampleName;
    }
}
