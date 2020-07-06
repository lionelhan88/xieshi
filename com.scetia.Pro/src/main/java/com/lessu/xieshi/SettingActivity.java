package com.lessu.xieshi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.uikit.views.LSAlert.DialogCallback;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.PermissionUtils;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.login.LoginActivity;
import com.lessu.xieshi.scan.ScanActivity;
import com.lessu.xieshi.training.TrainingActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;

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
        
        ButterKnife.bind(this);
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
	@OnClick(R.id.bt_set_login_out)
	public void resetLogin(){
		LSAlert.showDialog(this, "提示", "确定要重新登陆吗？", "确定", "取消",
				new DialogCallback() {
					@Override
					public void onConfirm() {
						loginOut();
					}

					@Override
					public void onCancel() {

					}
				});
	}
	@OnClick(R.id.updateButton)
	public void updateButtonDidClick(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("PlatformType", "1");//1为安卓
		params.put("SystemType", "2");//2为内部版
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/GetAppVersion"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				String versionName = null;
				try {
					versionName = getPackageManager().getPackageInfo("com.scetia.Pro", 0).versionName;
				} catch (NameNotFoundException e) {
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
				boolean updateFlag = false;
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
							downLoadFile(urlString);
						}
						
						@Override
						public void onCancel() {
						}
					});
				}
			}
			
		});
	}
	@OnClick(R.id.scanLogin)
	public void scanLogin(){
		if(Shref.getString(SettingActivity.this, Common.USERID,"").equals("")){
			//如果userid是"",则要提示用户重新登录才能获取到userId
			LSAlert.showDialog(SettingActivity.this, "提示", "使用此功能需要重新登录\n是否现在登录？",
					"确定", "取消", new LSAlert.DialogCallback() {
						@Override
						public void onConfirm() {
							//退出登录
							loginOut();
						}

						@Override
						public void onCancel() {

						}
					});
		}
		PermissionUtils.requestPermission(this, new PermissionUtils.permissionResult() {
			@Override
			public void hasPermission(List<String> granted, boolean isAll) {
				Intent scanIntent = new Intent(SettingActivity.this, ScanActivity.class);
				startActivity(scanIntent);
			}
		},Manifest.permission.CAMERA);
	}
	protected void downLoadFile(String httpUrl) {
		final Uri uri = Uri.parse(httpUrl);
		final Intent it = new Intent(Intent.ACTION_VIEW, uri);          
		startActivity(it);
	}

	/**
	 * 退出登录
	 */
	private void loginOut(){
		Intent intentTUICHU = new Intent();
		intentTUICHU.setClass(SettingActivity.this, LoginActivity.class);
		intentTUICHU.putExtra("exit", true);
		intentTUICHU.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intentTUICHU);
		finish();
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
