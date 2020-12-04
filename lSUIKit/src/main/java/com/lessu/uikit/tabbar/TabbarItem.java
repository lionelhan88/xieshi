package com.lessu.uikit.tabbar;

import androidx.fragment.app.Fragment;

public class TabbarItem extends Object {
	public int imageUnselectedResourceId;
	public int imageSelectedResourceId;
	public String title;
	public Fragment fragment;

	public TabbarItem(String title, int imageUnselectedResourceId, int imageSelectedResourceId,Fragment fragment){
		super();
		this.title = title;
		this.imageUnselectedResourceId = imageUnselectedResourceId;
		this.imageSelectedResourceId = imageSelectedResourceId;
		this.fragment = fragment;
	}
	public static TabbarItem newTabbarItem(String title, int imageUnselectedResourceId, int imageSelectedResourceId,Fragment fragment){
		return new TabbarItem(title, imageUnselectedResourceId, imageSelectedResourceId,fragment);
	}
}
