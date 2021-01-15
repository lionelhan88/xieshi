package com.lessu.navigation;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by lessu on 14-7-31.
 */
public abstract class BarItem extends LinearLayout {
    protected String title;

    public BarItem(Context context) {
        super(context);
    }

}
