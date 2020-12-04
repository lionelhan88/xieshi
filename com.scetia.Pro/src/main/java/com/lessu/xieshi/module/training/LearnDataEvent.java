package com.lessu.xieshi.module.training;

import com.lessu.xieshi.bean.PaidItem;
import com.lessu.xieshi.bean.PushToDx;

import java.util.List;
import java.util.Map;

public class LearnDataEvent {
    private PushToDx pushToDx;
    private Map<String,String> paidItemMap;
    private List<PaidItem> paidItems;

    public LearnDataEvent(PushToDx pushToDx, Map<String, String> paidItemMap) {
        this.pushToDx = pushToDx;
        this.paidItemMap = paidItemMap;
    }

    public LearnDataEvent(PushToDx pushToDx, Map<String, String> paidItemMap, List<PaidItem> paidItems) {
        this.pushToDx = pushToDx;
        this.paidItemMap = paidItemMap;
        this.paidItems = paidItems;
    }

    public PushToDx getPushToDx() {
        return pushToDx;
    }

    public void setPushToDx(PushToDx pushToDx) {
        this.pushToDx = pushToDx;
    }

    public Map<String, String> getPaidItemMap() {
        return paidItemMap;
    }

    public void setPaidItemMap(Map<String, String> paidItemMap) {
        this.paidItemMap = paidItemMap;
    }

    public List<PaidItem> getPaidItems() {
        return paidItems;
    }

    public void setPaidItems(List<PaidItem> paidItems) {
        this.paidItems = paidItems;
    }
}
