package com.lessu.xieshi.module.unqualified;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.EasyGson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.DensityUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.xieshi.bean.ListSampleDetail;
import com.lessu.xieshi.bean.Yangpinxinxi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SampleDetailActivity extends NavigationActivity {
	private WebView sampledetailWebView;
	private Map<String,String> activtyMapper;
	String UqSampleID = "";
	String ExecContent = "";
	private TextView tv_bianhao;
	private TextView tv_hege;
	private ListView lv_yangpinxinxi;
	private ArrayList<ListSampleDetail> al;

/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_detail_activity);
		this.setTitle("样品信息");
		tv_bianhao = (TextView) findViewById(R.id.tv_bianhao);
		tv_hege = (TextView) findViewById(R.id.tv_hege);
		lv_yangpinxinxi = (ListView) findViewById(R.id.lv_yangpinxinxi);
		BarButtonItem	handleButtonItem = new BarButtonItem(this , "处理" );
		handleButtonItem.setOnClickMethod(this,"handleButtonDidClick");	
        navigationBar.setRightBarItem(handleButtonItem);
        dataRequest();
	}
*/

	@Override
	protected int getLayoutId() {
		return R.layout.sample_detail_activity;
	}

	@Override
	protected void initView() {
		this.setTitle("样品信息");
		tv_bianhao = (TextView) findViewById(R.id.tv_bianhao);
		tv_hege = (TextView) findViewById(R.id.tv_hege);
		lv_yangpinxinxi = (ListView) findViewById(R.id.lv_yangpinxinxi);
		BarButtonItem	handleButtonItem = new BarButtonItem(this , "处理" );
		handleButtonItem.setOnClickMethod(this,"handleButtonDidClick");
		navigationBar.setRightBarItem(handleButtonItem);
		dataRequest();
	}

	private void dataRequest() {
		HashMap<String, Object> params = new HashMap<>();
		Bundle bundelForData=this.getIntent().getExtras(); 
		String Report_ID = bundelForData.getString("Report_id");
		String Checksum = bundelForData.getString("Checksum");
		String Sample_ID = bundelForData.getString("Sample_id");
		String Token =  Constants.User.GET_TOKEN();
		params.put("Token", Token);
		params.put("Report_id", Report_ID);
		params.put("Checksum", Checksum);
		params.put("Sample_id", Sample_ID);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUQ.asmx/SampleDetail"), params, new EasyAPI.ApiFastSuccessCallBack() {
					@Override
					public void onSuccessJson(JsonElement result) {
						al=new ArrayList<ListSampleDetail>();
						String jsonString = result.getAsJsonObject().get("Data").toString();
						Yangpinxinxi yangpinxinxi = GsonUtil.JsonToObject(jsonString, Yangpinxinxi.class);
						JsonElement jsonElement = EasyGson.jsonFromString(jsonString);
						JsonObject json = jsonElement.getAsJsonObject();
						UqSampleID = jsonElement.getAsJsonObject().get("UqSampleID").getAsString();
						try{
						ExecContent = jsonElement.getAsJsonObject().get("ExecContent").getAsString();
						}
						catch (Exception e){
							ExecContent = "";
						}
						if  (json.get("UqExecStatus").getAsString().equalsIgnoreCase("0")){
							json.remove("UqExecStatus");
							json.addProperty("UqExecStatus", "未处理");
						}else if (json.get("UqExecStatus").getAsString().equalsIgnoreCase("1")){
							json.remove("UqExecStatus");
							json.addProperty("UqExecStatus", "已处理");
						}else if (json.get("UqExecStatus").getAsString().equalsIgnoreCase("2")){
							json.remove("UqExecStatus");
							json.addProperty("UqExecStatus", "处理中");
						}
						tv_bianhao.setText(yangpinxinxi.getSample_ID());
						tv_hege.setText(yangpinxinxi.getExam_Result());
						al.add(new ListSampleDetail("样品名称:",yangpinxinxi.getSampleName()));
						al.add(new ListSampleDetail("代表数量:",yangpinxinxi.getDelegate_Quan()));
						al.add(new ListSampleDetail("委托编号:",yangpinxinxi.getConSign_ID()));
						al.add(new ListSampleDetail("检测项目:",yangpinxinxi.getItemName()));
						al.add(new ListSampleDetail("规格:",yangpinxinxi.getSpec_Cn()));
						al.add(new ListSampleDetail("强度:",yangpinxinxi.getGrade_Cn()));
						al.add(new ListSampleDetail("参数:",yangpinxinxi.getExam_parameter_Cn()));
						al.add(new ListSampleDetail("标识号区间:",yangpinxinxi.getBsNO()));
						al.add(new ListSampleDetail("备案证号:",yangpinxinxi.getRecord_Certificate()));
						al.add(new ListSampleDetail("生产厂家:",yangpinxinxi.getProduce_Factory()));
						al.add(new ListSampleDetail("工程部位:",yangpinxinxi.getProJect_Part()));
						al.add(new ListSampleDetail("日期:",yangpinxinxi.getMolding_Date()));
						al.add(new ListSampleDetail("龄期:",yangpinxinxi.getAgeTime()));
						lv_yangpinxinxi.setAdapter(new BaseAdapter() {
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
									view=View.inflate(SampleDetailActivity.this,R.layout.item_sampledetail2,null);
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
					}
				});
	}
	static class ViewHolder{
		TextView tv1;
		TextView tv2;
	}

	public void handleButtonDidClick(){
		final String[] itemString = {"已处理","处理中"};
		final String[] valueString = {"1","2"};
		new AlertDialog.Builder(this)  
		.setTitle("处理状态")  
		.setIcon(android.R.drawable.ic_dialog_info)                  
		.setSingleChoiceItems(itemString, 0,   
		  new DialogInterface.OnClickListener() {  
		                              
		     public void onClick(DialogInterface dialog, int which) {  
		    	 final String UqExecStatus = valueString[which];
		    	 dialog.dismiss();  
		    	 final EditText contentEditText = new EditText(SampleDetailActivity.this);
		    	 contentEditText.setText(ExecContent);
		    	 contentEditText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(SampleDetailActivity.this,50)));
		    	 new AlertDialog.Builder(SampleDetailActivity.this).setTitle("请输入处理内容").setIcon(
		    		     android.R.drawable.ic_dialog_info).setView(contentEditText).setPositiveButton("确定", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								HashMap<String, Object> params = new HashMap<>();
						 		String Token = Constants.User.GET_TOKEN();
						 		params.put("Token", Token);
						 		params.put("ExecContent", contentEditText.getText().toString());
						 		ExecContent = contentEditText.getText().toString();
						 		params.put("UqExecStatus", UqExecStatus);
						 		params.put("UqSampleID", UqSampleID);
						 		EasyAPI.apiConnectionAsync(SampleDetailActivity.this, true, false, ApiMethodDescription.get("/ServiceUQ.asmx/ExecUqSamle"), params, new EasyAPI.ApiFastSuccessCallBack() {
						 					@Override
						 					public void onSuccessJson(JsonElement result) {
						 						String jsonString = result.getAsJsonObject().get("Data").toString();
						 						if (jsonString.equalsIgnoreCase("true")){
						 							LSAlert.showAlert(SampleDetailActivity.this, "处理成功！");
						 						}
						 					}
						 				});
						 		arg0.dismiss();  
							}
						})
		    		     .setNegativeButton("取消", null).show();
		    	 
		        
		     }  
		  }  
		)  
		.setNegativeButton("取消", null)  
		.show(); 
    }
}
