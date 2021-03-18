package com.scetia.Pro.baseapp.uitls;

/**
 * created by ljs
 * on 2020/11/19
 */
public enum LoadState {
    LOAD_INIT(200,"正在加载数据..."),
    LOAD_INIT_SUCCESS(200,"加载数据成功"),
    LOADING("正在加载数据..."),
    SUCCESS(200,"加载数据成功"),
    EMPTY(200,"暂无数据！"),
    FAILURE(),
    NO_MORE(200,"没有更多数据了");

    private int code;
    private String message;
    LoadState() {
    }

    LoadState(String message) {
        this.message = message;
    }

    LoadState(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public LoadState setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getCode() {
        return code;
    }

    public LoadState setCode(int code) {
        this.code = code;
        return this;
    }
}
