package com.lessu.xieshi.bean;

import com.lessu.data.LoadState;

/**
 * created by ljs
 * on 2020/12/8
 * 需要不同的加载形态
 */
public class LoadMoreState {
    //加载不同的提示
    public int loadType;
    public LoadState loadState;
    public LoadMoreState(int loadType, LoadState loadState) {
        this.loadType = loadType;
        this.loadState = loadState;
    }

    public LoadMoreState() {
    }
}
