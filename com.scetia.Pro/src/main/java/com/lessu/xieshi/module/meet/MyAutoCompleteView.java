package com.lessu.xieshi.module.meet;

import android.content.Context;
import android.util.AttributeSet;

/**
 * created by ljs
 * on 2020/8/26
 */
public class MyAutoCompleteView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {


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
