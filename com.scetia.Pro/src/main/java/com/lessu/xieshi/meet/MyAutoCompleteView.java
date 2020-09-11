package com.lessu.xieshi.meet;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

/**
 * created by ljs
 * on 2020/8/26
 */
public class MyAutoCompleteView extends android.support.v7.widget.AppCompatAutoCompleteTextView {


    public MyAutoCompleteView(Context context) {
        super(context);
    }

    public MyAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAutoCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }
}
