package com.lessu.xieshi.set;

import android.os.Bundle;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;

public class PasswordCompleteActivity extends NavigationActivity {

/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_complete_activity);
		
		navigationBar.setBackgroundColor(0xFF3598DC);
		
		setTitle("修改成功");
		
		BarButtonItem	completeButtonitem = new BarButtonItem(this ,"完成");
		completeButtonitem.setOnClickMethod(this,"completeButtonDidClick");	
        navigationBar.setRightBarItem(completeButtonitem);
        
        BarButtonItem	nullButtonitem = new BarButtonItem(this,"");
        navigationBar.setLeftBarItem(nullButtonitem);
	}*/

	@Override
	protected int getLayoutId() {
		return R.layout.password_complete_activity;
	}

	@Override
	protected void initView() {
		setTitle("修改成功");
		BarButtonItem	completeButtonitem = new BarButtonItem(this ,"完成");
		completeButtonitem.setOnClickMethod(this,"completeButtonDidClick");
		navigationBar.setRightBarItem(completeButtonitem);

		BarButtonItem	nullButtonitem = new BarButtonItem(this,"");
		navigationBar.setLeftBarItem(nullButtonitem);
	}

	public void completeButtonDidClick(){
		finish();
    }
}
