package com.lessu.xieshi.meet.event;

public class MeetingScanResult {
    private String scanResult="";

    public MeetingScanResult(String scanResult) {
        this.scanResult = scanResult;
    }

    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }
}
