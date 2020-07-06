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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.GsonValidate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.BaseActivity;
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
import butterknife.OnFocusChange;


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
		ImmersionBar.with(this).titleBar(tv_login_version).navigationBarColor(android.R.color.white).init();
		String userpower = Shref.getString(LoginActivity.this, Common.USERPOWER, "");
		//String userpower = "01101000000000";
		Intent intent=getIntent();
		boolean exit = intent.getBooleanExtra("exit", false);
		boolean jiebang = intent.getBooleanExtra("jiebang", false);
		//如果之前已经登录，打开程序直接进入主界面
		if (userpower != null && !userpower.equals("") && !exit && !jiebang) {
			Toboundary(userpower);
		} else {
			HashMap<String, Object> updateparams = new HashMap<String, Object>();
			updateparams.put("PlatformType", "1");//1为安卓
			updateparams.put("SystemType", "2");//2为内部版
			EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/GetAppVersion"), updateparams, new EasyAPI.ApiFastSuccessCallBack() {
				@Override
				public void onSuccessJson(JsonElement result) {
					String versionName = null;
					try {
						versionName = getPackageManager().getPackageInfo("com.scetia.Pro", 0).versionName;
						System.out.println("versionName.." + versionName);
					} catch (PackageManager.NameNotFoundException e) {
						e.printStackTrace();
					}
					tv_login_version.setText(versionName);
					JsonObject json = result.getAsJsonObject().get("Data").getAsJsonArray().get(0).getAsJsonObject();
					String serviceVersion = json.get("Version").getAsString();
					//是否强制更新标识
					final int isMustBeUpdate = json.get("Update_Flag").getAsInt();
					String[] localVersionArray = versionName.split("\\.");
					String[] serviceVersionArray = serviceVersion.split("\\.");
					int localCount = localVersionArray.length;
					int serviceCount = serviceVersionArray.length;
					int count = localCount;
					if (localCount > serviceCount) {
						count = serviceCount;
					}
					boolean updateFlag = false;
					try {
						for (int i = 0; i < count; i++) {
							if (Integer.parseInt(localVersionArray[i]) < Integer.parseInt(serviceVersionArray[i])) {
								updateFlag = true;
								AppApplication.isupdate = true;
							}
						}
					} catch (Exception e) {
						updateFlag = false;
						AppApplication.isupdate = false;
					}
					if (updateFlag) {
						final String urlString = json.get("Update_Url").getAsString();
						String description="";
						if(isMustBeUpdate==1){
							//强制更新
							description = "更新内容:\r\n" + json.get("Description").getAsString()
									+"\r\n此更新为强制更新，必须更新后尚可继续使用，\n如暂时不更新点取消退出程序！\r\n"+ "是否立即前往更新？";
						}else{
							description = "更新内容:\r\n" + json.get("Description").getAsString() + "是否立即前往更新？";
						}
						AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
								.setTitle("检查到新版本")
								.setMessage(description)
								.setCancelable(false)
								.setPositiveButton("确定",null)
								.setNegativeButton("取消", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialogInterface, int i) {
										if(isMustBeUpdate==1) {
											AppApplication.exit();
										}
									}
								});
						final AlertDialog dialog = builder.create();
						dialog.setOnShowListener(new DialogInterface.OnShowListener() {
							@Override
							public void onShow(DialogInterface dialogInterface) {
								Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
								button.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										downLoadFile(urlString);
									}
								});
							}
						});
						dialog.show();
					}
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
		//检查权限
	private void Toboundary(String userPower) {
		LogUtil.showLogD("原始权限数据......."+ userPower);
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
			String unMisPower = userPower.substring(0,15);
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
		String deviceId = null;
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//android9以后获取不到IEMI的编码了，可能会发出异常
		if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED){
			try {
				deviceId = telephonyManager.getDeviceId();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		//如果获取不到IEMI的编号，则获取wifi的MAC地址
		if (deviceId == null || deviceId.isEmpty()) {
			WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			deviceId = info.getMacAddress();
		}
		//final String DeviceId = deviceId;
		//测试设备
		final String DeviceId = "13512354";
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

		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/UserLogin"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				System.out.println(result);
				JsonObject json = result.getAsJsonObject().get("Data").getAsJsonObject();
				boolean isFirstLogin = json.get("IsFirstLogin").getAsBoolean();
				String userPower = json.get("UserPower").getAsString();
				System.out.println("userPower....."+userPower);
				String token = GsonValidate.getStringByKeyPath(json, "Token", "");
				String PhoneNumber = GsonValidate.getStringByKeyPath(json, "PhoneNumber", "");
				String userId = GsonValidate.getStringByKeyPath(json, "UserId", "");
				String MemberInfoStr = json.get("MemberInfoStr").getAsString();
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

			@Override
			public String onFailed(ApiError error) {
				LSAlert.showAlert(LoginActivity.this, "用户名或者密码错误");
				return null;
			}
		});
	}
	protected void downLoadFile(String httpUrl) {
		final Uri uri = Uri.parse(httpUrl);
		final Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);
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
