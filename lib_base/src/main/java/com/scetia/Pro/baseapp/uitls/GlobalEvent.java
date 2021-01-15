package com.scetia.Pro.baseapp.uitls;

/**
 * created by ljs
 * on 2020/12/11
 */
public class GlobalEvent<T>{

    private int code;
    private T data;

    public GlobalEvent(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }
}
