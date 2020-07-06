package com.lessu.xieshi.dataexamine;

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
import com.lessu.xieshi.XieShiSlidingMenuActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExamineSearchActivity extends XieShiSlidingMenuActivity {

	JsonArray typeJson = new JsonArray();
	JsonArray itemJson = new JsonArray();
	String kindId = "";
	String token = "";
	String Type = "";
	String itemId = "";
	String doneFlag = "";
	String typeTitle = "";
	String projectTitle = "";
	String flagTitle = "";
	private int click;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examine_search_activity);
		this.setTitle("报告批准查询");
		navigationBar.setBackgroundColor(0xFF3598DC);
		ButterKnife.bind(this);
		token = LSUtil.valueStatic("Token");
		Type = "0";
        Bundle bundle = getIntent().getExtras();
        kindId = bundle.getString("KindId");
        itemId = bundle.getString("ItemId");

		System.out.println("sousuo....."+itemId);
		doneFlag = bundle.getString("doneFlag");
        typeTitle = bundle.getString("TypeTitle");
        projectTitle = bundle.getString("ProjectTitle");
        flagTitle = bundle.getString("FlagTitle");
        if (doneFlag.isEmpty()) {
        	doneFlag="0";
        }
        if (!typeTitle.isEmpty()){
			TextView tv = (TextView)(findViewById(R.id.typeTextView));
	        tv.setText(typeTitle);
	        projectDataBind();
		}
        if (!projectTitle.isEmpty()){
			TextView tv = (TextView)(findViewById(R.id.projectTextView));
	        tv.setText(projectTitle);
		}
        if (flagTitle.isEmpty()){
			TextView tv = (TextView)(findViewById(R.id.auditedTextView));
	        tv.setText("否");
	        flagTitle = "否";
		}
		else{
			TextView tv = (TextView)(findViewById(R.id.auditedTextView));
	        tv.setText(flagTitle);
		}
		getType();
	}

	private void getType() {
		// TODO Auto-generated method stub
		
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Token", token);
		params.put("Type", Type);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/Get_ItemKind"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
			
			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
				JsonArray json = result.getAsJsonObject().get("Data").getAsJsonArray();
				System.out.println("json...."+json);
				typeJson = json;
			}
			
			@Override
			public String onFailed(ApiError error) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
	
	@OnClick(R.id.typeButton)
	protected void typeButtonDidClick(){
		ArrayList<String> itemArrayList = new ArrayList<String>();
		int typeJsonSize = typeJson.size();
		if (typeJsonSize<=0){
			LSAlert.showAlert(ExamineSearchActivity.this, "目前没有类别！");
			return;
		}
		for (int i=0;i<typeJson.size();i++){
			itemArrayList.add(typeJson.get(i).getAsJsonObject().get("KindName").getAsString());
		}
		final String[] itemString =  (String[])itemArrayList.toArray(new String[typeJson.size()]);
		int index = -1;
		if (typeTitle!=null && !typeTitle.isEmpty()){
			index = itemArrayList.indexOf(typeTitle);
		}
		if (index<0){
			index = 0;
		}
		new AlertDialog.Builder(this)  
		.setTitle("批准类别")  
		.setIcon(android.R.drawable.ic_dialog_info)                  
		.setSingleChoiceItems(itemString, index,   
		  new DialogInterface.OnClickListener() {  
		                              
		     public void onClick(DialogInterface dialog, int which) {  
		        TextView tv = (TextView)(findViewById(R.id.typeTextView));
		        tv.setText(itemString[which]);
		        typeTitle = itemString[which];
		        kindId = typeJson.get(which).getAsJsonObject().get("KindId").getAsString();
		        TextView tv1 = (TextView)(findViewById(R.id.projectTextView));
		        tv1.setText("");
		        itemId = "";
		        dialog.dismiss();  
		        
		        //databind for project
		        projectDataBind();
		     }  
		  }  
		)  
		.setNegativeButton("取消", null)  
		.show(); 
	}
	
	protected void projectDataBind() {
		// TODO Auto-generated method stub
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Token", token);
		params.put("Type", Type);
		params.put("KindId", kindId);
		EasyAPI.apiConnectionAsync(ExamineSearchActivity.this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/Get_ItemItem"), params, new EasyAPI.ApiFastSuccessFailedCallBack() {
			
			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
				JsonArray json = result.getAsJsonObject().get("Data").getAsJsonArray();
				System.out.println("itemjson...."+itemJson);
				itemJson = json;
			}
			
			@Override
			public String onFailed(ApiError error) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	@OnClick(R.id.projectButton)
	protected void projectButtonDidClick(){
		if (!kindId.isEmpty()){
			
			ArrayList<String> itemArrayList = new ArrayList<String>();
			int itemJsonSize = itemJson.size();
			if (itemJsonSize<=0){
				LSAlert.showAlert(ExamineSearchActivity.this, "该类别没有项目！");
				return;
			}
			for (int i=0;i<itemJsonSize;i++){
				itemArrayList.add(itemJson.get(i).getAsJsonObject().get("ItemName").getAsString());
			}
			final String[] itemString =  (String[])itemArrayList.toArray(new String[itemJson.size()]);
			int index = -1;
			if (projectTitle!=null && !projectTitle.isEmpty()){
				index = itemArrayList.indexOf(projectTitle);
			}
			if (index<0){
				index = 0;
			}
			new AlertDialog.Builder(this)  
			.setTitle("批准项目")  
			.setIcon(android.R.drawable.ic_dialog_info)                  
			.setSingleChoiceItems(itemString, index,   
			  new DialogInterface.OnClickListener() {  
			                              
			     public void onClick(DialogInterface dialog, int which) {  
			        TextView tv = (TextView)(findViewById(R.id.projectTextView));
			        tv.setText(itemString[which]);
			        projectTitle = itemString[which];
			        itemId = itemJson.get(which).getAsJsonObject().get("ItemId").getAsString();
					 click = which;
			        dialog.dismiss();  
			     }  
			  }  
			)  
			.setNegativeButton("取消", null)  
			.show(); 
			}
			else{
				LSAlert.showAlert(ExamineSearchActivity.this, "请先选择审核类别！");
			}
	}
	
	@OnClick(R.id.auditedButton)
	protected void auditedButtonDidClick(){
		final String[] itemString = {"是","否"};
		int index = -1;
		if (flagTitle!=null && !flagTitle.isEmpty()){
			ArrayList<String> temp = new ArrayList<String>();
			temp.add("是");
			temp.add("否");
			index = temp.indexOf(flagTitle);
		}
		if (index<0){
			index = 0;
		}
		new AlertDialog.Builder(this)  
		.setTitle("查询已批准报告")  
		.setIcon(android.R.drawable.ic_dialog_info)                  
		.setSingleChoiceItems(itemString, index,   
		  new DialogInterface.OnClickListener() {  
		                              
		     public void onClick(DialogInterface dialog, int which) { 
		    	 if (which==0){
		    		 doneFlag = "1";
		    	 }
		    	 else{
		    		 doneFlag = "0";
		    	 }
		        TextView tv = (TextView)(findViewById(R.id.auditedTextView));
		        tv.setText(itemString[which]);
		        flagTitle = itemString[which];
		        dialog.dismiss();  
		     }  
		  }  
		)  
		.setNegativeButton("取消", null)  
		.show(); 
	}
	
	@OnClick(R.id.searchButton)
	protected void searchButtonDidClick(){
		if (itemId.isEmpty()){
			LSAlert.showAlert(ExamineSearchActivity.this, "请选择项目！");
			return;
		}

		//itemId = itemJson.get(click).getAsJsonObject().get("ItemId").getAsString();

		Bundle bundle = new Bundle();
		bundle.putString("TypeTitle", typeTitle);
		bundle.putString("ProjectTitle", projectTitle);
		bundle.putString("FlagTitle", flagTitle);

		System.out.println("sousuo...diannjiqvedinmg.."+itemId);

		bundle.putString("ItemId", itemId);
		bundle.putString("doneFlag", doneFlag);
		bundle.putString("KindId", kindId);
		setResult(RESULT_OK, this.getIntent().putExtras(bundle));
		finish();
	}

}
