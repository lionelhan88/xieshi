package com.lessu.xieshi;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ListViewCell extends LinearLayout{
	
	
	public ListViewCell(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ListViewCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public ListViewCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	private int position;
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
}
