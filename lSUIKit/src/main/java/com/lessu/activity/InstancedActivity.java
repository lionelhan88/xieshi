package com.lessu.activity;

import android.os.Bundle;

import com.lessu.navigation.NavigationActivity;


public class InstancedActivity extends NavigationActivity {
	public interface InstancedActivityCallback{
		void onStartComplete(InstancedActivity activity);
	}
	private static InstancedActivityCallback activityCallback;
//	public static InstancedActivity instance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
	}
	@Override
	protected void onDestroy() {

		super.onDestroy();
		
	}
	
	@Override
	protected void onStop() {

		super.onStop();
		
	}
	@Override
	protected void onStart() {

		super.onStart();
		if(InstancedActivity.getActivityCallback() != null){
			getActivityCallback().onStartComplete(this);
			setActivityCallback(null);
		}
	}
	public static InstancedActivityCallback getActivityCallback() {
		return activityCallback;
	}
	public static void setActivityCallback(InstancedActivityCallback activityCallback) {
		InstancedActivity.activityCallback = activityCallback;
	}
	
}
