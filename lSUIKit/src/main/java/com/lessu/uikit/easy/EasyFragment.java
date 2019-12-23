package com.lessu.uikit.easy;

import java.lang.reflect.Field;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class EasyFragment extends Fragment{
	@Override
	public void onStart() {
		super.onStart();
		
		this.findViewTree(getView(), new ProcessViewInterface() {
			
			@Override
			public void onProccess(View view) {
				if(view.getClass().isAssignableFrom(Button.class)||view.getClass().isAssignableFrom(ImageButton.class)){
					Button button = (Button) view;

					try {
						Field field = button.getClass().getDeclaredField("mOnClickListener");
						field.setAccessible(true);
						Object onClickListener = field.get(button);
						
						if(onClickListener != null){
							return ;
						}
						
					} catch (SecurityException e) {
						return ;
					} catch (NoSuchFieldException e) {
						return ;
					} catch (IllegalArgumentException e) {
						return ;
					} catch (IllegalAccessException e) {
						return ;
					}
					
					button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {

							
						}
					});
				
				}
			}
		});
	}
	
	protected interface ProcessViewInterface{
		public void onProccess(View view);
	}
	public void findViewTree(View parent,ProcessViewInterface callback){
		callback.onProccess(parent);
		if(parent.getClass().isAssignableFrom(ViewGroup.class)){
			this.findViewTree(parent, callback);
		}
	}
}
