package com.lessu.uikit.Buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lessu.uikit.R;

public class ImageButton extends android.widget.ImageButton{

	private boolean isHighlight;
    protected int tintColor = 0xFF888888;

    public ImageButton(Context context,AttributeSet attrs) {
		super(context, attrs);
		final ImageButton self = this;
        setBackgroundColor(0);
		setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        self.onButtonTouchDown();
                        //			       setImageResource(m_touch_state_image);
                        break;
                    case MotionEvent.ACTION_UP:
                        self.onButtonTouchUp();
                        //			       setImageResource(m_normal_state_image);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        self.onButtonTouchUp();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

	}

	public ImageButton(Context context) {
		super(context);
		final ImageButton self = this;

		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()){
			       case MotionEvent.ACTION_DOWN:
			    	   self.onButtonTouchDown();
			    	   //			       setImageResource(m_touch_state_image);
			       break;
			       case MotionEvent.ACTION_UP:
			    	   self.onButtonTouchUp();
			    	   //			       setImageResource(m_normal_state_image);
			       break;
			       case MotionEvent.ACTION_CANCEL:
			    	   self.onButtonTouchUp();
			    	   break;
			       default:
			       break;
			    }
			    return false;
			}
		});

	}
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)


    @Override
	protected void onDraw(Canvas canvas) {

        if(this.getDrawable() != null) {
            if (isHighlight) {
                this.getDrawable().setColorFilter(tintColor, Mode.MULTIPLY);
            } else {
                this.getDrawable().clearColorFilter();
            }
        }

        super.onDraw(canvas);
	}
	public void onButtonTouchDown(){
		isHighlight = true;
		this.postInvalidate();
	}
	
	public void onButtonTouchUp(){
		isHighlight = false;
		this.postInvalidate();
	}

}
