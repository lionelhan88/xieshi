package com.lessu.xieshi.module.dataexamine;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonValidate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.web.TemplatedWebViewActivity;
import com.scetia.Pro.common.Util.Constants;

import android.os.Bundle;
import android.webkit.WebView;

public class ExamineDetailActivity extends TemplatedWebViewActivity {
	private WebView webView;
	private Map<String,String> activtyMapper;
/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auditing_detail_activity);
		if(webView == null){
			webView = (WebView) findViewById(R.id.auditing_detail);
		}
		this.setTitle("样品信息");
		navigationBar.setBackgroundColor(0xFF3598DC);
	}
*/

	@Override
	protected int getLayoutId() {
		return R.layout.auditing_detail_activity;
	}

	@Override
	protected void initView() {
		this.setTitle("样品信息");
		if(webView == null){
			webView = (WebView) findViewById(R.id.auditing_detail);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Bundle bundelForData=this.getIntent().getExtras();
		String consignId = bundelForData.getString("ConsignId");
		String token =  Constants.User.GET_TOKEN();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("ConsignId", consignId);
		params.put("Token", token);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceRP.asmx/ReportPreviewList"), params, new EasyAPI.ApiFastSuccessCallBack() {
			
			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
				JsonArray jsonArray = result.getAsJsonObject().get("Data").getAsJsonArray();
				if (jsonArray.size()>0)
				{
				JsonElement json = jsonArray.get(0);
				loadHtmlFile("html/apply_detail.html", "file:///android_asset/html/", json);
				}
				else{
					LSAlert.showAlert(ExamineDetailActivity.this, "该样品暂无信息");
				}
			}
		});
		
	}

	@Override
	public WebView getWebView() {
		if(webView == null){
			webView = (WebView) findViewById(R.id.auditing_detail);
		}
		return webView;
	}

	@Override
	public String onStringReplacementForText(String text, Object data) {
		JsonElement jsonData = (JsonElement) data;
		return GsonValidate.getStringByKeyPath(jsonData, text,"");
	}

	@Override
	public boolean onReceiveEvent(String event, Map<String, String> params) {
		return false;
	}
}
