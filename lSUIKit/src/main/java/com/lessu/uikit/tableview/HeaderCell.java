package com.lessu.uikit.tableview;

import com.scetia.Pro.common.Util.DensityUtil;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HeaderCell extends LinearLayout{
	
	TextView titleTextView;
	public HeaderCell(Context context) {
		super(context);

		this.setOrientation(HORIZONTAL);
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, DensityUtil.dip2px(getContext(), 44));
		this.setLayoutParams(layoutParams);
		
		
		titleTextView = new TextView(context);
		titleTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));

		this.addView(titleTextView);
//		setBackgroundColor(Color.RED);
	}
	
	public void setText(CharSequence title){
		titleTextView.setText(title);
	}
	public CharSequence getText(){
		return titleTextView.getText();
	}
	
	

}
