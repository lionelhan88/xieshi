package com.lessu.uikit.tableview;

import com.scetia.Pro.common.Util.DensityUtil;
import com.lessu.uikit.easy.EasyUI;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableViewCell extends FrameLayout{

	protected TableViewCellStyle style;
	public TextView titleView;
	public TextView detailTextView;
	
	public TableViewCell(Context context,TableViewCellStyle style) {
		super(context);
		this.style = style;
		this.init();
	
	}
	
	public TableViewCell(Context context) {
		this(context,TableViewCellStyle.Default);
		
	}
	protected void init(){
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, DensityUtil.dip2px(getContext(), 44));
		this.setLayoutParams(layoutParams);
		this.setPadding(DensityUtil.dip2px(getContext(), 20), 0, 0, 0);
		if(this.style == TableViewCellStyle.Default){
			LinearLayout contentLayout = new LinearLayout(getContext());
			contentLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			contentLayout.setOrientation(LinearLayout.HORIZONTAL);
			this.addView(contentLayout);
			
			
			titleView = new TextView(getContext());
			titleView.setLayoutParams(EasyUI.fillParentLayout);
			titleView.setTextSize(17);
            titleView.setGravity(Gravity.CENTER_VERTICAL);
			contentLayout.addView(titleView);
			
			
			
		}else if(this.style == TableViewCellStyle.RightDetail){
			LinearLayout contentLayout = new LinearLayout(getContext());
			contentLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			contentLayout.setOrientation(LinearLayout.HORIZONTAL);
			this.addView(contentLayout);
			
			titleView = new TextView(getContext());
			titleView.setLayoutParams(EasyUI.scaleWidthFillHeightLayout);
			titleView.setTextSize(17);
            titleView.setGravity(Gravity.CENTER_VERTICAL);
			contentLayout.addView(titleView);
			
			detailTextView = new TextView(getContext());
			detailTextView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT));
			detailTextView.setPadding(0, 0, DensityUtil.dip2px(getContext(), 8), 0);
			detailTextView.setTextSize(17);
			detailTextView.setGravity(Gravity.CENTER_VERTICAL);
			detailTextView.setTextColor(Color.GRAY);
			contentLayout.addView(detailTextView);
		}

	}



    public enum TableViewCellStyle{
        Default,
        Subtitle,
        RightDetail,
        LeftDetail
    }
	
}
