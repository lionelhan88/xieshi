package com.lessu.xieshi.mis.bean;

/**
 * Created by fhm on 2017/10/12.
 */

public class Mispingubean {


    /**
     * Success : true
     * Code : 1000
     * Message :
     * Data : true
     */

    private boolean Success;
    private int Code;
    private String Message;
    private boolean Data;

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

    public boolean isData() {
        return Data;
    }

    public void setData(boolean Data) {
        this.Data = Data;
    }
}
