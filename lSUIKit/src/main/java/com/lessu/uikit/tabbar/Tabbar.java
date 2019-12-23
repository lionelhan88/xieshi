package com.lessu.uikit.tabbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.widget.LinearLayout;

public class Tabbar extends LinearLayout{

	public Tabbar(Context context) {
		super(context);
		this.setOrientation(HORIZONTAL);
		this.setBackgroundColor(0xFFEEEEEE);
	}
	@Override
	protected void onDraw(Canvas canvas) {

		
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		paint.setColor(0xFFAAAAAA);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1);
		canvas.drawLine(0, 0, getWidth(), 0, paint);
		paint.setColor(0xFFFFFFFF);
		canvas.drawLine(0, 1, getWidth(), 1, paint);
	}
	
}
