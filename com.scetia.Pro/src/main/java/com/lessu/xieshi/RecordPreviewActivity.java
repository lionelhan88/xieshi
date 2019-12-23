package com.lessu.xieshi;

import java.util.Map;

import com.google.gson.EasyGson;
import com.google.gson.GsonValidate;
import com.google.gson.JsonElement;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class RecordPreviewActivity extends TemplatedWebViewActivity{
	private WebView recordpreviewWebView;
	private Map<String,String> activtyMapper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_detail_activity);
		if(recordpreviewWebView == null){
			recordpreviewWebView = (WebView) findViewById(R.id.report_detail);
		}
		this.setTitle("审核中记录预览");
		navigationBar.setBackgroundColor(0xFF3598DC);
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
		
//		if (event != null){
//			if (event.equalsIgnoreCase("sampleList")){
//				Intent intent = new Intent (ReportDetailActivity.this, SampleListActivity.class);
//				Bundle bundle = new Bundle();  
//				Bundle bundelForData=this.getIntent().getExtras(); 
//				String Report_ID = bundelForData.getString("Report_id");
//				String Checksum = bundelForData.getString("Checksum");
//				bundle.putString("Report_id", Report_ID);
//				bundle.putString("Checksum", Checksum);
//				intent.putExtras(bundle);
//				startActivity(intent);  
//				return true;
//			}
//		}
		return false;
	}


}
