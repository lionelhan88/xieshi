package com.lessu.xieshi.Utils;

/**
 * Created by fhm on 2016/12/14.
 */

public class PicSize {
    public static Double getpicsize(int w,int h){
        int ww=1024;
        double be = 0;
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be=w/ww;
        } else if (w < h && h > ww) {//如果高度高的话根据宽度固定大小缩放
            be=h/ww;
        }
        if (be <= 0) be = 1;
        return be;
    }
}
