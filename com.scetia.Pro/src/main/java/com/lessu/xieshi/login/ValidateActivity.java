package com.lessu.xieshi.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.foundation.LSUtil;
import com.lessu.foundation.ValidateHelper;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ValidateActivity extends NavigationActivity {
	String validateCode = "";
	private int recLen = 60;
	private Button getValidateButton;
	Timer timer = new Timer();
	//倒计时任务类，每次执行新的任务要重新实例化
	MyTimerTsk timerTsk = null;
	private String userName;
	private String passWord;
	private String deviceId;
	private String token;
	private String token1;
	private String shortuserpower;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.validate_activity);
		this.setTitle("绑定");
		navigationBar.setBackgroundColor(0xFF3598DC);
		getValidateButton = (Button) findViewById(R.id.getValidateButton);
		ButterKnife.bind(this);

		if (LSUtil.valueStatic("PhoneNumber") != null){
			String phoneNumber = LSUtil.valueStatic("PhoneNumber");
			((EditText)findViewById(R.id.phoneNumEditText)).setText(phoneNumber);
		}
	}

	@OnTextChanged(R.id.phoneNumEditText)
	public void phoneNumEditTextChanged(){
		LSUtil.setValueStatic("PhoneNumber", ((EditText)findViewById(R.id.phoneNumEditText)).getText().toString());
	}

	@OnClick(R.id.getValidateButton)
	public void getValidateButtonDidPress(){
		Bundle bundle = getIntent().getExtras();
		userName = bundle.getString("UserName");
		passWord = bundle.getString("PassWord");
		deviceId = bundle.getString("DeviceId");
		token = bundle.getString("token");
		String PhoneNumber = ((EditText)findViewById(R.id.phoneNumEditText)).getText().toString();
		if (!ValidateHelper.validatePhone(PhoneNumber)){
			LSAlert.showAlert(ValidateActivity.this, "手机号输入有误！请重新输入");
			return;
		}
		JsonObject paramJson = new JsonObject();
		paramJson.addProperty("UserName", userName);
		paramJson.addProperty("PassWord", passWord);
		paramJson.addProperty("DeviceId", deviceId);
		paramJson.addProperty("PhoneNumber", PhoneNumber);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("UserName", userName);
		params.put("PassWord", passWord);
		params.put("DeviceId", deviceId);
		params.put("PhoneNumber", PhoneNumber);
		System.out.println(params);
		String s = new Gson().toJson(params);
		System.out.println(s);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/User_BindStart"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				try{
					System.out.println(result);
					JsonObject resultJson = result.getAsJsonObject();
					if(!resultJson.get("Success").getAsBoolean()){
						//如果账号之前帮的手机号与当前输入的手机号不符合则提示
						LSAlert.showAlert(ValidateActivity.this, "当前账户及设备已绑定或正进行绑定中，请不要重复绑定");
						return;
					}
					token1 = resultJson.get("Data").getAsJsonObject().get("Token").getAsString();
					System.out.println(token1);
					LSUtil.setValueStatic("Token", token1);
					System.out.println("绑定的start...."+LSUtil.valueStatic("Token"));

					String PhoneNumber = resultJson.get("Data").getAsJsonObject().get("PhoneNumber").getAsString();
					LSUtil.setValueStatic("PhoneNumber", PhoneNumber);

				/*	String isSuccess = resultJson.get("Success").getAsString();
					if (isSuccess.equalsIgnoreCase("false")){
						String alertMessage = "";
						alertMessage = alertMessage + "Code:" + resultJson.get("Code").getAsString()+" 错误信息:" + resultJson.get("Message").getAsString();
						LSAlert.showAlert(ValidateActivity.this, alertMessage);
					}else{*/
						//开始进入验证码倒计时时间
						timerTsk = new MyTimerTsk();
						getValidateButton.setBackgroundResource(R.drawable.yanzhengma);
						getValidateButton.setText("重新获取验证码("+recLen+"s)");
						getValidateButton.setEnabled(false);
						timer.schedule(timerTsk, 1000, 1000);
					//}
				}
				catch (Exception e){
					System.out.println("catch..............");
					LSAlert.showAlert(ValidateActivity.this, e.getMessage());
				}

			}
		});
	}

	@OnClick(R.id.validateButton)
	public void validateButtonDidPress(){
		String PhoneNumber = ((EditText)findViewById(R.id.phoneNumEditText)).getText().toString();
		EditText validateCodeEditText = (EditText)findViewById(R.id.validateCodeEditText);
		if (!ValidateHelper.validatePhone(PhoneNumber)){
			LSAlert.showAlert(ValidateActivity.this, "手机号输入有误！请重新输入");
			return;
		}
		if (validateCodeEditText.getText().toString().isEmpty()){
			LSAlert.showAlert(ValidateActivity.this, "请输入验证码");
			return;
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Token", LSUtil.valueStatic("Token"));
		params.put("CheckCode", validateCodeEditText.getText().toString());

		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/User_BindEnd"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				int index = -1;
				boolean success = result.getAsJsonObject().get("Success").getAsBoolean();
				//2018-10-19日修改
				if(success){
					//如果验证手机成功，则到登录页面重新登录
					Intent intent = new Intent();
					intent.putExtra("userName",userName);
					intent.putExtra("password",passWord);
					setResult(RESULT_OK,intent);
					finish();
				}else{
					LSAlert.showAlert(ValidateActivity.this, "验证失败，请重新验证");
				}
			/*	String userPower = getIntent().getExtras().getString("UserPower");
				if (userPower != null && userPower.contains("1")){
					index = userPower.indexOf("1");
				}
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
//				if (index>4||index<0){
//					LSAlert.showAlert(ValidateActivity.this, "您没有权限，请联系管理员！");
//					return;
//				}
//				else{
				LSUtil.setValueStatic("Token", token1);
				Shref.setString(ValidateActivity.this, Common.USERNAME, userName);
				Shref.setString(ValidateActivity.this, Common.PASSWORD, passWord);
				Shref.setString(ValidateActivity.this, Common.DEVICEID, deviceId);
				Shref.setString(ValidateActivity.this, Common.USERPOWER, userPower);
				//新加权限“比对审批”多一位权限
				shortuserpower = userPower.substring(9, 15);
				System.out.println("shortuserpower......."+ shortuserpower);
				if(shortuserpower.equals("000000")) {
					Intent intent = new Intent(ValidateActivity.this, FirstActivity.class);
					intent.putExtra("userpower", userPower);
					intent.putExtra("username", userName);
					startActivity(intent);
					finish();
				}else {
					Intent intent = new Intent(ValidateActivity.this, MisguideActivity.class);
					if (Shref.getString(ValidateActivity.this, Common.USERNAME, null) != null) {
						userName = Shref.getString(ValidateActivity.this, Common.USERNAME, null);
					}
					intent.putExtra("shortuserpower", shortuserpower);
						intent.putExtra("username", userName);
					startActivity(intent);
					finish();
				}*/
				//Intent intent = new Intent (ValidateActivity.this, classArray.get(index));
				//startActivity(intent);
				//finish();
				//}
			}
		});

	}

	/*TimerTask task = new TimerTask() {
		@Override
		public void run() {

			runOnUiThread(new Runnable() {      // UI thread
				@Override
				public void run() {
					recLen--;
					getValidateButton.setText("重新获取验证码("+recLen+"s)");
					if(recLen <= 0){
						timer.cancel();
						timer.purge();
						timer = null;
						recLen = 60;
						getValidateButton.setEnabled(true);
						getValidateButton.setText("重新获取验证码");
						getValidateButton.setBackgroundResource(R.drawable.huoquyanzhengma1);
					}
				}
			});
		}
	};*/
	class MyTimerTsk extends TimerTask{

		@Override
		public void run() {
			runOnUiThread(new Runnable() {      // UI thread
				@Override
				public void run() {
					recLen--;
					getValidateButton.setText("重新获取验证码("+recLen+"s)");
					if(recLen <= 0){
						timerTsk.cancel();//任务执行玩后要取消，新的任务执行要重新new
						timerTsk = null;
						recLen = 60;
						getValidateButton.setEnabled(true);
						getValidateButton.setText("重新获取验证码");
						getValidateButton.setBackgroundResource(R.drawable.huoquyanzhengma1);
					}
				}
			});
		}
	}
}
