package com.lessu.navigation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scetia.Pro.common.Util.DensityUtil;
import com.lessu.uikit.Buttons.Button;

/**
 * Created by lessu on 14-7-31.
 * <p>
 * <p>
 * edited by lessu on 15-3-4
 * v1.1
 * support image bar item
 * custom view is not tested
 */
public class BarButtonItem extends BarItem {
    private float imageSizeDp = 16;
    protected float width;
    protected View customView;
    //view
    protected Button titleButton;

    protected int tintColor;

    private void barButtonItemInit() {
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT));


        titleButton = new Button(getContext());
        titleButton.setTextSize(14);

        titleButton.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
        layoutParams.leftMargin = DensityUtil.dip2px(getContext(), 12);
        layoutParams.rightMargin = DensityUtil.dip2px(getContext(), 12);
        titleButton.setLayoutParams(layoutParams);
        titleButton.setImageHeight(DensityUtil.dip2px(getContext(), imageSizeDp));
        titleButton.setTitleColor(Color.BLUE, Button.ButtonStatus.ButtonStatusHighlight);
        this.addView(titleButton);
    }

    public BarButtonItem(Context context, String title) {
        super(context);
        barButtonItemInit();

        this.title = title;
        titleButton.setTitle(title, Button.ButtonStatus.ButtonStatusNormal);
    }

    public BarButtonItem(Context context, int imageResourceId) {
        super(context);
        barButtonItemInit();
        titleButton.setTitle(null);
        titleButton.setImage(imageResourceId);

        LinearLayout.LayoutParams layoutParams = (LayoutParams) titleButton.getLayoutParams();
        layoutParams.width = titleButton.getImageBounds().right;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        titleButton.setLayoutParams(layoutParams);
    }

    public void setOnClickMethod(Object receiver, String method) {
        titleButton.setOnClickMethod(receiver, method);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        titleButton.setOnClickListener(l);
    }

    public void setCustomView(View customView) {

        if (customView != null) {
            this.addView(customView);
            this.removeView(titleButton);
        } else {
            this.removeView(customView);
            this.addView(titleButton);
        }
        this.customView = customView;

    }

    public View getCustomView() {
        return customView;
    }

    public Button getTitleButton() {
        return titleButton;
    }


    public int getTintColor() {
        return tintColor;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
        titleButton.setTitleColor(tintColor, Button.ButtonStatus.ButtonStatusNormal);
    }

    public void setTitle(String title) {
        this.title = title;
        titleButton.setTitle(title, Button.ButtonStatus.ButtonStatusNormal);
    }

    public String getTitle() {
        return title;
    }

    public static BarButtonItem backBarButtonItem(Context context) {
        BarButtonItem backItem = new BarButtonItem(context, "返回");
        backItem.titleButton.setImageBounds(new Rect(10, 0, 30, 34));
        return backItem;
    }

}
