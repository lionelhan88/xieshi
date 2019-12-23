package com.lessu.xieshi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.EasyGson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.GsonValidate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.xieshi.construction.SampleListActivity;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class ReportDetailActivity extends TemplatedWebViewActivity{
	private WebView reportdetailWebView;
	private Map<String,String> activtyMapper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_detail_activity);
		if(reportdetailWebView == null){
			reportdetailWebView = (WebView) findViewById(R.id.report_detail);
		}
		this.setTitle("检测报告");
		navigationBar.setBackgroundColor(0xFF3598DC);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Bundle bundelForData=this.getIntent().getExtras(); 
		String jsonString=bundelForData.getString("data"); 
		JsonElement jsonData = EasyGson.jsonFromString(jsonString);
		loadHtmlFile("html/report_detail.html", "file:///android_asset/html/", jsonData);
		
		
	}

	@Override
	public WebView getWebView() {
		if(reportdetailWebView == null){
			reportdetailWebView = (WebView) findViewById(R.id.report_detail);
		}
		return reportdetailWebView;
	}

	@Override
	public String onStringReplacementForText(String text, Object data) {
		JsonElement jsonData = (JsonElement) data;
		return GsonValidate.getStringByKeyPath(jsonData, text,"");
	}

	@Override
	public boolean onReceiveEvent(String event, Map<String, String> params) {
		
		if (event != null){
			if (event.equalsIgnoreCase("sampleList")){
				Intent intent = new Intent (ReportDetailActivity.this, SampleListActivity.class);
				Bundle bundle = new Bundle();  
				Bundle bundelForData=this.getIntent().getExtras(); 
				String Report_ID = bundelForData.getString("Report_id");
				String Checksum = bundelForData.getString("Checksum");
				bundle.putString("Report_id", Report_ID);
				bundle.putString("Checksum", Checksum);
				intent.putExtras(bundle);
				startActivity(intent);  
				return true;
			}
		}
		return false;
	}


}
