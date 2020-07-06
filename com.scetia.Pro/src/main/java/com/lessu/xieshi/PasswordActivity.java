package com.lessu.xieshi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.lessu.foundation.LSUtil;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordActivity extends XieShiSlidingMenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_activity);
		
		navigationBar.setBackgroundColor(0xFF3598DC);
		
		setTitle("密码修改");
		
		//ButterKnife.inject(PasswordActivity.this);
	}
	
	@OnClick(R.id.commitButton)
	public void commitButtonDidClick(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		String userName = LSUtil.valueStatic("UserName");
		String passWordOld = ((EditText)findViewById(R.id.oldPasswordEditText)).getText().toString();
		String passWordNew = ((EditText)findViewById(R.id.newPasswordEditText)).getText().toString();
		String passWordComfirm = ((EditText)findViewById(R.id.comfirmPasswordEditText)).getText().toString();
		if (!passWordNew.equals(passWordComfirm)){
			LSAlert.showAlert(PasswordActivity.this, "您输入两次密码不相同！");
			return;
		}
		params.put("UserName", userName);
		params.put("PassWordOld",passWordOld);
		params.put("PassWordNew", passWordNew);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/User_ChangePassWord"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
			
			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PasswordActivity.this, PasswordCompleteActivity.class);
				startActivity(intent);
				finish();
			}
			
			@Override
			public String onFailed(ApiError error) {
				// TODO Auto-generated method stub
				LSAlert.showAlert(PasswordActivity.this, error.errorMeesage);
				return null;
			}
		});
	}
}
