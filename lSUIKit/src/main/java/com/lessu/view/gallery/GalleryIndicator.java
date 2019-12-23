package com.lessu.view.gallery;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

import com.lessu.uikit.Utils;

public class GalleryIndicator extends View{

	private int count;
	private int currentIndex;
	private Paint fillPaint;
	int INDICATOR_ITEM_SIZE 		= 8;
	int INDICATOR_ITEM_PADDING 		= 4;
	static int INDICATOR_ITEM_FILL_COLOR= Color.WHITE;
	public GalleryIndicator(Context context) {
		super(context);
		fillPaint = new Paint();
		fillPaint.setColor(INDICATOR_ITEM_FILL_COLOR);
		fillPaint.setStrokeWidth(Utils.dip2px(getContext(), 1));
		fillPaint.setAntiAlias(true);
//		invalidate();
		
		INDICATOR_ITEM_SIZE = Utils.dip2px(getContext(), INDICATOR_ITEM_SIZE);
		INDICATOR_ITEM_PADDING = Utils.dip2px(getContext(), INDICATOR_ITEM_PADDING);
		setBackgroundColor(Color.TRANSPARENT);
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
		invalidate();
	}
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		
		int itemWidth = getCount() * (INDICATOR_ITEM_PADDING * 2 + INDICATOR_ITEM_SIZE);
		int startX    = (width - itemWidth) / 2;
		for( int i = 0 ; i < getCount() ; i ++){
			if (i == currentIndex){
				fillPaint.setStyle(Style.FILL_AND_STROKE);
			}else{
				fillPaint.setStyle(Style.STROKE);
			}
			canvas.drawCircle(startX + INDICATOR_ITEM_PADDING + (INDICATOR_ITEM_PADDING *2 + INDICATOR_ITEM_SIZE) * i, height / 2, INDICATOR_ITEM_SIZE / 2 , fillPaint);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int desiredHeight = INDICATOR_ITEM_SIZE * 2;
		int desiredWidth  = getCount() * (INDICATOR_ITEM_PADDING * 2 + INDICATOR_ITEM_SIZE);
		
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int height=MeasureSpec.getSize(heightMeasureSpec);
		int width=MeasureSpec.getSize(widthMeasureSpec);
		
		if (mode == MeasureSpec.AT_MOST){
			height = Math.min(desiredHeight, height);
//			Log.d("height onMeasure", String.format("AT_MOST:%d",height));
		}else if (mode == MeasureSpec.EXACTLY){
//			Log.d("height onMeasure", String.format("EXACTLY:%d",height));
		}else if (mode == MeasureSpec.UNSPECIFIED){
			height = desiredHeight;
//			Log.d("height onMeasure", String.format("UNSPECIFIED:%d",height));
		}
		mode = MeasureSpec.getMode(widthMeasureSpec);
		
		if (mode == MeasureSpec.AT_MOST){
			width = Math.max(desiredWidth, width);
//			Log.d("width onMeasure", String.format("AT_MOST:%d",width));
		}else if (mode == MeasureSpec.EXACTLY){
//			Log.d("width onMeasure", String.format("EXACTLY:%d",width));
		}else if (mode == MeasureSpec.UNSPECIFIED){
			width = desiredWidth;
//			Log.d("width onMeasure", String.format("UNSPECIFIED:%d",width));
		}
		setMeasuredDimension(width, height);
	}
}
