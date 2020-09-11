package com.lessu.xieshi.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.GsonValidate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.Utils.PermissionUtils;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.construction.ConstructionListActivity;
import com.lessu.xieshi.dataauditing.DataAuditingActivity;
import com.lessu.xieshi.dataexamine.ExamineDetailActivity;
import com.lessu.xieshi.mis.activitys.MisguideActivity;
import com.lessu.xieshi.todaystatistics.TodayStatisticsActivity;
import com.lessu.xieshi.unqualified.UnqualifiedSearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 当前页面在android9.0后要开启硬件加速，否则输入密码时不会实时显示
 */
public class LoginActivity extends NavigationActivity {
	private TextView tv_login_version;
	private String userName;
	private String shortuserpower;
	private final static String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE,
	Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		ButterKnife.bind(this);
		navigationBar.setVisibility(View.GONE);
		ButterKnife.bind(this);
		((EditText) findViewById(R.id.userNameEditText)).setSelected(false);
		((EditText) findViewById(R.id.userNameEditText)).clearFocus();
		tv_login_version = (TextView) findViewById(R.id.tv_login_version);
		ImmersionBar.with(this).titleBar(tv_login_version)
				.navigationBarColor(com.lessu.uikit.R.color.light_gray)
				.navigationBarDarkIcon(true)
				.init();
		//拿到本地存储的权限
		String userpower = Shref.getString(LoginActivity.this, Common.USERPOWER, "");
		//String userpower = "01101000000000";
		Intent intent=getIntent();
		boolean exit = intent.getBooleanExtra("exit", false);
		boolean jiebang = intent.getBooleanExtra("jiebang", false);
		//如果之前已经登录，打开程序直接进入主界面
		if (userpower != null && !userpower.equals("") && !exit && !jiebang) {
			Toboundary(userpower);
		} else {
			try {
				String versionName = getPackageManager().getPackageInfo("com.scetia.Pro", 0).versionName;
				tv_login_version.setText(versionName);
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
			getUpdate(false,new UpdateAppCallback() {
				@Override
				public void updateCancel() {
					AppApplication.exit();
				}
			});
		}
	}

	@Override
	protected void initImmersionBar() {
	}

	@OnClick(R.id.loginButton)
	public void loginButtonDidPress() {
		//登陆接口访问
		userName = ((EditText) findViewById(R.id.userNameEditText)).getText().toString();
		final String PassWord = ((EditText) findViewById(R.id.passWordEditText)).getText().toString();
		login(userName, PassWord);
	}

	/**
	 * 检查权限，根据不同的权限跳转不同的页面
	 * @param userPower
	 */
	private void Toboundary(String userPower) {
		LogUtil.showLogD("原始权限数据......."+ userPower);
		//什么都没有的账号登陆，提示角色权限
		if(userPower.equals("")){
			LSAlert.showAlert(this,"无角色权限！");
			return;
		}
		//新增的权限“比对审批”多一位 2018-10-19
		if(userPower.length()==15){
			shortuserpower = userPower.substring(9,15);
		}else if(userPower.length()<15) {
			shortuserpower = userPower.substring(9, 14);
		}else{
			//新版本新加了权限
			shortuserpower = userPower.substring(16);
		}
		LogUtil.showLogD("shortuserpower......."+ shortuserpower);
		if(shortuserpower.equals("00000")||shortuserpower.equals("000000")) {
			ArrayList<Class> classArray = new ArrayList<Class>();
			classArray.add(UnqualifiedSearchActivity.class);
			classArray.add(DataAuditingActivity.class);
			classArray.add(ExamineDetailActivity.class);
			classArray.add(ConstructionListActivity.class);
			classArray.add(TodayStatisticsActivity.class);
			// version1
			classArray.add(null);
			classArray.add(null);
			classArray.add(null);
			Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
			if (Shref.getString(LoginActivity.this, Common.USERNAME, null) != null) {
				userName = Shref.getString(LoginActivity.this, Common.USERNAME, null);
			}
			String unMisPower="";
			if(userPower.length()<15){
				 unMisPower = userPower.substring(userPower.length());
			}else {
				unMisPower = userPower.substring(0, 15);
			}
			intent.putExtra("userpower", unMisPower);
			intent.putExtra("username", userName);
			startActivity(intent);
			finish();
		}else{
			Intent intent = new Intent(LoginActivity.this, MisguideActivity.class);
			if (Shref.getString(LoginActivity.this, Common.USERNAME, null) != null) {
				userName = Shref.getString(LoginActivity.this, Common.USERNAME, null);
			}
			intent.putExtra("shortuserpower", shortuserpower);
			intent.putExtra("username", userName);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		((EditText)findViewById(R.id.userNameEditText)).getText().clear();
		((EditText)findViewById(R.id.passWordEditText)).getText().clear();
	}

	@Override
	protected void onStart() {
		super.onStart();
		//android版本大于6.0申请动态权限
		PermissionUtils.requestPermission(this, new PermissionUtils.permissionResult() {
			@Override
			public void hasPermission(List<String> granted, boolean isAll) {

			}
		},permissions);
	}

	/**
	 * 执行登陆请求
	 * @param name
	 * @param password
	 */
	@SuppressLint("HardwareIds")
	private void login(final String name, final String password){
		//获取存在本地的deviceID
		String deviceId = Shref.getString(LoginActivity.this, Common.DEVICEID, "");
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//android9以后获取不到IEMI的编码了，可能会发出异常
		if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED){
			try {
				deviceId = telephonyManager.getDeviceId();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		//如果前一种方法娶不到deviceID,则用android_id的方式获取,如果本地已经存在deviceID，则使用本地的deviceID
		if(deviceId==null||deviceId.equals("")) {
			deviceId = Settings.System.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
		}
		final String DeviceId = deviceId;
		LogUtil.showLogE("deviceID="+DeviceId);
		//测试设备
		//final String DeviceId = "02:00:00:00:00:00";
		if (name == null || name.isEmpty()) {
			LSAlert.showAlert(LoginActivity.this, "请输入账号");
			return;
		}
		if (password == null || password.isEmpty()) {
			LSAlert.showAlert(LoginActivity.this, "请输入密码");
			return;
		}
		HashMap<String, Object> params = new HashMap<>();
		params.put("UserName", name);
		params.put("PassWord", password);
		params.put("DeviceId", DeviceId);

		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/UserLogin"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				System.out.println(result);
				//是否登录成功
				boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
				if(!isSuccess){
					//登录失败，并提示用户
					String message =GsonValidate.getStringByKeyPath(result.getAsJsonObject(),"Message","");
					LSAlert.showAlert(LoginActivity.this,message);
					return;
				}
				JsonObject json = result.getAsJsonObject().get("Data").getAsJsonObject();
				boolean isFirstLogin = json.get("IsFirstLogin").getAsBoolean();
				String userPower = json.get("UserPower").getAsString();
				System.out.println("userPower....."+userPower);
				String token = GsonValidate.getStringByKeyPath(json, "Token", "");
				String PhoneNumber = GsonValidate.getStringByKeyPath(json, "PhoneNumber", "");
				String userId = GsonValidate.getStringByKeyPath(json, "UserId", "");
				String MemberInfoStr=GsonValidate.getStringByKeyPath(json, "MemberInfoStr", "");;
				LSUtil.setValueStatic("PhoneNumber", PhoneNumber);
				LSUtil.setValueStatic("UserName", name);
				System.out.println("这里总走了吧111111");
				if (isFirstLogin) {
					Bundle bundle = new Bundle();
					bundle.putString("token", token);
					bundle.putString("UserName", name);
					bundle.putString("PassWord", password);
					bundle.putString("DeviceId", DeviceId);
					bundle.putString("UserPower", userPower);
					Intent intent = new Intent(LoginActivity.this, ValidateActivity.class);
					intent.putExtras(bundle);
					startActivityForResult(intent,0x01);
				} else {
					LSUtil.setValueStatic("Token", token);
					Shref.setString(LoginActivity.this, Common.USERNAME, name);
					Shref.setString(LoginActivity.this, Common.PASSWORD, password);
					Shref.setString(LoginActivity.this, Common.DEVICEID, DeviceId);
					Shref.setString(LoginActivity.this, Common.USERPOWER, userPower);
					Shref.setString(LoginActivity.this,Common.USERID,userId);
					Shref.setString(LoginActivity.this,Common.MEMBERINFOSTR,MemberInfoStr);
					Toboundary(userPower);
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==0x01){
			if(RESULT_OK==resultCode) {
				String userName1 = data.getStringExtra("userName");
				String password1 = data.getStringExtra("password");
				login(userName1, password1);
			}
		}
	}
}
