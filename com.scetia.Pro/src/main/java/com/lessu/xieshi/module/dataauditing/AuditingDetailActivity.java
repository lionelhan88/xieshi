package com.lessu.xieshi.module.dataauditing;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.gson.GsonValidate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.web.TemplatedWebViewActivity;
import com.lessu.xieshi.module.unqualified.UqTestingReportConclusionActivity;
import com.scetia.Pro.common.Util.Constants;

import java.util.HashMap;
import java.util.Map;

public class AuditingDetailActivity extends TemplatedWebViewActivity {
	private WebView webView;
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
		String sampleId = bundelForData.getString("SampleId");
		String token =  Constants.User.GET_TOKEN();
		HashMap<String, Object> params = new HashMap<>();
		params.put("ConsignId", sampleId);
		params.put("Token", token);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceRP.asmx/RecordPreviewList"), params, new EasyAPI.ApiFastSuccessCallBack() {
			
			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
				JsonArray jsonArray = result.getAsJsonObject().get("Data").getAsJsonArray();
				System.out.println(jsonArray);
				if (jsonArray.size()>0)
				{
					JsonElement json = jsonArray.get(0);
					try{
					JsonArray paramInfoJson = json.getAsJsonObject().get("ParamInfo").getAsJsonArray();
					String htmlString = "";
					for (int i=0;i<paramInfoJson.size();i++){
						String parameterName = paramInfoJson.get(i).getAsJsonObject().get("ParameterName").getAsString();
						String uCResultValue = paramInfoJson.get(i).getAsJsonObject().get("UCResultValue").getAsString();
						String uCTestValue = paramInfoJson.get(i).getAsJsonObject().get("UCTestValue").getAsString();
						String uCStandardValue = paramInfoJson.get(i).getAsJsonObject().get("UCStandardValue").getAsString();
						htmlString = htmlString + String.format("<li class=\"detail-switch-li\"><p class=\"detail-switch-title\">%s</p><p class=\"detail-switch-content\">%s(检测:%s，标准:%s)</p></li>",
								parameterName, uCResultValue,uCTestValue,uCStandardValue);
					}
					json.getAsJsonObject().addProperty("ParamInfoHtml", htmlString);
					}
					catch(Exception e){
						
					}
					loadHtmlFile("html/verify_detail.html", "file:///android_asset/html/", json);
				}
				else{
					LSAlert.showAlert(AuditingDetailActivity.this, "该样品暂无信息");
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
		if (event != null){
			if (event.equalsIgnoreCase("sampleList")){
				Intent intent = new Intent (AuditingDetailActivity.this, UqTestingReportConclusionActivity.class);
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