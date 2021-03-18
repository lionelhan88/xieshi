package com.lessu.xieshi.module.web;

import java.util.Map;

import com.google.gson.EasyGson;
import com.google.gson.GsonValidate;
import com.google.gson.JsonElement;
import com.lessu.xieshi.R;

import android.os.Bundle;
import android.webkit.WebView;

public class RecordPreviewActivity extends TemplatedWebViewActivity {
	private WebView recordpreviewWebView;
	private Map<String,String> activtyMapper;

	@Override
	protected int getLayoutId() {
		return R.layout.report_detail_activity;
	}

	@Override
	protected void initView() {
		if(recordpreviewWebView == null){
			recordpreviewWebView = (WebView) findViewById(R.id.report_detail);
		}
		this.setTitle("审核中记录预览");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Bundle bundelForData=this.getIntent().getExtras(); 
		String jsonString=bundelForData.getString("data"); 
		JsonElement jsonData = EasyGson.jsonFromString(jsonString);
		loadHtmlFile("html/record_preview.html", "file:///android_asset/html/", jsonData);
		
		
	}

	@Override
	public WebView getWebView() {
		if(recordpreviewWebView == null){
			recordpreviewWebView = (WebView) findViewById(R.id.report_detail);
		}
		return recordpreviewWebView;
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
