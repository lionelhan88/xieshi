package com.lessu.xieshi.module.web;

import java.util.Map;

import com.google.gson.EasyGson;
import com.google.gson.GsonValidate;
import com.google.gson.JsonElement;
import com.lessu.xieshi.R;

import android.os.Bundle;
import android.webkit.WebView;

public class ReportPreviewActivity extends TemplatedWebViewActivity {
	private WebView reportpreviewWebView;

	@Override
	protected int getLayoutId() {
		return R.layout.report_detail_activity;
	}

	@Override
	protected void initView() {
		if(reportpreviewWebView == null){
			reportpreviewWebView = (WebView) findViewById(R.id.report_detail);
		}
		this.setTitle("批准中报告预览");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Bundle bundelForData=this.getIntent().getExtras(); 
		String jsonString=bundelForData.getString("data"); 
		JsonElement jsonData = EasyGson.jsonFromString(jsonString);
		loadHtmlFile("html/report_preview.html", "file:///android_asset/html/", jsonData);
		
		
	}

	@Override
	public WebView getWebView() {
		if(reportpreviewWebView == null){
			reportpreviewWebView = (WebView) findViewById(R.id.report_detail);
		}
		return reportpreviewWebView;
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
