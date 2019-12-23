package com.lessu.cells;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lessu.uikit.R;

/**
 * Created by lessu on 14-8-1.
 */
public class SelectStyleableCell extends LinearLayout{
    int normalStatusColor;
    int highLightStatusColor;
    ViewGroup wrapper;
    public SelectStyleableCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SelectStyleableCell);

        normalStatusColor = array.getColor(R.styleable.SelectStyleableCell_normal_background, Color.TRANSPARENT);
        highLightStatusColor = array.getColor(R.styleable.SelectStyleableCell_hightlight_background,R.color.hight_light_color);

        this.setBackgroundColor(normalStatusColor);

        final SelectStyleableCell self = this;
//        wrapper = new FrameLayout(getContext());
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int i = 0;
//        i++;
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                onCellTouchDown();
//                //			       setImageResource(m_touch_state_image);
//                break;
//            case MotionEvent.ACTION_UP:
//                onCellTouchUp();
//                //			       setImageResource(m_normal_state_image);
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                onCellTouchUp();
//                break;
//            default:
//                break;
//        }
//
//
//        return super.onTouchEvent(event);
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    protected void onCellTouchDown(){
        this.setBackgroundColor(highLightStatusColor);
    }
    private void onCellTouchUp() {
        this.setBackgroundColor(normalStatusColor);
    }

}
