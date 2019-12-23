package com.lessu.xieshi.tianqi.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by fhm on 2017/9/29.
 */

public class MyScrollview  extends ScrollView {

    private int lastscrolly;

    public MyScrollview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public MyScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyScrollview(Context context) {
        super(context);
    }
    /**
     * 滑动事件
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 1);
    }

    private OnScrollListener listener;

    /**
     * 设置滑动距离监听器
     */
    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }
    // 滑动距离监听器
    public interface OnScrollListener{

        /**
         * 在滑动的时候调用，scrollY为已滑动的距离
         */
        void onScroll(int scrollY);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        //System.out.println("getscrolly..."+getScrollY());
       // System.out.println("dip2px..."+DensityUtil.dip2px(getContext(),getScrollY()));
       // System.out.println("px2dip..."+DensityUtil.px2dip(getContext(),getScrollY()));
        if(listener!=null&&lastscrolly!=getScrollY()){
            listener.onScroll(getScrollY());
        }
        lastscrolly = getScrollY();
    }
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//        return false;
//    }
}
