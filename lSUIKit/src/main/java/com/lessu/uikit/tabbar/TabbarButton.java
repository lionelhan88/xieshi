package com.lessu.uikit.tabbar;

import com.scetia.Pro.common.Util.DensityUtil;
import com.lessu.uikit.Buttons.Button;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;

public class TabbarButton extends Button{

	public TabbarButton(Context context){
		super(context);
//        this.setPadding(10,10,10,10);
		this.setTextPosition(TextPosition.TextPositionBottom);
		this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		
		this.setTitleColor(0xFF888888, ButtonStatus.ButtonStatusNormal);
		this.setTitleColor(0xFF22D0A1, ButtonStatus.ButtonStatusHighlight);
		this.setTitleColor(0xFF22D0A1, ButtonStatus.ButtonStatusSelected);
	}
	
	public void setUpTabbarButton(String title,int unselectedId,int selectedId){
		this.setImage(unselectedId, ButtonStatus.ButtonStatusNormal);
		this.setImage(selectedId, ButtonStatus.ButtonStatusHighlight);
		this.setImage(selectedId, ButtonStatus.ButtonStatusSelected);
		
		this.setTitle(title);
	}
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//

//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
//		this.
//	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {

		super.onLayout(changed, left, top, right, bottom);
		if (changed){
			int size = (int) (bottom - top - this.getTextSize() - DensityUtil.dip2px(getContext(), 4)) - 10;
			
			this.imageBounds = new Rect(0 , 5 + DensityUtil.dip2px(getContext(), 2), size , size);

			this.refresh();
		}
	}
	
}
