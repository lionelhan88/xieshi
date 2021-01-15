package com.scetia.Pro.network.bean;

import com.google.gson.JsonObject;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;
import com.scetia.Pro.network.response.IResultData;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/18
 */
public class BuildSandResultData<T> implements IResultData<T> {

    /**
     * statusCode : 200
     * message : GET Request successful.
     * isError : false
     * result : [{"unitName":"上海腾达混凝土有限公司","unitType":"预拌混凝土、预拌砂浆","serialNo":"0319"}]
     */

    private int statusCode;
    private String message;
    private boolean isError;
    private T result;
    private JsonObject responseException;
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public JsonObject getResponseException() {
        return responseException;
    }

    public void setResponseException(JsonObject responseException) {
        this.responseException = responseException;
    }

    @Override
    public T handleData() {
        return getResult();
       /* if(getStatusCode()==200||getStatusCode()==201){
            return getResult();
        } else if(getStatusCode()==500){
            String exceptionMessage = responseException.get("exceptionMessage").getAsString();
            //TODO:直接抛出异常，返回失败数据
            throw new ExceptionHandle.ResultException(getStatusCode(),exceptionMessage);
        } else{
            //TODO:直接抛出异常，返回失败数据
            throw new ExceptionHandle.ResultException(getStatusCode(),getMessage());
        }*/
    }
}
