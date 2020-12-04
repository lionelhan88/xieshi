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
  /*  private void setChildBackground(){
        int backapah = (int) (255 - 255 * m1 / m2);
        int allapha = (int) (255 * m1 / m2);
        if (allapha > 255) {
            allapha = 255;
        }
        if (allapha < 0) {
            allapha = 0;
        }
        if (backapah > 255) {
            backapah = 255;
        }
        if (backapah < 0) {
            backapah = 0;
        }
        if(getChildCount()==0) return;
        RelativeLayout layout = (RelativeLayout) getChildAt(0);
        LinearLayout llTqAll = (LinearLayout) layout.getChildAt(0);
        llTqAll.getBackground().setAlpha(allapha);
        LinearLayout llTqBackfround = llTqAll.findViewById(R.id.llto)

        llTqBackfround.getBackground().setAlpha(backapah);
        llTqShenlanse.getBackground().setAlpha(backapah);
        llTqAddparent.setAlpha((float) (m1 / m2));
        dfTq.setAlpha((float) (m1 / m2));
        if (scTq.getScrollY() <= 0) {
            llTqAll.getBackground().setAlpha(0);
            llTqBackfround.getBackground().setAlpha(255);
            llTqShenlanse.getBackground().setAlpha(255);
        }
        if (scTq.getScrollY() >= indexm) {
            dfTq.setAlpha(1f);
            llTqBackfround.getBackground().setAlpha(0);
            llTqAll.getBackground().setAlpha(255);
            llTqShenlanse.getBackground().setAlpha(0);
        }
    }*/
}
