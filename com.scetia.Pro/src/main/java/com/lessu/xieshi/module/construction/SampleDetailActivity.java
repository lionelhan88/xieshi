package com.lessu.xieshi.module.construction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.bean.ListSampleDetail;
import com.lessu.xieshi.bean.SampleDetail;
import com.scetia.Pro.common.Util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SampleDetailActivity extends NavigationActivity {
	private WebView sampledetailWebView;
	private Map<String,String> activtyMapper;
	private SampleDetail sampleDetail;
	private ArrayList<ListSampleDetail> al;
	private ListView lv_sample_detail;

/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_detail);
		lv_sample_detail = (ListView) findViewById(R.id.lv_sample_detail);
		this.setTitle("样品信息");
		navigationBar.setBackgroundColor(0xFF3598DC);
	}*/

	@Override
	protected int getLayoutId() {
		return R.layout.sample_detail;
	}

	@Override
	protected void initView() {
		this.setTitle("样品信息");
		lv_sample_detail = (ListView) findViewById(R.id.lv_sample_detail);
	}

	@Override
	protected void onStart() {
		super.onStart();
		HashMap<String, Object> params = new HashMap<String, Object>();
		Bundle bundelForData=this.getIntent().getExtras(); 
		String Id = bundelForData.getString("Id");
		String MemberCode = bundelForData.getString("MemberCode");
		String Token =  Constants.User.GET_TOKEN();
		
		
		params.put("Token", Token);
		params.put("Id", Id);
		params.put("MemberCode", MemberCode);
		
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceSM.asmx/SampleDetail"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
				al=new ArrayList<ListSampleDetail>();
				System.out.println(result);
				String jsonString = result.getAsJsonObject().get("Data").toString();
				System.out.println("样品信息"+jsonString);
				if(jsonString.equals("null")){
					return;
				}
				sampleDetail = GsonUtil.JsonToObject(jsonString, SampleDetail.class);
				al.add(new ListSampleDetail("检测项目",sampleDetail.getItemName()));
				al.add(new ListSampleDetail("样品名称",sampleDetail.getSampleName()));
				al.add(new ListSampleDetail("规格",sampleDetail.getSpecName()));
				al.add(new ListSampleDetail("强度",sampleDetail.getGradeName()));
				al.add(new ListSampleDetail("标签号码",sampleDetail.getCoreCodeNo()));
				al.add(new ListSampleDetail("操作状态",sampleDetail.getDownStatus()));
				al.add(new ListSampleDetail("检测状态",sampleDetail.getSample_Status()));
				al.add(new ListSampleDetail("检测类别",sampleDetail.getExam_Kind()));
				al.add(new ListSampleDetail("参数",sampleDetail.getExam_Parameter_Cn()));
				//al.add(new ListSampleDetail("原样品记录Id",sampleDetail.getReExam_OldSample_Id()));
				al.add(new ListSampleDetail("工程部位",sampleDetail.getProJect_Part()));
				al.add(new ListSampleDetail("见证人",sampleDetail.getJZCertificate()));
				al.add(new ListSampleDetail("取样人",sampleDetail.getQYCertificate()));
				al.add(new ListSampleDetail("备案证",sampleDetail.getRecord_Certificate()));
				al.add(new ListSampleDetail("生产厂家",sampleDetail.getProduce_Factory()));
				al.add(new ListSampleDetail("代表数量",sampleDetail.getDelegate_Quan()));


				if(!sampleDetail.getMolding_Date().equals("-")&&!sampleDetail.getMolding_Date().equals("null")){
					al.add(new ListSampleDetail("制作日期",sampleDetail.getMolding_Date()));
				}

				al.add(new ListSampleDetail("龄期",sampleDetail.getAgeTime()));
				al.add(new ListSampleDetail("入库批号",sampleDetail.getBatchNumber()));
				lv_sample_detail.setAdapter(new BaseAdapter() {
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
							view=View.inflate(SampleDetailActivity.this,R.layout.item_sampledetail,null);
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

				//JsonElement jsonElement = EasyGson.jsonFromString(jsonString);
				//loadHtmlFile("html/new_sample_detail.html", "file:///android_asset/html/", jsonElement);
			}
		});
		
	}
	static class ViewHolder{
		TextView tv1;
		TextView tv2;
	}

//	@Override
//	public WebView getWebView() {
//		if(sampledetailWebView == null){
//			sampledetailWebView = (WebView) findViewById(R.id.sample_detail);
//		}
//		return sampledetailWebView;
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
//		return false;
//	}
}
