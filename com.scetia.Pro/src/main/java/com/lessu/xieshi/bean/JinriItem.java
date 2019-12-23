package com.lessu.xieshi.bean;

/**
 * Created by fhm on 2016/9/21.
 */
public class JinriItem {
    public String itemName;
    public String itemSampleCount;
    public String itemSampleFinishCount;
    public String itemBhgCount;
    public String itemNoExam;

    public JinriItem(String itemName, String itemSampleCount, String itemSampleFinishCount, String itemBhgCount,String itemNoExam) {
        this.itemName = itemName;
        this.itemSampleCount = itemSampleCount;
        this.itemSampleFinishCount = itemSampleFinishCount;
        this.itemBhgCount = itemBhgCount;
        this.itemNoExam=itemNoExam;
    }
}
