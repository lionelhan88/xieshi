package com.scetia.Pro.network.bean;

import com.google.gson.annotations.SerializedName;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;
import com.scetia.Pro.network.response.IResultData;

/**
 * created by ljs
 * on 2020/11/25
 * 协实服务返回的完全数据实体类型，
 */
public class XSResultData<T> implements IResultData<T> {
    @SerializedName("Success")
    private boolean success;
    @SerializedName("Code")
    private int code;
    @SerializedName("Message")
    private String message;
    @SerializedName("Data")
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public T handleData() {
        if(success){
            return data;
        }else{
            throw new ExceptionHandle.ResultException(getCode(), getMessage());
        }
    }
}
