package com.lessu.uikit.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsoluteLayout;

import com.lessu.uikit.easy.EasyUI;

/**
 * Created by lessu on 14-7-15.
 */
public class SliderWrapperView extends AbsoluteLayout {

    private View wrappedView;
    private View menuView;


    protected int itemOffsetX = 0;
    public SliderWrapperView(Context context) {
        super(context);
    }

    public SliderWrapperView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMenuView(View view){
        menuView = view;
        this.addView(menuView);

        LayoutParams layoutParams = new LayoutParams(menuView.getLayoutParams());
        layoutParams.x = this.getWidth() + itemOffsetX;
        menuView.setLayoutParams(layoutParams);

    }
    public void wrap(View view){
        wrappedView = view;
        this.addView(wrappedView);

        LayoutParams layoutParams = new LayoutParams(wrappedView.getLayoutParams());
        layoutParams.x = itemOffsetX;
        wrappedView.setLayoutParams(layoutParams);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed){
            setItemOffsetX(itemOffsetX);
        }
    }

    protected void setItemOffsetX(int itemOffsetX){
        this.itemOffsetX = itemOffsetX;
        LayoutParams layoutParams1 = new LayoutParams(menuView.getLayoutParams());
        layoutParams1.x = this.getWidth() + itemOffsetX;
        menuView.setLayoutParams(layoutParams1);

        LayoutParams layoutParams2 = new LayoutParams(wrappedView.getLayoutParams());
        layoutParams2.x = itemOffsetX;
        wrappedView.setLayoutParams(layoutParams2);

    }
}
