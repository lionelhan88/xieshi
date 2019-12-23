package com.lessu.xieshi.uploadpicture;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.EasyGson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.PicSize;
import com.lessu.xieshi.XieShiSlidingMenuActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class UploadPictureActivity extends XieShiSlidingMenuActivity implements OnItemClickListener{
	ArrayList<CharSequence> imageList = new ArrayList<CharSequence>();
	JsonArray list = new JsonArray();
	int nowUploadIndex = -1;
	int position = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_picture_activity);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(this);

		this.setTitle("图片上传");
		navigationBar.setBackgroundColor(0xFF3598DC);

		BarButtonItem	menuButtonitem = new BarButtonItem(this ,R.drawable.icon_navigation_menu);
		menuButtonitem.setOnClickMethod(this,"menuButtonDidClick");
		//navigationBar.setLeftBarItem(menuButtonitem);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		HashMap<String, Object> params = new HashMap<String, Object>();
		String token = LSUtil.valueStatic("Token");
		params.put("Token", token);
		params.put("TaskName", "");
		params.put("ProjectName", "");
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceStake.asmx/GetTaskList"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				String jsonString = result.getAsJsonObject().get("Data").toString();
				JsonElement jsonElement = EasyGson.jsonFromString(jsonString);
				if (!jsonElement.isJsonNull()&&jsonElement.isJsonArray()){
					list = jsonElement.getAsJsonArray();
				}
				else{
					LSAlert.showAlert(UploadPictureActivity.this, "当前无数据!");
				}
				adapter.notifyDataSetChanged();
			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
	}

	protected BaseAdapter adapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View getView(int position, View view, ViewGroup parentViewGroup) {
			if (view == null){
				view = View.inflate(UploadPictureActivity.this, R.layout.upload_picture_item, null);
			}
			EasyUI.setTextViewText(view.findViewById(R.id.projectNameTextView), list.get(position).getAsJsonObject(), "ProjectName", "暂无");
			EasyUI.setTextViewText(view.findViewById(R.id.taskNameTextView), list.get(position).getAsJsonObject(), "TaskName", "暂无");
			EasyUI.setTextViewText(view.findViewById(R.id.startTimeTextView), list.get(position).getAsJsonObject(), "PlanStartTime", "暂无");
			EasyUI.setTextViewText(view.findViewById(R.id.endTimeTextView), list.get(position).getAsJsonObject(), "PlanEndTime", "暂无");
			EasyUI.setTextViewText(view.findViewById(R.id.markCodeTextView), list.get(position).getAsJsonObject(), "ContractRegNo", "暂无");
			EasyUI.setTextViewText(view.findViewById(R.id.entrustCodeTextView), list.get(position).getAsJsonObject(), "DelegationID", "暂无");
			EasyUI.setTextViewText(view.findViewById(R.id.addressTextView), list.get(position).getAsJsonObject(), "ProjectAddress", "暂无");
			EasyUI.setTextViewText(view.findViewById(R.id.testTypeTextView), list.get(position).getAsJsonObject(), "Method", "暂无");

			view.findViewById(R.id.uploadButtom).setTag(position);
			EasyUI.setButtonClickMethod(view.findViewById(R.id.uploadButtom) , UploadPictureActivity.this, "uploadButtomDidClick");
			view.findViewById(R.id.downloadButton).setTag(position);
			EasyUI.setButtonClickMethod(view.findViewById(R.id.downloadButton) , UploadPictureActivity.this, "downloadButtomDidClick");
			return view;
		}


		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}


	};


	public void uploadButtomDidClick(View button){
		position = (Integer) button.getTag();
		new AlertDialog.Builder(UploadPictureActivity.this)
				.setTitle("图片选择")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(new String[] {"图片1","图片2","图片3"}, 0,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								nowUploadIndex = which;
								picChoose();
								dialog.dismiss();
							}
						})
				.setNegativeButton("取消", null)
				.show();
	}

	protected void picChoose() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(UploadPictureActivity.this)
				.setTitle("图片选择")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(new String[] {"拍照","相册"}, 0,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								if (which==0){
									Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //"android.media.action.IMAGE_CAPTURE";
									startActivityForResult(intent, 1);
								}
								else{
									Intent intent = new Intent();
			        /* �?��Pictures画面Type设定为image */
									intent.setType("image/*");
			        /* 使用Intent.ACTION_GET_CONTENT这个Action */
									intent.setAction(Intent.ACTION_GET_CONTENT);
			        /* 取得相片后返回本画面 */
									startActivityForResult(intent, 1);
								}
								dialog.dismiss();
							}
						})
				.setNegativeButton("取消", null)
				.show();

	}
	public void downloadButtomDidClick(View button){
		final int position = (Integer) button.getTag();
		Intent intent = new Intent(UploadPictureActivity.this,ImageGalleryActivity.class);
		intent.putExtra("TaskID", list.get(position).getAsJsonObject().get("TaskID").getAsString());
		startActivity(intent);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
//            Uri uri = data.getData(); 
//            if (uri !=null){
			Bundle bitmapBundle = data.getExtras();
			Bitmap bitmap =null;
			if (bitmapBundle!=null){
				bitmap = (Bitmap) bitmapBundle.get("data");
			}

			ContentResolver cr = this.getContentResolver();
			if (bitmap==null){
				Uri uri = data.getData();
				if (uri !=null){
					try {
						bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
						double getpicsize = PicSize.getpicsize(bitmap.getWidth(), bitmap.getHeight());
						if(bitmap.getWidth()<1024&&bitmap.getWidth()<1024){

						}else{
							BitmapFactory.Options opts=new BitmapFactory.Options();
							System.out.println( "be.........."+(int) getpicsize);
							opts.inTempStorage = new byte[100 * 1024];
							opts.inPreferredConfig = Bitmap.Config.RGB_565;
							opts.inPurgeable = true;
							opts.inSampleSize = (int) getpicsize;
							System.out.println("长。。。"+bitmap.getHeight());
							System.out.println("宽。。。"+bitmap.getWidth());
							bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri),null,opts);
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (bitmap!=null){
				try {
					if (nowUploadIndex>=0){
//                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
						ByteArrayOutputStream baos = new ByteArrayOutputStream();

						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
						byte[] imgByte = baos.toByteArray();

						String imgByteString = Base64.encodeToString(imgByte,Base64.DEFAULT);
						HashMap<String, Object> params = new HashMap<String, Object>();
						String token = LSUtil.valueStatic("Token");
						String taskID = list.get(position).getAsJsonObject().get("TaskID").getAsString();
						String imgIndex = String.valueOf(nowUploadIndex);
						imgIndex = String.valueOf(nowUploadIndex+1);
						params.put("Token", token);
						params.put("TaskID", taskID);
						params.put("ImgIndex", imgIndex);
						params.put("ImgByte", imgByteString);
						System.out.print(params);
						EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.post("/ServiceStake.asmx/UploadTaskImage"), params, new EasyAPI.ApiFastSuccessCallBack() {
							@Override
							public void onSuccessJson(JsonElement result) {
								String jsonString = result.getAsJsonObject().get("Data").toString();
								if (jsonString.equals("1")){
									LSAlert.showAlert(UploadPictureActivity.this, "上传成功");
								}
								else{
									LSAlert.showAlert(UploadPictureActivity.this, "上传失败");
								}
							}
						});
					}
				} catch (Exception e) {
					LSAlert.showAlert(this, e.getMessage());
				}
			}
			else{
				LSAlert.showAlert(this,"您的相片选择失败！请使用原生相机");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void menuButtonDidClick(){
		menu.toggle();
	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			exitBy2Click();
//			return false;
//		} else {
//			return super.dispatchKeyEvent(event);
//		}
//	}
	private static Boolean isExit = false;
	private void exitBy2Click() {
		// TODO Auto-generated method stub
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备�?��
			Toast.makeText(this, "再按�?��返回键�?出程�?", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消�?��
				}
			}, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任�?

		} else {
			finish();
			System.exit(0);
		}
	}
}
