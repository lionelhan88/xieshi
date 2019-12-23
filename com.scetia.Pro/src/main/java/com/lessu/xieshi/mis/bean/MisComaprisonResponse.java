package com.lessu.xieshi.mis.bean;

/**
 * Created by fhm on 2018/10/18.
 * 比对审批批准返回数据实体类
 */



public class MisComaprisonResponse {
    /**
     * Success : true
     * Code : 1000
     * Message :
     * Data : 1
     */

    private boolean Success;
    private int Code;
    private String Message;
    private int Data;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getData() {
        return Data;
    }

    public void setData(int data) {
        Data = data;
    }
}
