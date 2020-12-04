package com.lessu.xieshi.module.unqualified;

import java.util.HashMap;

import com.google.gson.EasyGson;
import com.google.gson.GsonValidate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SampleListActivity extends NavigationActivity implements OnItemClickListener {
	JsonArray list = new JsonArray();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.uq_sample_list_activity);
		ListView listView = (ListView) findViewById(R.id.listView);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(this);
		this.setTitle("报告结论");
		navigationBar.setBackgroundColor(0xFF3598DC);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		HashMap<String, Object> params = new HashMap<String, Object>();
		Bundle bundelForData=this.getIntent().getExtras(); 
		String Report_ID = bundelForData.getString("Report_id");
		String Checksum = bundelForData.getString("Checksum");
		String Token = LSUtil.valueStatic("Token");
		
		
		params.put("Report_id", Report_ID);
		params.put("Checksum", Checksum);
		params.put("Token", Token);
		
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUQ.asmx/SampleList"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				String jsonString = result.getAsJsonObject().get("Data").toString();
				JsonElement jsonElement = EasyGson.jsonFromString(jsonString);
				if (jsonElement.isJsonArray()){
					list = jsonElement.getAsJsonArray();
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
		Intent intent = new Intent (SampleListActivity.this, SampleDetailActivity.class) ;
		Bundle bundle = new Bundle();  
		Bundle bundelForData=this.getIntent().getExtras(); 
		String Report_ID = bundelForData.getString("Report_id");
		String Checksum = bundelForData.getString("Checksum");
		bundle.putString("Report_id", Report_ID);
		bundle.putString("Checksum", Checksum);
		bundle.putString("Sample_id", GsonValidate.getStringByKeyPath(list.get(position), "Sample_Id",""));
		intent.putExtras(bundle);
		startActivity(intent);  
	}
	
	protected BaseAdapter adapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return list.size();
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parentViewGroup) {
			if (view == null){
				view = View.inflate(SampleListActivity.this, R.layout.uq_sample_list_item, null);
			}
			TextView textViewSampleId = (TextView) view.findViewById(R.id.sampleIdTextView);
			textViewSampleId.setText(GsonValidate.getStringByKeyPath(list.get(position), "Sample_Id",""));
			
			TextView textViewExamResult = (TextView) view.findViewById(R.id.examResultTextView);
			textViewExamResult.setText(GsonValidate.getStringByKeyPath(list.get(position), "Exam_Result",""));

			return view;
		}
		
		
		@Override
		public long getItemId(int arg0) {
			return 0;
		}
		
		@Override
		public Object getItem(int arg0) {
			return null;
		}
		

	};

}
