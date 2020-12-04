package com.lessu.xieshi.module.dataexamine;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.gson.EasyGson;
import com.google.gson.JsonElement;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.bean.ExamineSample;

import java.util.Map;

import butterknife.ButterKnife;

public class ExamineSampleDetailActivity extends NavigationActivity {
	private WebView detailWebView;
	private Map<String,String> activtyMapper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examine_sample_detail_activity);
		//if(detailWebView == null){
			//detailWebView = (WebView) findViewById(R.id.detailWebView);
		//}
		this.setTitle("样品详情");
		navigationBar.setBackgroundColor(0xFF3598DC);
		ButterKnife.bind(this);
	}
	@Override
	protected void onStart() {
		super.onStart();
		Bundle bundle = getIntent().getExtras();
		String jsonString = bundle.getString("jsonString");
		System.out.println(jsonString);
		JsonElement jsonElement = EasyGson.jsonFromString(jsonString);
		ExamineSample examineSample = GsonUtil.JsonToObject(jsonString, ExamineSample.class);
		TextView tv1 = (TextView) findViewById(R.id.tv1);
		tv1.setText(examineSample.getSample_Id());
		TextView tv2 = (TextView) findViewById(R.id.tv2);
		tv2.setText(examineSample.getSampleName());
		TextView tv3 = (TextView) findViewById(R.id.tv3);
		tv3.setText(examineSample.getSpecName());
		TextView tv4 = (TextView) findViewById(R.id.tv4);
		tv4.setText(examineSample.getGradeName());
		TextView tv5 = (TextView) findViewById(R.id.tv5);
		tv5.setText(examineSample.getDelegateQuan());
		TextView tv6 = (TextView) findViewById(R.id.tv6);
		tv6.setText(examineSample.getProjectPart());
		TextView tv7 = (TextView) findViewById(R.id.tv7);
		tv7.setText(examineSample.getProduceFactory());
		TextView tv8 = (TextView) findViewById(R.id.tv8);
		tv8.setText(examineSample.getRecord_Certificate());
		TextView tv9 = (TextView) findViewById(R.id.tv9);
		tv9.setText(examineSample.getDetectionDate());
		TextView tv10 = (TextView) findViewById(R.id.tv10);
		tv10.setText(examineSample.getExam_Result());
		TextView tv11 = (TextView) findViewById(R.id.tv11);
		tv11.setText(examineSample.getAccessRuleCode());
		TextView tv12 = (TextView) findViewById(R.id.tv12);
		tv12.setText(examineSample.getDetectonRule());

		//loadHtmlFile("html/va_sample_detail.html", "file:///android_asset/html/", jsonElement);
		
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

}
