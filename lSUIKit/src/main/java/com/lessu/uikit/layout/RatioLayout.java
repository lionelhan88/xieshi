package com.lessu.uikit.layout;



import com.lessu.uikit.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class RatioLayout extends ViewGroup{
	public final static int ADJUST_WIDTH = 0;
	public final static int AADJUST_HEIGHT= 1;
	private float ratio;
	private int adjustFor;
	
	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	      TypedArray tArray = context.obtainStyledAttributes(attrs,R.styleable.RatioLayout);
	        ratio = tArray.getFloat(R.styleable.RatioLayout_ratio, 0);
	        
	        adjustFor = tArray.getInt(R.styleable.RatioLayout_ratio_adjust_for, 200);
	        
	        tArray.recycle();
	}

//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		getChildAt(0);
//	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(ratio<=0){
//			super.onMeasure(widthMeasureSpec, heightMeasureSpec);	
		}else{
			getMeasuredHeight();
			getMeasuredWidth();
			int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
			int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
			
			int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
			int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
			
			
			int measuredWidth = widthSpecSize;
			int measuredHeight = heightSpecSize;
			
//			if(widthSpecMode == MeasureSpec.EXACTLY){
//				Log.i("ratioLayout:", String.format("width EXACTLY :%d", widthSpecSize));
//			}else if (widthSpecMode == MeasureSpec.AT_MOST){
//				Log.i("ratioLayout:", String.format("width AT_MOST :%d", widthSpecSize));			
//			}else if (widthSpecMode == MeasureSpec.UNSPECIFIED){
//				Log.i("ratioLayout:", String.format("width UNSPECIFIED :%d", widthSpecSize));
//			}
//			
//			if(heightSpecMode == MeasureSpec.EXACTLY){
//				Log.i("ratioLayout:", String.format("height EXACTLY :%d", heightSpecSize));
//			}else if (heightSpecMode == MeasureSpec.AT_MOST){
//				Log.i("ratioLayout:", String.format("height EXACTLY :%d", heightSpecSize));
//			}else if (heightSpecMode == MeasureSpec.UNSPECIFIED){
//				Log.i("ratioLayout:", String.format("height EXACTLY :%d", heightSpecSize));
//			}
	        measureChildren(widthMeasureSpec, heightMeasureSpec);
	        View child = getChildAt(0);
	        if (adjustFor == ADJUST_WIDTH) {
	        	int height = child.getMeasuredHeight();
	        	int width  = (int) (height / ratio);
	        	setMeasuredDimension(width, height);	
			}else{
				int width  = child.getMeasuredWidth();
				int height = (int) (width * ratio);
	        	setMeasuredDimension(width, height);
			}
//			setMeasuredDimension(measuredWidth, measuredHeight);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View childView = getChildAt(0);
		childView.layout(0, 0, r - l, b - t);
	}

}
