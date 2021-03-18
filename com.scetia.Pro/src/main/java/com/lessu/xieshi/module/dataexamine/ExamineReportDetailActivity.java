package com.lessu.xieshi.module.dataexamine;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.EasyGson;
import com.google.gson.JsonElement;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.bean.ReportDetail;
import com.scetia.Pro.common.Util.Constants;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

public class ExamineReportDetailActivity extends NavigationActivity {
	private WebView detailWebView;
	private Map<String,String> activtyMapper;
	private String jsonStringSample;

	@Override
	protected int getLayoutId() {
		return R.layout.examine_report_detail_activity;
	}

	@Override
	protected void initView() {
		this.setTitle("报告详情");
	}

	@Override
	protected void onStart() {
		super.onStart();
		HashMap<String, Object> params = new HashMap<String, Object>();
		Bundle bundelForData=this.getIntent().getExtras(); 
		String ConsignId = bundelForData.getString("ConsignId");
		String Token =  Constants.User.GET_TOKEN();
		
		params.put("Token", Token);
		params.put("ConsignId", ConsignId);

		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceRP.asmx/ReportPreviewList"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub

				String jsonString = result.getAsJsonObject().get("Data").toString();
				System.out.println(jsonString);
				JsonElement jsonElement = EasyGson.jsonFromString(jsonString);
				JsonElement json = jsonElement.getAsJsonArray().get(0);
				jsonStringSample = jsonString;
				ReportDetail reportDetail = GsonUtil.JsonToObject(json.toString(), ReportDetail.class);
				TextView tv_baogaobianhao = (TextView) findViewById(R.id.tv_baogaobianhao);
				tv_baogaobianhao.setText(reportDetail.getReport_ID()+"");
				TextView tv_jianceleibie = (TextView) findViewById(R.id.tv_jianceleibie);
				tv_jianceleibie.setText(reportDetail.getExam_Kind()+"");
				TextView tv_weituoriqi = (TextView) findViewById(R.id.tv_weituoriqi);
				tv_weituoriqi.setText(reportDetail.getDeliver_Date()+"");
				TextView tv_jianzhengren = (TextView) findViewById(R.id.tv_jianzhengren);
				tv_jianzhengren.setText(reportDetail.getWitness());
				TextView tv_qvyangren = (TextView) findViewById(R.id.tv_qvyangren);
				tv_qvyangren.setText(reportDetail.getSampling());
				TextView tv_weituodanwei = (TextView) findViewById(R.id.tv_weituodanwei);
				tv_weituodanwei.setText(reportDetail.getEntrustUnitName()+"");
				TextView tv_gongchengmingcen = (TextView) findViewById(R.id.tv_gongchengmingcen);
				tv_gongchengmingcen.setText(reportDetail.getProjectName()+"");
				TextView tv_gongchengdizhi = (TextView) findViewById(R.id.tv_gongchengdizhi);
				tv_gongchengdizhi.setText(reportDetail.getProjectAddress()+"");
				TextView tv_shigongdanwei = (TextView) findViewById(R.id.tv_shigongdanwei);
				tv_shigongdanwei.setText(reportDetail.getBuildUnit()+"");
				TextView tv_baogaoriqi = (TextView) findViewById(R.id.tv_baogaoriqi);
				tv_baogaoriqi.setText(reportDetail.getReportDate());
				TextView tv_fangweijiaoyanma = (TextView) findViewById(R.id.tv_fangweijiaoyanma);
				tv_fangweijiaoyanma.setText(reportDetail.getIdentifyingCode());


				//loadHtmlFile("html/apply_detail.html", "file:///android_asset/html/", json);
			}
		});
		
	}

//	@Override
//	public WebView getWebView() {
//		if(detailWebView == null){
//			detailWebView = (WebView) findViewById(R.id.detailWebView);
//		}
//		return detailWebView;
//	}
//
//	@Override
//	public String onStringReplacementForText(String text, Object data) {
//		JsonElement jsonData = (JsonElement) data;
//		return GsonValidate.getStringByKeyPath(jsonData, text,"");
//	}
//	@Override
//	public boolean onReceiveEvent(String event, Map<String, String> params) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@OnClick(R.id.checkSampleButton)
	public void checkSampleButtonDidClick(){
		Intent intent = new Intent(ExamineReportDetailActivity.this,ExamineSampleListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("jsonStringSample", jsonStringSample);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
