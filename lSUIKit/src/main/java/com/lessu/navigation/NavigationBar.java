package com.lessu.navigation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scetia.Pro.common.Util.DensityUtil;

/**
 * Created by lessu on 14-7-31.
 */
public class NavigationBar extends RelativeLayout {
    protected int barTintColor;
    protected int tintColor;
    private LinearLayout leftBarLayout;
    private LinearLayout middleBarLayout;
    private LinearLayout rightBarLayout;
    private TextView titleView;
    private Paint paint;

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationBar(Context context) {
        super(context);
        init();
    }

    public void init(){
        this.barTintColor = 0xFFF0F0F0;
        this.tintColor    = Color.WHITE;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getContext(), 49));
        this.setLayoutParams(layoutParams);
        this.setBackgroundColor(this.barTintColor);


        leftBarLayout = new LinearLayout(getContext());
        middleBarLayout = new LinearLayout(getContext());
        rightBarLayout = new LinearLayout(getContext());

        LayoutParams leftLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        leftLayoutParams.alignWithParent = true;
        leftLayoutParams.addRule(ALIGN_PARENT_LEFT);

        leftBarLayout.setLayoutParams(leftLayoutParams);
        leftBarLayout.setMinimumWidth(DensityUtil.dip2px(getContext(), 15));
        leftBarLayout.setGravity(Gravity.CENTER_HORIZONTAL);


        LayoutParams middleLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        middleBarLayout.setLayoutParams(middleLayoutParams);

        LayoutParams rightLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rightLayoutParams.alignWithParent = true;
        rightLayoutParams.addRule(ALIGN_PARENT_RIGHT);
        rightBarLayout.setLayoutParams(rightLayoutParams);
        rightBarLayout.setMinimumWidth(DensityUtil.dip2px(getContext(),15));
        rightBarLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        this.addView(leftBarLayout);
        this.addView(middleBarLayout);
        this.addView(rightBarLayout);


        titleView = new TextView(getContext());
        titleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        titleView.setTextColor(tintColor);
        titleView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        titleView.setTextSize(18);

        middleBarLayout.addView(titleView);

        paint = new Paint();

    }

    CharSequence        title;
    public void setTitle(CharSequence text){
    	if(titleView.getParent() != middleBarLayout){
    		middleBarLayout.removeAllViews();
    		middleBarLayout.addView(titleView);
    	}
        title = text;
        titleView.setText(text);
    }
    public CharSequence getTitle() {
        return title;
    }

    public void setLeftBarItem(BarButtonItem item){
    	if(item == null){
    		leftBarLayout.removeAllViews();
    	}else{
    		leftBarLayout.removeAllViews();
	        leftBarLayout.addView(item);
	        item.setTintColor(tintColor);
    	}
    }

	public void setRightBarItem(BarButtonItem item){
		if(item == null){
			rightBarLayout.removeAllViews();
		}else{
			rightBarLayout.removeAllViews();
	        rightBarLayout.addView(item);
	        item.setTintColor(tintColor);
		}
	}
    public void addRightBarItem(BarButtonItem item){
        if(item == null){
            rightBarLayout.removeAllViews();
        }else{
            rightBarLayout.addView(item);
            item.setTintColor(tintColor);
        }
    }
}
