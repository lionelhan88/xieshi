package com.lessu.xieshi.module.mis.bean;

/**
 * Created by fhm on 2017/10/13.
 */

public class Misnjbean {

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

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public int getData() {
        return Data;
    }

    public void setData(int Data) {
        this.Data = Data;
    }
}
