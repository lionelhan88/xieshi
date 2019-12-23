package com.lessu.xieshi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.construction.ConstructionListActivity;
import com.lessu.xieshi.dataauditing.DataAuditingActivity;
import com.lessu.xieshi.dataexamine.DataExamineActivity;
import com.lessu.xieshi.login.LoginActivity;
import com.lessu.xieshi.map.ProjectListActivity;
import com.lessu.xieshi.todaystatistics.AdminTodayStatisticsActivity;
import com.lessu.xieshi.todaystatistics.TodayStatisticsActivity;
import com.lessu.xieshi.unqualified.UnqualifiedSearchActivity;
import com.lessu.xieshi.uploadpicture.UploadPictureActivity;

import java.util.ArrayList;

public class XieShiSlidingMenuActivity extends NavigationActivity {
	//2018-10-24 实现沉浸式状态栏 此时赋值false
	boolean isFirstStart = false;
	public SlidingMenu menu; 
	private static String userPower;
	ArrayList<Integer> viewIdArray = new ArrayList<Integer>();
	int[] mapper = {4,0,1,2,3,5,7,6};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("......oncreate");
		menu = new SlidingMenu(this);
		// 滑动方向(LEFT,RIGHT,LEFT_RIGHT)
		menu.setMode(SlidingMenu.LEFT);
		// 滑动显示SlidingMenu的范围  
		//menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// 菜单的宽度  
		int width = (int) (281*getResources().getDisplayMetrics().density);
		menu.setBehindWidth(width);
		// 菜单的布局文件
		menu.setMenu(R.layout.frame_menu);
		//setViews();

	}
	
	
	
	private void setViews() {
		// TODO Auto-generated method stub
		LinearLayout button = (LinearLayout) menu.findViewById(R.id.exitButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(XieShiSlidingMenuActivity.this, LoginActivity.class);
				intent.putExtra("exit",true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});
		button = (LinearLayout) menu.findViewById(R.id.settingButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(XieShiSlidingMenuActivity.this, SettingActivity.class);   
				startActivity(intent); 
			}
		});
		viewIdArray.add(R.id.layoutItem0);
		viewIdArray.add(R.id.layoutItem1);
		viewIdArray.add(R.id.layoutItem2);
		viewIdArray.add(R.id.layoutItem3);
		viewIdArray.add(R.id.layoutItem4);
		viewIdArray.add(R.id.layoutItem5);
		viewIdArray.add(R.id.layoutItem6);
		viewIdArray.add(R.id.layoutItem7);
		menu.findViewById(R.id.section0).setVisibility(View.GONE);
		menu.findViewById(R.id.section1).setVisibility(View.GONE);
		menu.findViewById(R.id.section2).setVisibility(View.GONE);
		System.out.println("userPower....."+userPower);
		userPower= Shref.getString(this, Common.USERPOWER,"");
		if (userPower != null && !userPower.isEmpty()){
			for (int i = 0;i<userPower.length();i++){
				char c = userPower.charAt(i);
				int key = mapper[i];
				if (key<0){
					continue;
				}
				if (c=='0'||i==7){
					int viewId = viewIdArray.get(mapper[i]);
					View rowView = menu.findViewById(viewId);
					if (rowView!= null){
					rowView.setVisibility(View.GONE);
					}
				}
				else{
					String mapperSection0 = "01";
					String mapperSection1 = "234";
					String mapperSection2 = "567";
					
					if (mapperSection0.contains(String.valueOf(key))){
						menu.findViewById(R.id.section0).setVisibility(View.VISIBLE);
					}else if (mapperSection1.contains(String.valueOf(key))){
						menu.findViewById(R.id.section1).setVisibility(View.VISIBLE);
					}else if (mapperSection2.contains(String.valueOf(key))){
						menu.findViewById(R.id.section2).setVisibility(View.VISIBLE);
					}
					
				}
			}
		}
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isFirstStart){
			// 把SlidingMenu附加在Activity上  
			// SlidingMenu.SLIDING_WINDOW:菜单拉开后高度是全屏的  
			// SlidingMenu.SLIDING_CONTENT:菜单拉开后高度是不包含Title/ActionBar的内容区域  
			menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
			  
			Button button = (Button) menu.findViewById(R.id.dataExamineJumpButton);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent (XieShiSlidingMenuActivity.this, DataExamineActivity.class);
					startActivity(intent);
				}
			});//报告批准
			button = (Button) menu.findViewById(R.id.dataAuditingJumpButton);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent (XieShiSlidingMenuActivity.this, DataAuditingActivity.class);
					startActivity(intent);
				}
			});//记录审核
			button = (Button) menu.findViewById(R.id.todayStatisticsJumpButton);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Class tempClass = null;
					if (userPower.charAt(0)=='0'){
						tempClass = TodayStatisticsActivity.class;
						Intent intent = new Intent (XieShiSlidingMenuActivity.this, tempClass);
						startActivity(intent);
					}
					else{
						tempClass = AdminTodayStatisticsActivity.class;
						Intent intent = new Intent (XieShiSlidingMenuActivity.this, tempClass);
						intent.putExtra("diyici",true);
						startActivity(intent);
					}

				}
			});//统计信息
			button = (Button) menu.findViewById(R.id.unqualifiedJumpButton);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent (XieShiSlidingMenuActivity.this, UnqualifiedSearchActivity.class);
					startActivity(intent);

					}
			});//不合格信息查询
			button = (Button) menu.findViewById(R.id.constructionJumpButton);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent (XieShiSlidingMenuActivity.this, ConstructionListActivity.class);
					startActivity(intent);
				}
			});//搞定样品查询
			button = (Button) menu.findViewById(R.id.projectJumpButton);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent (XieShiSlidingMenuActivity.this, ProjectListActivity.class);
					startActivity(intent);
				}
			});//工程查询
			button = (Button) menu.findViewById(R.id.uploadJumpButton);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent (XieShiSlidingMenuActivity.this, UploadPictureActivity.class);
					startActivity(intent);
				}
			});//现场图片上传
			isFirstStart = false;
			menu.showMenu();
			menu.toggle();
		}
	}

	public static void setUserPower(String userPower) {
		XieShiSlidingMenuActivity.userPower = userPower;
	}

}
