package com.lessu.xieshi.http;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/18
 */
public class BuildSandResultData<T> {


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
}
