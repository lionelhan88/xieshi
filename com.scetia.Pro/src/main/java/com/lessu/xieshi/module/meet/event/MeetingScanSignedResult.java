package com.lessu.xieshi.module.meet.event;

public class MeetingScanSignedResult {
    private boolean isRefresh = false;

    public MeetingScanSignedResult(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
