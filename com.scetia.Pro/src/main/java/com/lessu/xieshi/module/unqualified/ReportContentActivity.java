package com.lessu.xieshi.module.unqualified;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.EasyGson;
import com.google.gson.JsonElement;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.bean.ListReportDetail;
import com.lessu.xieshi.bean.ReportContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportContentActivity extends NavigationActivity implements View.OnClickListener {
	private WebView detailWebView;
	private Map<String,String> activtyMapper;
	private ListView lv_report_content;
	private ArrayList<ListReportDetail> al;
	private LinearLayout ll_reportcontent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_content_activity);

//		if(detailWebView == null){
//			detailWebView = (WebView) findViewById(R.id.sample_detail);
//		}
		lv_report_content = (ListView) findViewById(R.id.lv_report_content);
		ll_reportcontent = (LinearLayout) findViewById(R.id.ll_reportcontent);
		this.setTitle("检测报告内容");
		navigationBar.setBackgroundColor(0xFF3598DC);
		ll_reportcontent.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		HashMap<String, Object> params = new HashMap<String, Object>();
		Bundle bundelForData=this.getIntent().getExtras();
		String Report_id = bundelForData.getString("Report_id");
		String Checksum = bundelForData.getString("Checksum");
		String Token = LSUtil.valueStatic("Token");


		params.put("Token", Token);
		params.put("Report_id", Report_id);
		params.put("Checksum", Checksum);

		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUQ.asmx/ProjectConsign"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
				al=new ArrayList<ListReportDetail>();
				System.out.println("result..."+result);
				String jsonString = result.getAsJsonObject().get("Data").toString();
				if(jsonString.equals("null")){
					return;
				}
				JsonElement jsonElement = EasyGson.jsonFromString(jsonString);
				System.out.println(jsonString);
				ReportContent reportContent = GsonUtil.JsonToObject(jsonString, ReportContent.class);
				al.add(new ListReportDetail("报告编号",reportContent.getReport_ID()));
				al.add(new ListReportDetail("委托编号",reportContent.getConSign_ID()));
				al.add(new ListReportDetail("检测类别",reportContent.getExam_Kind()));
				al.add(new ListReportDetail("工程连续号",reportContent.getProject_SSN()));
				al.add(new ListReportDetail("委托单位",reportContent.getEntrustUnitName()));
				al.add(new ListReportDetail("工程名称",reportContent.getProJectName()));
				al.add(new ListReportDetail("防伪校验码",reportContent.getIdentifyingCode()));
				al.add(new ListReportDetail("工程地址",reportContent.getProjectAddress()));
				al.add(new ListReportDetail("施工单位",reportContent.getBuildUnitName()));
				al.add(new ListReportDetail("见证单位",reportContent.getSuperviseUnitName()));
				al.add(new ListReportDetail("取样人(编号)",reportContent.getSampling()));
				al.add(new ListReportDetail("见证人(编号)",reportContent.getWitness()));
				al.add(new ListReportDetail("委托日期",reportContent.getDetectonDate()));
				al.add(new ListReportDetail("报告日期",reportContent.getReport_CreateTime()));
				al.add(new ListReportDetail("检测机构名称",reportContent.getMemberName()));
				al.add(new ListReportDetail("检测单位地址",reportContent.getContactAddress()));
				lv_report_content.setAdapter(new BaseAdapter() {
					@Override
					public int getCount() {
						return al.size();
					}

					@Override
					public Object getItem(int i) {
						return al.get(i);
					}

					@Override
					public long getItemId(int i) {
						return i;
					}

					@Override
					public View getView(int i, View view, ViewGroup viewGroup) {
						ViewHolder holder;
						if(view==null){
							holder=new ViewHolder();
							view=View.inflate(ReportContentActivity.this,R.layout.item_reportcontent,null);
							holder.tv1= (TextView) view.findViewById(R.id.item_tv1);
							holder.tv2= (TextView) view.findViewById(R.id.item_tv2);
							view.setTag(holder);
						}else{
							holder= (ViewHolder) view.getTag();
						}
						if(i%2==0){
							view.setBackgroundColor(Color.parseColor("#eaeaea"));
						}else{
							view.setBackgroundColor(Color.parseColor("#ffffff"));
						}
						holder.tv1.setText(al.get(i).tv1);
						holder.tv2.setText(al.get(i).tv2);
						return view;
					}
				});
				//loadHtmlFile("html/uq_report_detail.html", "file:///android_asset/html/", jsonElement);
			}
		});

	}
	static class ViewHolder{
		TextView tv1;
		TextView tv2;
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent (ReportContentActivity.this, SampleListActivity.class);
		Bundle bundle = new Bundle();
		Bundle bundelForData=this.getIntent().getExtras();
		String Report_ID = bundelForData.getString("Report_id");
		String Checksum = bundelForData.getString("Checksum");
		bundle.putString("Report_id", Report_ID);
		bundle.putString("Checksum", Checksum);
		intent.putExtras(bundle);
		startActivity(intent);
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
//
//	@Override
//	public boolean onReceiveEvent(String event, Map<String, String> params) {
//
//		if (event != null){
//			if (event.equalsIgnoreCase("sampleList")){
//				Intent intent = new Intent (ReportContentActivity.this, SampleListActivity.class);
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
//		return false;
//	}

}
