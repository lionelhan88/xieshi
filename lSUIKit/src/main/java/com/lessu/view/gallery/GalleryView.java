package com.lessu.view.gallery;

import android.content.Context;

import android.os.Handler;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;


import com.google.gson.GsonValidate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.lessu.uikit.easy.EasyUI;
import com.lessu.view.gallery.GalleryCell.GalleryCellOnClickListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
/**
 * Created by lessu on 14-7-2.
 */
public class GalleryView extends RelativeLayout implements OnPageChangeListener {

    private ArrayList<GalleryCell> galleryList;
    protected ViewPager viewPager;
    protected JsonArray data;
    protected String imageKey = "image_url";
    protected String titlekey = "title";
    
    private boolean showTitle = true;

	private OnItemClickListener onItemClickListener;
	private Timer switchTimer;
	private Handler switchHandle;
	
    //set to nagetive if don't want it to auto switch
    private int switchDuration= 0;
    
    private PagerAdapter pagerAdapter;
	private GalleryIndicator indicator;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void updateGallery(){
        galleryList.clear();
        if(data == null){
        	return ;
        }
        for (int i = 0 ; i < data.size() ;i++){
            final JsonObject item = data.get(i).getAsJsonObject();
            GalleryCell view = new GalleryCell(getContext());
            view.setLayoutParams(EasyUI.fillParentLayout);
            if (isShowTitle()){
            	view.setTitleString(GsonValidate.getStringByKeyPath(item,titlekey,"暂无标题"));
            }
            view.setImageUrl(GsonValidate.getStringByKeyPath(item, imageKey, ""));
            view.setShowTitle(isShowTitle());
            final int index = i;
            view.setOnClickListener(new GalleryCellOnClickListener() {
				
				@Override
				public void onClick(GalleryCell cell) {

					if(onItemClickListener != null){
						onItemClickListener.onItemClick(cell, index, item);
					}
					
				}
			});
            
            galleryList.add(view);
        }

        pagerAdapter.notifyDataSetChanged();
        indicator.setCount(data.size());
     
    }
    protected  void init(){
    	viewPager = new ViewPager(getContext());
    	viewPager.setLayoutParams(EasyUI.fillParentLayout);
    	this.addView(viewPager);
    	
        galleryList = new ArrayList<GalleryCell>();
        switchHandle = new Handler();
        
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return galleryList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(galleryList.get(position));
                return galleryList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(galleryList.get(position));

            }
        };
        viewPager.setAdapter(pagerAdapter);
        indicator = new GalleryIndicator(getContext());
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 0;
        layoutParams.alignWithParent= true;
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        indicator.setLayoutParams(layoutParams);
        this.addView(indicator);
        
        
        viewPager.setOnPageChangeListener(this);
//        this.setCurrentItem(0);
    }

//    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = super.dispatchTouchEvent(ev);
        if(ret && ev.getAction() == MotionEvent.ACTION_MOVE){
            try {
                float dX = ev.getX() - ev.getHistoricalX(ev.getHistorySize() - 1);
                float dY = ev.getY() - ev.getHistoricalY(ev.getHistorySize() - 1);
                if (dX == 0 &&dY == 0) return ret;
//                Log.d("Gallery View",String.format("X:%.2f Y:%.2f", dX,dY));
                if(Math.abs(dY)>=Math.abs(dX)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }catch (Exception e){
//                Log.d("Gallery View",String.format("History %d", ev.getHistorySize()));
            }
        }
//        if (ev.getAction() == MotionEvent.ACTION_CANCEL ||ev.getAction() == MotionEvent.ACTION_UP){
//            getParent().requestDisallowInterceptTouchEvent(false);
//        }
        return ret;
    }

    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
        updateGallery();

    }

    public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
		updateGallery();
	}

	private void setUpSwitchTimer(){
		if(switchTimer!=null){
			switchTimer.cancel();
			switchTimer.purge();
			switchTimer = null;
		}
		switchTimer = new Timer();
        
        if(this.getSwitchDuration() > 0){
	        switchTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					switchHandle.post(new Runnable() {
						@Override
						public void run() {
							viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % galleryList.size() , true);
						}
					});
				}
			}, this.getSwitchDuration() * 1000, this.getSwitchDuration() * 1000);
        }
	}
	@Override
	protected void onDetachedFromWindow() {
		switchTimer.cancel();
		super.onDetachedFromWindow();
	}
	@Override
	protected void onAttachedToWindow() {
		setUpSwitchTimer();
		super.onAttachedToWindow();
	}
	
    public int getSwitchDuration() {
		return switchDuration;
	}

	public void setSwitchDuration(int switchDuration) {
		this.switchDuration = switchDuration;
		
		setUpSwitchTimer();
	}
    
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageSelected(int arg0) {
		indicator.setCurrentIndex(arg0);
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public interface OnItemClickListener {
    	void onItemClick(GalleryCell cell,int index, JsonObject data);
    }

	
}
