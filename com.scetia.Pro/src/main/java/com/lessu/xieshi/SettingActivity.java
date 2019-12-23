package com.lessu.xieshi;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.uikit.views.LSAlert.DialogCallback;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.login.LoginActivity;

import java.io.File;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends XieShiSlidingMenuActivity {
	protected String updateVersion;
	private BarButtonItem handleButtonItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		setTitle("设置");
		navigationBar.setBackgroundColor(0xFF3598DC);
		/*BarButtonItem	menuButtonitem = new BarButtonItem(this ,R.drawable.icon_navigation_menu);
        menuButtonitem.setOnClickMethod(this,"menuButtonDidClick");	
        //navigationBar.setLeftBarItem(menuButtonitem);*/
        
        String serviceString = LSUtil.valueStatic("service");
        TextView serviceTextView = (TextView) findViewById(R.id.serviceTextView);
        HashMap<String, String> serviceTitleMap = new HashMap<String, String>();
        serviceTitleMap.put("telecom", "电信服务器");
        serviceTitleMap.put("unicom", "联通服务器");
        if (serviceTitleMap.containsKey(serviceString))
        {
        	serviceTextView.setText(serviceTitleMap.get(serviceString));
        }
        else{
        	serviceTextView.setText("当前服务器错误");
        }
        
        ButterKnife.inject(this);
	}
	
	public void menuButtonDidClick(){
		menu.toggle();
    }

	@OnClick(R.id.jiechuButton)
	public void jiechuButtonDidClick(){
		//解除绑定的操作
		String description="手机号:"+ LSUtil.valueStatic("PhoneNumber")+"\n"+"用户名:"+Shref.getString(SettingActivity.this, Common.USERNAME, "");
		LSAlert.showDialog(SettingActivity.this, "解除账号绑定",description , "确定", "取消", new DialogCallback() {

			@Override
			public void onConfirm() {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("UserName", Shref.getString(SettingActivity.this, Common.USERNAME, ""));
				params.put("PhoneNumber",LSUtil.valueStatic("PhoneNumber"));
				params.put("PassWord",Shref.getString(SettingActivity.this,Common.PASSWORD,""));
				System.out.println(params);
				EasyAPI.apiConnectionAsync(SettingActivity.this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/User_UnBind "), params, new EasyAPI.ApiFastSuccessCallBack() {
					@Override
					public void onSuccessJson(JsonElement result) {
						// TODO Auto-generated method stub
						Shref.setString(SettingActivity.this,Common.USERPOWER,"");
						//LSUtil.setValueStatic("Token", "");

						Intent intent = new Intent();
						intent.setClass(SettingActivity.this, LoginActivity.class);
						intent.putExtra("jiebang",true);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();

					}
				});
			}
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});
	}
	
	@OnClick(R.id.passwordButton)
	public void passwordButtonDidClick(){
		Intent intent = new Intent(SettingActivity.this, PasswordActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.serviceButton)
	public void serviceButtonDidClick(){
		Intent intent = new Intent(SettingActivity.this, ServiceActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.updateButton)
	public void updateButtonDidClick(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("PlatformType", "1");//1为安卓
		params.put("SystemType", "2");//2为内部版
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/GetAppVersion"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
				String versionName = null;
				try {
					versionName = getPackageManager().getPackageInfo("com.scetia.Pro", 0).versionName;
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JsonObject json = result.getAsJsonObject().get("Data").getAsJsonArray().get(0).getAsJsonObject();
				String serviceVersion = json.get("Version").getAsString();
				String[] localVersionArray = versionName.split("\\.");
				String[] serviceVersionArray = serviceVersion.split("\\.");
				int localCount = localVersionArray.length;
				int serviceCount = serviceVersionArray.length;
				int count = localCount;
				if (localCount>serviceCount){
					count = serviceCount;
				}
				Boolean updateFlag = false;
				try{
				for (int i=0;i<count;i++){
					if (Integer.parseInt(localVersionArray[i])<Integer.parseInt(serviceVersionArray[i])){
						updateFlag = true;
					}
				}
				}
				catch(Exception e){
					updateFlag = false;
				}
				if (!updateFlag){
					LSAlert.showAlert(SettingActivity.this, "当前已是最新版本！");
				}
				else{
					updateVersion = serviceVersion;
					final String urlString = json.get("Update_Url").getAsString();
					String description = "更新内容:\r\n"+json.get("Description").getAsString()+ "是否立即前往更新";
					LSAlert.showDialog(SettingActivity.this, "检查到新版本",description , "确定", "取消", new DialogCallback() {
						
						@Override
						public void onConfirm() {
							// TODO Auto-generated method stub
							downLoadFile(urlString);
//								if (updatefile == null){
//									LSAlert.showAlert(SettingActivity.this, "未成功下载更新软件，请稍后再试");
//									return;
//								}
//								else{
//									installApk(updatefile);
//								}
						}
						
						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
			
		});
			
		
	}
	protected void downLoadFile(String httpUrl) {
        // TODO Auto-generated method stub
		final Uri uri = Uri.parse(httpUrl);          
		final Intent it = new Intent(Intent.ACTION_VIEW, uri);          
		startActivity(it);
//		if (updateVersion.isEmpty()){
//			LSAlert.showAlert(SettingActivity.this, "当前以是最新版本或服务器版本未获取到！");
//			return null;
//		}
//        final String fileName = "updata"+updateVersion+".apk";
//        File tmpFile = new File("/sdcard/xieshi_update");
//        if (!tmpFile.exists()) {
//                tmpFile.mkdir();
//        }
//        final File file = new File("/sdcard/update/" + fileName);
//
//       try {
//                URL url = new URL(httpUrl);
//                try {
//                        HttpURLConnection conn = (HttpURLConnection) url
//                                        .openConnection();
//                        InputStream is = conn.getInputStream();
//                        FileOutputStream fos = new FileOutputStream(file);
//                        byte[] buf = new byte[256];
//                        conn.connect();
//                        double count = 0;
//                        if (conn.getResponseCode() >= 400) {
//                                Toast.makeText(SettingActivity.this, "连接超时", Toast.LENGTH_SHORT)
//                                                .show();
//                        } else {
//                                while (count <= 100) {
//                                        if (is != null) {
//                                                int numRead = is.read(buf);
//                                                if (numRead <= 0) {
//                                                        break;
//                                                } else {
//                                                        fos.write(buf, 0, numRead);
//                                                }
//
//                                       } else {
//                                                break;
//                                        }
//
//                               }
//                        }
//
//                       conn.disconnect();
//                        fos.close();
//                        is.close();
//                } catch (IOException e) {
//                        // TODO Auto-generated catch block
//
//                       e.printStackTrace();
//                }
//        } catch (MalformedURLException e) {
//                // TODO Auto-generated catch block
//
//               e.printStackTrace();
//        }
//
//       return file;
}
	/**
     * 安装APK
     */
    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
