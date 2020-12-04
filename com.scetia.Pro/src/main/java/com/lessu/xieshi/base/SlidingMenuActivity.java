package com.lessu.xieshi.base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lessu.navigation.NavigationActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SlidingMenuActivity extends NavigationActivity {
	SlidingMenu menu = new SlidingMenu(this);  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void setMode(int mode){
		menu.setMode(mode);
	}
	
	public void setTouchModeAbove(int area){
		menu.setTouchModeAbove(area);  
	}
	
	public void setBehindWidth(int width){
		menu.setBehindWidth(width);
	}
	
	public void attachToActivity(Activity activity,int slidStyle){
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);  
	}
	
	public void setMenu(int id){
		menu.setMenu(id);  
	}
	
	public void showMenu(boolean animate){
		menu.showMenu(animate);  
	}
	
	public void toggle(){
		menu.toggle(); 
	}
}
