package com.lessu.xieshi.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.foundation.LSUtil;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProjectSearchActivity extends XieShiSlidingMenuActivity {
	
	JsonArray projectJson = new JsonArray();
	String token = "";
	String memberCode = "";
	String hour = "4";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_search_activity);
		this.setTitle("工程查询");
		navigationBar.setBackgroundColor(0xFF3598DC);
		ButterKnife.bind(this);
		getType();
	}

	private void getType() {
		token = LSUtil.valueStatic("Token");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Token", token);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceStake.asmx/GetMemberList"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
			
			@Override
			public void onSuccessJson(JsonElement result) {
				JsonArray json = result.getAsJsonObject().get("Data").getAsJsonArray();
				projectJson = json;
			}
			
			@Override
			public String onFailed(ApiError error) {
				// TODO Auto-generated method stub
				int i = 1;
				return null;
			}
		});
	}

	@OnClick(R.id.projectButton)
	protected void projectButtonDidClick(){
		ArrayList<String> itemArrayList = new ArrayList<String>();
		int typeJsonSize = projectJson.size();
		if (typeJsonSize<=0){
			LSAlert.showAlert(ProjectSearchActivity.this, "目前没有会员");
			return;
		}
		for (int i=0;i<projectJson.size();i++){
			itemArrayList.add(projectJson.get(i).getAsJsonObject().get("MemberName").getAsString());
		}
		final String[] itemString =  (String[])itemArrayList.toArray(new String[projectJson.size()]);
		new AlertDialog.Builder(this)  
		.setTitle("会员选择")  
		.setIcon(android.R.drawable.ic_dialog_info)                  
		.setSingleChoiceItems(itemString, 0,   
		  new DialogInterface.OnClickListener() {  
		                              
		     public void onClick(DialogInterface dialog, int which) {  
		        TextView tv = (TextView)(findViewById(R.id.projectTextView));
		        tv.setText(itemString[which]);
		        memberCode = projectJson.get(which).getAsJsonObject().get("MemberCode").getAsString();
		        dialog.dismiss();  
		        
		     }  
		  }  
		)  
		.setNegativeButton("取消", null)  
		.show(); 
	}
	
	@OnClick(R.id.timeButton)
	protected void timeButtonDidClick(){
		final String[] itemString =  {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
		new AlertDialog.Builder(this)  
		.setTitle("时效选择")  
		.setIcon(android.R.drawable.ic_dialog_info)                  
		.setSingleChoiceItems(itemString, 4,   
		  new DialogInterface.OnClickListener() {  
		                              
		     public void onClick(DialogInterface dialog, int which) {  
		        TextView tv = (TextView)(findViewById(R.id.timeTextView));
		        tv.setText(itemString[which]);
		        hour = itemString[which];
		        dialog.dismiss();  
		     }  
		  }  
		)  
		.setNegativeButton("取消", null)  
		.show(); 
	}
	
	
	@OnClick(R.id.searchButton)
	protected void searchButtonDidClick(){
		/*if (memberCode.isEmpty()){
			LSAlert.showAlert(ProjectSearchActivity.this, "请选择会员！");
			return;
		}*/
		/*if (hour.isEmpty()){
			LSAlert.showAlert(ProjectSearchActivity.this, "请选择时效！");
			return;
		}*/
		Bundle bundle = new Bundle();
		bundle.putString("MemberCode", memberCode);
		bundle.putString("Hour", hour);
		setResult(RESULT_OK, this.getIntent().putExtras(bundle));
		finish();
	}
}
