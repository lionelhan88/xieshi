package com.lessu.xieshi.module.construction;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonValidate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.xieshi.module.mis.activitys.Content;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.web.TemplatedWebViewActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class QrcodeDetailActivity extends TemplatedWebViewActivity {
	private WebView sampledetailWebView;
	private Map<String,String> activtyMapper;
	String Id = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_detail_activity);
		if(sampledetailWebView == null){
			sampledetailWebView = (WebView) findViewById(R.id.sample_detail);
		}
		this.setTitle("扫描详情");
	}

	@Override
	protected void onStart() {
		super.onStart();
		HashMap<String, Object> params = new HashMap<String, Object>();
		Bundle bundelForData=this.getIntent().getExtras(); 
		Id = bundelForData.getString("CoreCode");
		
		String Token = Content.getToken();
		
		params.put("Token", Token);
		params.put("CoreCode", Id);
		
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceSM.asmx/SampleInfoByCode"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
				JsonObject json = result.getAsJsonObject().get("Data").getAsJsonObject();
				json.addProperty("QR_Code", Id);
				JsonElement jsonElement = result.getAsJsonObject().get("Data");
				loadHtmlFile("html/ws_sample_detail_qr.html", "file:///android_asset/html/", jsonElement);
			}
		});
		
	}

	@Override
	public WebView getWebView() {
		if(sampledetailWebView == null){
			sampledetailWebView = (WebView) findViewById(R.id.sample_detail);
		}
		return sampledetailWebView;
	}

	@Override
	public String onStringReplacementForText(String text, Object data) {
		JsonElement jsonData = (JsonElement) data;
		return GsonValidate.getStringByKeyPath(jsonData, text," ");
	}

	@Override
	public boolean onReceiveEvent(String event, Map<String, String> params) {
		
		return false;
	}
}