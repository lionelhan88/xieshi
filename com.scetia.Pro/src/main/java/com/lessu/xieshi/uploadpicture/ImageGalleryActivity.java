package com.lessu.xieshi.uploadpicture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.uikit.views.LSAlert.DialogCallback;
import com.lessu.xieshi.HorizontalScrollViewEx;
import com.lessu.xieshi.R;
import com.lessu.xieshi.XieShiGestureListener;
import com.lessu.xieshi.map.ProjectListActivity;

import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageGalleryActivity extends NavigationActivity{
	public static int picCount = 3;
	private HorizontalScrollViewEx hsvMain;
	int width ;
	int nowPositionX = 0;
	int index=1;
	// 监听屏幕动作事件
    GestureDetector gestureDetector ;
	ArrayList<ImageView> imageList = new ArrayList<ImageView>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_gallery_activity);
		this.setTitle("图片");
		
		navigationBar.setBackgroundColor(0xFF3598DC);
		hsvMain = (HorizontalScrollViewEx) findViewById(R.id.ScrollView);
		LinearLayout linear = (LinearLayout) findViewById(R.id.LinearLayout);
		
		WindowManager wm = this.getWindowManager();
		
		width = wm.getDefaultDisplay().getWidth();
		
//		gestureDetector = new GestureDetector(new XieShiGestureListener(hsvMain, width));
		gestureDetector = new GestureDetector(new OnGestureListener(){

			@Override
			public boolean onDown(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float arg2, float arg3) {
				// TODO Auto-generated method stub
				float distanceX = e2.getX() - e1.getX();
		    	
		    	int moveDistance = 0;
		        if (distanceX > 0) {
		        	if (nowPositionX>0){
		        	nowPositionX = nowPositionX - width;
		        	moveDistance = -width;
		        	index = index-1;
		        	}
		        }
		        else if (distanceX==0){
		        	nowPositionX = nowPositionX;
		        }else{
		        	if (nowPositionX<2*width){
		        	nowPositionX = nowPositionX + width;
		        	moveDistance = width;
		        	index = index+1;
		        	}
		        }
		        ((TextView)findViewById(R.id.indexTextView)).setText(index+"/3");
		        hsvMain.smoothScrollTo(nowPositionX, 0, 500);
		        return true;  
			}

			@Override
			public void onLongPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				LSAlert.showDialog(ImageGalleryActivity.this, "保存图片", "是否保存图片？", "确定", "取消", new DialogCallback() {
					
					@Override
					public void onConfirm() {
						int index = nowPositionX/width;
						ImageView imageView = imageList.get(index);
						imageView.setDrawingCacheEnabled(true);
						Bitmap nowBitmap = imageView.getDrawingCache();
						if (nowBitmap!=null){
						
						String result = MediaStore.Images.Media.insertImage(getContentResolver(), nowBitmap, null, null);
						if(result!=null&&!result.isEmpty()){
							LSAlert.showAlert(ImageGalleryActivity.this, "保存成功！");
						}
						else{
							LSAlert.showAlert(ImageGalleryActivity.this, "保存失败！");
						}
						}
						else{
							LSAlert.showAlert(ImageGalleryActivity.this, "图片不存在或未加载！");
						}
						imageView.setDrawingCacheEnabled(false);
					}
					
					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}
				});
				
			}

			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1,
					float arg2, float arg3) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		//		linear.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				// TODO Auto-generated method stub
//			   return gestureDetector.onTouchEvent(arg1);
//			}
//		});
		for (int i = 1;i<4;i++){
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ImageView.ScaleType.CENTER);
		imageView.setImageResource(R.drawable.img_downloading_image);
		android.view.ViewGroup.LayoutParams para;
        para = new LayoutParams(width,android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(para);
		imageList.add(imageView);
		linear.addView(imageView);
		}
		
		for (int i=1;i<4;i++){
			final int index = i;
			HashMap<String, Object> params = new HashMap<String, Object>();
	    	String token = LSUtil.valueStatic("Token");
	    	String taskId = getIntent().getStringExtra("TaskID");
	    	String imgIndex = String.valueOf(i);
	    	params.put("Token", token);
			params.put("TaskID", taskId);
			params.put("ImgIndex", i);
			
			EasyAPI.apiConnectionAsync(ImageGalleryActivity.this, false, false, ApiMethodDescription.get("/ServiceStake.asmx/GetTaskImage"), params , new EasyAPI.ApiFastSuccessFailedCallBack() {
				@Override
				public void onSuccessJson(JsonElement result) {
					JsonObject json = result.getAsJsonObject().get("Data").getAsJsonObject();
					
					String imageString = json.get("ImgByte").getAsString();
					ImageView imageView = imageList.get(index-1);
					int i = index;
					int k = json.get("ImgIndex").getAsInt();
					if (imageString == null || imageString.equals("")){
						imageView.setImageResource(R.drawable.img_no_image);
					}
					else{
						try{
					imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
					byte[] imgByte = Base64.decode(imageString, Base64.DEFAULT);
					BitmapFactory.Options options = new BitmapFactory.Options();   
		            options.inJustDecodeBounds = true;
		            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, options);
		            //获取到这个图片的原始宽度和高度  
		            int picWidth  = options.outWidth;  
		            int picHeight = options.outHeight; 
	                //获取屏的宽度和高度  
		            WindowManager windowManager = getWindowManager();  
		            Display display = windowManager.getDefaultDisplay();  
		            int screenWidth = display.getWidth();  
		            int screenHeight = display.getHeight(); 
		            
		            options.inJustDecodeBounds = false; 
                    //isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2  
				    options.inSampleSize = 1;  
				                    //根据屏的大小和图片大小计算出缩放比例  
				    if(picWidth > picHeight){  
					    if(picWidth > screenWidth)  
					    	options.inSampleSize = picWidth/screenWidth;  
					    }  
				    else{  
					    if(picHeight > screenHeight)  
					    	options.inSampleSize = picHeight/screenHeight;  
				    }  
      
		            imageBitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, options);
		            picWidth = options.outWidth;
		            picHeight = options.outHeight;
		            float scale = 1;
		            //缩放
		            if(picWidth > picHeight){  
		            	scale = (float)screenWidth/(float)picWidth;  
		            }  
		            else{  
		            	scale = (float)screenHeight/(float)picHeight;  
		            }  
		            Matrix matrix = new Matrix(); 
		            matrix.postScale(scale,scale); //长和宽放大缩小的比例
		            imageBitmap = Bitmap.createBitmap(imageBitmap,0,0,imageBitmap.getWidth(),imageBitmap.getHeight(),matrix,true);
		            imageView.setImageBitmap(imageBitmap);  
					}
					catch(Exception e){
						int j = 1;
					}
					}
					}
				
				@Override
				public String onFailed(ApiError error) {
					// TODO Auto-generated method stub
					ImageView imageView = imageList.get(index-1);
					imageView.setImageResource(R.drawable.img_no_image);
					return null;
				}
			});
		}
		ButterKnife.bind(this);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
		
		super.onStart();
	}
		
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
        boolean result = gestureDetector.onTouchEvent(ev);
//        result = ;
        return result;
    }

	  
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//    	// TODO Auto-generated method stub
//    	boolean result = gestureDetector.onTouchEvent(event);
////        if (ev.getAction()==MotionEvent.ACTION_DOWN||ev.getAction()==MotionEvent.ACTION_UP){
////        	result = super.dispatchTouchEvent(ev);
////        }
//        return result;
//    }
	
	@OnClick(R.id.leftButton)
	public void leftButtonDidPress(){
    	if (nowPositionX>0){
    		nowPositionX = nowPositionX - width;
    		index = index-1;
    		hsvMain.smoothScrollTo(nowPositionX, 0, 500);
    		((TextView)findViewById(R.id.indexTextView)).setText(index+"/3");
    	}
        
	}
	
	@OnClick(R.id.rightButton)
	public void rightButtonDidPress(){
    	if (nowPositionX<2*width){
	    	nowPositionX = nowPositionX + width;
	    	index=index+1;
	    	hsvMain.smoothScrollTo(nowPositionX, 0, 500);
	    	((TextView)findViewById(R.id.indexTextView)).setText(index+"/3");
    	}
	}

	@OnClick(R.id.downloadButton)
	public void downloadButtonDidPress(){
		LSAlert.showDialog(ImageGalleryActivity.this, "保存图片", "是否保存图片？", "确定", "取消", new DialogCallback() {
			
			@Override
			public void onConfirm() {
				int index = nowPositionX/width;
				ImageView imageView = imageList.get(index);
				imageView.setDrawingCacheEnabled(true);
				Bitmap nowBitmap = imageView.getDrawingCache();
				if (nowBitmap!=null){
				
				String result = MediaStore.Images.Media.insertImage(getContentResolver(), nowBitmap, null, null);
				if(result!=null&&!result.isEmpty()){
					LSAlert.showAlert(ImageGalleryActivity.this, "保存成功！");
				}
				else{
					LSAlert.showAlert(ImageGalleryActivity.this, "保存失败！");
				}
				}
				else{
					LSAlert.showAlert(ImageGalleryActivity.this, "图片不存在或未加载！");
				}
				imageView.setDrawingCacheEnabled(false);
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
