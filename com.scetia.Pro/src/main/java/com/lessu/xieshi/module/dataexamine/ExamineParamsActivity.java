package com.lessu.xieshi.module.dataexamine;

import com.google.gson.EasyGson;
import com.google.gson.GsonValidate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ExamineParamsActivity extends NavigationActivity implements OnItemClickListener {
	JsonArray list = new JsonArray();
	@Override
	protected int getLayoutId() {
		return R.layout.examine_params_activity;
	}

	@Override
	protected void initView() {
		this.setTitle("参数列表");
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Bundle bundle = getIntent().getExtras();
		
		String jsonString = bundle.getString("jsonString");
		
		JsonElement jsonElement = EasyGson.jsonFromString(jsonString);
		JsonElement json = jsonElement.getAsJsonObject().get("ParamInfo");
		if (json.isJsonArray()){
			list = json.getAsJsonArray();
		}
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
	}
	
	protected BaseAdapter adapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return list.size();
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parentViewGroup) {
			if (view == null){
				view = View.inflate(ExamineParamsActivity.this, R.layout.examine_params_item, null);
			}
			TextView textView1 = (TextView) view.findViewById(R.id.paramNameTextView);
			textView1.setText(GsonValidate.getStringByKeyPath(list.get(position), "ParameterName",""));
			
			TextView textView2 = (TextView) view.findViewById(R.id.standardValueTextView);
			textView2.setText(GsonValidate.getStringByKeyPath(list.get(position), "UCStandardValue",""));
			
			TextView textView3 = (TextView) view.findViewById(R.id.checkValueTextView);
			textView3.setText(GsonValidate.getStringByKeyPath(list.get(position), "UCTestValue",""));
			
			TextView textView4 = (TextView) view.findViewById(R.id.checkResultTextView);
			textView4.setText(GsonValidate.getStringByKeyPath(list.get(position), "UCResultValue",""));
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
