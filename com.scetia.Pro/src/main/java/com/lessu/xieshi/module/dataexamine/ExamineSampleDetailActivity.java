package com.lessu.xieshi.module.dataexamine;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.EasyGson;
import com.google.gson.JsonElement;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.xieshi.bean.ExamineSample;

public class ExamineSampleDetailActivity extends NavigationActivity {
/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examine_sample_detail_activity);
		this.setTitle("样品详情");
		ButterKnife.bind(this);
	}*/

	@Override
	protected int getLayoutId() {
		return R.layout.examine_sample_detail_activity;
	}

	@Override
	protected void initView() {
		this.setTitle("样品详情");
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
	}
}
