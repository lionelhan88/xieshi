package com.lessu.xieshi.module.mis.bean;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

/**
 * Created by fhm on 2017/10/10.
 */

public class MisGuideBean {
    public int pic;
    public String text;
    public Class<? extends FragmentActivity> clazz;

    public MisGuideBean(int pic, String text, Class<? extends FragmentActivity> clazz) {
        this.pic = pic;
        this.text = text;
        this.clazz = clazz;
    }
}
