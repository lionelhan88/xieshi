package com.lessu.xieshi.module.todaystatistics;

import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ProjectListActivity  extends NavigationActivity implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_list_activity);
		ListView listView = (ListView) findViewById(R.id.listView);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(this);
		this.setTitle("工程查询");
		navigationBar.setBackgroundColor(0xFF3598DC);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
	}
	
	protected BaseAdapter adapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return 3;
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parentViewGroup) {
			if (view == null){
				view = View.inflate(ProjectListActivity.this, R.layout.construction_list_item, null);
			}

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
