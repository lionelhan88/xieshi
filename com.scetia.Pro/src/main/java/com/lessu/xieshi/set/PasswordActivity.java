package com.lessu.xieshi.set;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.scetia.Pro.common.Util.Constants;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.scetia.Pro.common.Util.SPUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class PasswordActivity extends XieShiSlidingMenuActivity {

	@BindView(R.id.oldPasswordEditText)
	EditText etPassWordOld;
	@BindView(R.id.newPasswordEditText)
	EditText etPassWordNew;
	@BindView(R.id.comfirmPasswordEditText)
	EditText etPassWordConfirm;
	@Override
	protected int getLayoutId() {
		return R.layout.password_activity;
	}

	@Override
	protected void initView() {
		setTitle("密码修改");
	}

	@OnClick(R.id.commitButton)
	public void commitButtonDidClick(){
		HashMap<String, Object> params = new HashMap<>();
		String userName = SPUtil.getSPLSUtil(Constants.User.KEY_USER_NAME,"");
		String passWordOld = etPassWordOld.getText().toString();
		String passWordNew = etPassWordNew.getText().toString();
		String passWordConfirm = etPassWordConfirm.getText().toString();
		if(TextUtils.isEmpty(passWordOld)){
			ToastUtil.showShort("请输入原密码");
			return;
		}
		if(TextUtils.isEmpty(passWordNew)){
			ToastUtil.showShort("请输入新密码");
			return;
		}
		if(TextUtils.isEmpty(passWordConfirm)){
			ToastUtil.showShort("请再次输入新密码");
			return;
		}

		if (!passWordNew.equals(passWordConfirm)){
			LSAlert.showAlert(PasswordActivity.this, "您输入两次密码不相同！");
			return;
		}
		params.put("UserName", userName);
		params.put("PassWordOld",passWordOld);
		params.put("PassWordNew", passWordNew);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/User_ChangePassWord"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
			
			@Override
			public void onSuccessJson(JsonElement result) {
				boolean success = result.getAsJsonObject().get("Success").getAsBoolean();
				if(success) {
					SPUtil.setSPConfig(Constants.User.KEY_PASSWORD, passWordNew);
					Intent intent = new Intent(PasswordActivity.this, PasswordCompleteActivity.class);
					startActivity(intent);
					finish();
				}else {
					//修改密码失败
					LSAlert.showAlert(PasswordActivity.this,"修改密码失败！");
				}
			}
			
			@Override
			public String onFailed(ApiError error) {
				LSAlert.showAlert(PasswordActivity.this, error.errorMeesage);
				return null;
			}
		});
	}
}
