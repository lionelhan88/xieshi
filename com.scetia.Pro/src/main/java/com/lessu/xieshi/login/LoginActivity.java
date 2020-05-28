package com.lessu.xieshi.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.GsonValidate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.foundation.LSUtil;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.AppApplication;
import com.lessu.xieshi.BaseActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.construction.ConstructionListActivity;
import com.lessu.xieshi.dataauditing.DataAuditingActivity;
import com.lessu.xieshi.dataexamine.ExamineDetailActivity;
import com.lessu.xieshi.mis.activitys.MisguideActivity;
import com.lessu.xieshi.todaystatistics.TodayStatisticsActivity;
import com.lessu.xieshi.unqualified.UnqualifiedSearchActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;


/**
 * 当前页面在android9.0后要开启硬件加速，否则输入密码时不会实时显示
 */
public class LoginActivity extends BaseActivity {
	private TextView tv_login_version;
	private String userName;
	private String shortuserpower;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		ButterKnife.inject(this);
		((EditText) findViewById(R.id.userNameEditText)).setSelected(false);
		((EditText) findViewById(R.id.userNameEditText)).clearFocus();
		tv_login_version = (TextView) findViewById(R.id.tv_login_version);
		String userpower = Shref.getString(LoginActivity.this, Common.USERPOWER, "");
		//String userpower = "01101000000000";
		Intent intent=getIntent();
		boolean exit = intent.getBooleanExtra("exit", false);
		boolean jiebang = intent.getBooleanExtra("jiebang", false);
		if (userpower != null && userpower != ""&& !exit && !jiebang) {
			System.out.println("走这里直接进入。。。。");
			Toboundary(userpower);
		} else {
			HashMap<String, Object> updateparams = new HashMap<String, Object>();
			updateparams.put("PlatformType", "1");//1为安卓
			updateparams.put("SystemType", "2");//2为内部版
			System.out.println("updateparams..."+updateparams);
			EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/GetAppVersion"), updateparams, new EasyAPI.ApiFastSuccessCallBack() {
				@Override
				public void onSuccessJson(JsonElement result) {
					String versionName = null;
					try {
						versionName = getPackageManager().getPackageInfo("com.scetia.Pro", 0).versionName;
						System.out.println("versionName.."+versionName);
					} catch (PackageManager.NameNotFoundException e) {
						e.printStackTrace();
					}
					JsonObject json = result.getAsJsonObject().get("Data").getAsJsonArray().get(0).getAsJsonObject();
					String serviceVersion = json.get("Version").getAsString();
					tv_login_version.setText(versionName);
					String[] localVersionArray = versionName.split("\\.");
					String[] serviceVersionArray = serviceVersion.split("\\.");
					int localCount = localVersionArray.length;
					int serviceCount = serviceVersionArray.length;
					int count = localCount;
					if (localCount > serviceCount) {
						count = serviceCount;
					}
					Boolean updateFlag = false;
					try {
						for (int i = 0; i < count; i++) {
							if (Integer.parseInt(localVersionArray[i]) < Integer.parseInt(serviceVersionArray[i])) {
								updateFlag = true;
								AppApplication.isupdate=true;
							}
						}
					} catch (Exception e) {
						updateFlag = false;
						AppApplication.isupdate=false;
					}
					if (updateFlag) {
						final String urlString = json.get("Update_Url").getAsString();
						String description = "更新内容:\r\n" + json.get("Description").getAsString() + "是否立即前往更新";
						LSAlert.showDialog(LoginActivity.this, "检查到新版本", description, "确定", "取消", new LSAlert.DialogCallback() {

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


		//debug
		//数据审核，报告批准，今日统计
//		((EditText)findViewById(R.id.userNameEditText)).setText("T2916");
//		((EditText)findViewById(R.id.passWordEditText)).setText("1");
		//今日统计，不合格信息查询
//		((EditText)findViewById(R.id.userNameEditText)).setText("GLY");
//		((EditText)findViewById(R.id.passWordEditText)).setText("1319");
//		//样品查询，图片
//		((EditText)findViewById(R.id.userNameEditText)).setText("Q13503");
//		((EditText)findViewById(R.id.passWordEditText)).setText("424777");
		//数据审核，报告批准，今日统计
//		((EditText)findViewById(R.id.userNameEditText)).setText("t9990001");
//		((EditText)findViewById(R.id.passWordEditText)).setText("1");
		//样品查询
//		((EditText)findViewById(R.id.userNameEditText)).setText("J20623");
//		((EditText)findViewById(R.id.passWordEditText)).setText("279162");

	}
		@OnFocusChange(R.id.userNameEditText)
		public void userNameEditTextFocus (View view,boolean hasFocus){
			if (hasFocus) {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... arg0) {
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
					//protected void onPostExecute(Void result) {
					//int px =DensityUtil.dp2px( LoginActivity.this, 100);
					//((ScrollView)(findViewById(R.id.scrollView))).scrollTo(0, px);
					//};

				}.execute();
			}
		}
		@OnFocusChange(R.id.passWordEditText)
		public void passWordEditTextTextFocus (View view,boolean hasFocus){
			if (hasFocus) {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... arg0) {
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
					//protected void onPostExecute(Void result) {
					//int px =DensityUtil.dp2px( LoginActivity.this, 100);
					//((ScrollView)(findViewById(R.id.scrollView))).scrollTo(0, px);
					//};

				}.execute();
			}
		}
		@OnClick(R.id.loginButton)
		public void loginButtonDidPress () {
			//登陆接口访问
			userName = ((EditText) findViewById(R.id.userNameEditText)).getText().toString();
			final String PassWord = ((EditText) findViewById(R.id.passWordEditText)).getText().toString();
			login(userName,PassWord);
			/*String deviceId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			if (deviceId == null || deviceId.isEmpty()) {
				WifiManager wifi = (WifiManager) getSystemService(
						Context.WIFI_SERVICE);
				WifiInfo info = wifi.getConnectionInfo();
				deviceId = info.getMacAddress();
			}
			final String DeviceId = deviceId;
			if (userName == null || userName.isEmpty()) {
				LSAlert.showAlert(LoginActivity.this, "请输入账号");
				return;
			}
			if (PassWord == null || PassWord.isEmpty()) {
				LSAlert.showAlert(LoginActivity.this, "请输入密码");
				return;
			}
			HashMap<String, Object> params = new HashMap<>();
			params.put("UserName", userName);
			params.put("PassWord", PassWord);
			params.put("DeviceId", DeviceId);

			EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/UserLogin"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
				@Override
				public void onSuccessJson(JsonElement result) {

					System.out.println(result);
					System.out.println("what happen?");
					JsonObject json = result.getAsJsonObject().get("Data").getAsJsonObject();
					boolean isFirstLogin = json.get("IsFirstLogin").getAsBoolean();
					String userPower = json.get("UserPower").getAsString();
					//这里是。
					if(userName.equals("boleyuan")){
						userPower="000000000111011";
					}
					System.out.println("userPower....."+userPower);

					String token = GsonValidate.getStringByKeyPath(json, "Token", "");

					String PhoneNumber = GsonValidate.getStringByKeyPath(json, "PhoneNumber", "");
					LSUtil.setValueStatic("PhoneNumber", PhoneNumber);

					//XieShiSlidingMenuActivity.setUserPower(userPower);
					LSUtil.setValueStatic("UserName", userName);
					System.out.println("这里总走了吧111111");
					if (isFirstLogin) {
						Bundle bundle = new Bundle();
						bundle.putString("token", token);
						bundle.putString("UserName", userName);
						bundle.putString("PassWord", PassWord);
						bundle.putString("DeviceId", DeviceId);
						bundle.putString("UserPower", userPower);
						Intent intent = new Intent(LoginActivity.this, ValidateActivity.class);
						intent.putExtras(bundle);
						//startActivity(intent);
						startActivityForResult(intent,0x01);
					} else {
						LSUtil.setValueStatic("Token", token);
						Shref.setString(LoginActivity.this, Common.USERNAME, userName);
						Shref.setString(LoginActivity.this, Common.PASSWORD, PassWord);
						Shref.setString(LoginActivity.this, Common.DEVICEID, DeviceId);
						System.out.println("这里总走了吧2222222");
						Shref.setString(LoginActivity.this, Common.USERPOWER, userPower);
						System.out.println("这里总走了吧asdasd");
						Toboundary(userPower);
					}
				}

				@Override
				public String onFailed(ApiError error) {

					LSAlert.showAlert(LoginActivity.this, "用户名或者密码错误");

					return null;
				}
			});*/

		}
	private void Toboundary(String userPower) {
		System.out.println("Tobundary.................");
//		int index = -1;
//		if (userPower.contains("1")){
//			index = userPower.indexOf("1");
//		}
		//新增的权限“比对审批”多一位 2018-10-19
		if(userPower.length()==15){
			shortuserpower = userPower.substring(9,15);
		}else {
			shortuserpower = userPower.substring(9, 14);
		}
		System.out.println("shortuserpower......."+ shortuserpower);
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
//		if (index>4||index<0){
//			LSAlert.showAlert(LoginActivity.this, "您没有权限，请联系管理员！");
//			return;
//		}
//		else{
			Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
			System.out.println(userName + "更前");
			System.out.println(Shref.getString(LoginActivity.this, Common.USERNAME, null));
			if (Shref.getString(LoginActivity.this, Common.USERNAME, null) != null) {
				System.out.println(userName + "前");
				userName = Shref.getString(LoginActivity.this, Common.USERNAME, null);
			}
			intent.putExtra("userpower", userPower);
			intent.putExtra("username", userName);
			System.out.println("这里不可能没走。。。。。");
			startActivity(intent);
			finish();


			//Intent intent = new Intent (LoginActivity.this, classArray.get(index));

//						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//startActivity(intent);
			//finish();
			//}
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
		System.out.println("........onstop");
		((EditText)findViewById(R.id.userNameEditText)).getText().clear();
		((EditText)findViewById(R.id.passWordEditText)).getText().clear();
	}
	//@OnClick(R.id.changePasswordButton)
	//public void changePasswordButtonDidPress(){
	//String UserName = ((EditText)findViewById(R.id.userNameEditText)).getText().toString();
	//if (UserName.isEmpty()){
	//LSAlert.showAlert(LoginActivity.this, "请输入用户名");
	//return;
	//}
	//LSUtil.setValueStatic("UserName", UserName);
	//Intent intent = new Intent(LoginActivity.this, PasswordActivity.class);
	//startActivity(intent);
	//}

	private void login(final String name, final String password){
		String deviceId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		if (deviceId == null || deviceId.isEmpty()) {
			WifiManager wifi = (WifiManager) getSystemService(
					Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			deviceId = info.getMacAddress();
		}
		final String DeviceId = deviceId;
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
				System.out.println("what happen?");
				JsonObject json = result.getAsJsonObject().get("Data").getAsJsonObject();
				boolean isFirstLogin = json.get("IsFirstLogin").getAsBoolean();
				String userPower = json.get("UserPower").getAsString();
				//这里是。
				if(name.equals("boleyuan")){
					userPower="000000000111011";
				}
				System.out.println("userPower....."+userPower);

				String token = GsonValidate.getStringByKeyPath(json, "Token", "");

				String PhoneNumber = GsonValidate.getStringByKeyPath(json, "PhoneNumber", "");
				LSUtil.setValueStatic("PhoneNumber", PhoneNumber);

				//XieShiSlidingMenuActivity.setUserPower(userPower);
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
					//startActivity(intent);
					startActivityForResult(intent,0x01);
				} else {
					LSUtil.setValueStatic("Token", token);
					Shref.setString(LoginActivity.this, Common.USERNAME, name);
					Shref.setString(LoginActivity.this, Common.PASSWORD, password);
					Shref.setString(LoginActivity.this, Common.DEVICEID, DeviceId);
					System.out.println("这里总走了吧2222222");
					Shref.setString(LoginActivity.this, Common.USERPOWER, userPower);
					System.out.println("这里总走了吧asdasd");
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
