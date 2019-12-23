package com.lessu.xieshi;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class XieShiGestureListener extends SimpleOnGestureListener {
	private static final String TAG = "XieShiGestureListener";  
	  
	private HorizontalScrollViewEx hsvMain;
	int width ;
	int nowPositionX = 0;
	
    public XieShiGestureListener(HorizontalScrollViewEx comeHsvMain,int comeWidth) {  
    	hsvMain = comeHsvMain;
    	width = comeWidth;
    }  
  
    /** 
     * 双击的第二下Touch down时触发  
     * @param e 
     * @return 
     */  
    @Override  
    public boolean onDoubleTap(MotionEvent e) {  
        return super.onDoubleTap(e);  
    }  
  
    /** 
     * 双击的第二下 down和up都会触发，可用e.getAction()区分。 
     * @param e 
     * @return 
     */  
    @Override  
    public boolean onDoubleTapEvent(MotionEvent e) {  
        return super.onDoubleTapEvent(e);  
    }  
  
    /** 
     * down时触发  
     * @param e 
     * @return 
     */  
    @Override  
    public boolean onDown(MotionEvent e) {  
        return super.onDown(e);  
    }  
  
    /** 
     * Touch了滑动一点距离后，up时触发。 
     * @param e1 
     * @param e2 
     * @param velocityX 
     * @param velocityY 
     * @return 
     */  
    @Override  
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
            float velocityY) {  
    	float distanceX = e2.getX() - e1.getX();
    	
    	int moveDistance = 0;
        if (distanceX > 0) {
        	if (nowPositionX>0){
        	nowPositionX = nowPositionX - width;
        	moveDistance = -width;
        	}
        }
        else if (distanceX==0){
        	nowPositionX = nowPositionX;
        }else{
        	if (nowPositionX<2*width){
        	nowPositionX = nowPositionX + width;
        	moveDistance = width;
        	}
        }
        hsvMain.smoothScrollTo(nowPositionX, 0, 500);
        return true;  
    }  
  
    /** 
     * Touch了不移动一直 down时触发  
     * @param e 
     */  
    @Override  
    public void onLongPress(MotionEvent e) {  
    	int index = nowPositionX/720;
        super.onLongPress(e);  
    }  
  
    /** 
     * Touch了滑动时触发。  
     * @param e1 
     * @param e2 
     * @param distanceX 
     * @param distanceY 
     * @return 
     */  
    @Override  
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,  
            float distanceY) {  
//    	hsvMain.scrollBy((int) distanceX, 0);
        return super.onScroll(e1, e2, distanceX, distanceY);  
    }   
  
    /** 
     * Touch了还没有滑动时触发  
     * @param e 
     */  
    @Override  
    public void onShowPress(MotionEvent e) {  
        super.onShowPress(e);  
    }  
  
    @Override  
    public boolean onSingleTapConfirmed(MotionEvent e) {  
        return super.onSingleTapConfirmed(e);  
    }  
  
    @Override  
    public boolean onSingleTapUp(MotionEvent e) {  
        return super.onSingleTapUp(e);  
    } 
}
