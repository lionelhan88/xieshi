package com.lessu.xieshi.module.weather.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.lessu.xieshi.R;
import com.lessu.xieshi.module.mis.activitys.Content;

/**
 * Created by fhm on 2017/9/29.
 */

public class ScrollViewEx extends ScrollView {
    private int lastscrolly;
    public ScrollViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public ScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ScrollViewEx(Context context) {
        super(context);
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
        if(listener!=null&&lastscrolly!=getScrollY()){
            listener.onScroll(getScrollY());
        }
        lastscrolly = getScrollY();
    }
}
