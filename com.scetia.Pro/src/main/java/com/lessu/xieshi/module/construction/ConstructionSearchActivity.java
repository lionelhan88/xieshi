package com.lessu.xieshi.module.construction;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.Buttons.Button;
import com.lessu.xieshi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConstructionSearchActivity extends NavigationActivity {
	String notFinishedOnly = "true";
	@BindView(R.id.rg_search) RadioGroup rg_search;
	@BindView(R.id.rb_yes) RadioButton rb_yes;
	@BindView(R.id.rb_no) RadioButton rb_no;
	@BindView(R.id.bt_search) Button bt_search;

	@Override
	protected int getLayoutId() {
		return R.layout.construction_search_activity;
	}

	@Override
	protected void initView() {
		this.setTitle("工地搜索");
		BarButtonItem searchButtonitem = new BarButtonItem(this , "确认" );
		searchButtonitem.setOnClickMethod(this,"searchButtonDidClick");
		rg_search.setOnCheckedChangeListener((radioGroup, i) -> {
			if(i==rb_yes.getId()){
				notFinishedOnly = "true";
			}else if(i==rb_no.getId()){
				notFinishedOnly = "false";
			}
		});
		bt_search.setOnClickListener((View.OnClickListener) view -> {
			Bundle bundle = new Bundle();
			bundle.putString("NotFinishedOnly", notFinishedOnly);
			TextView tv = (TextView)(findViewById(R.id.queryStrTextView));
			String queryStr = tv.getText().toString();
			bundle.putString("QueryStr", queryStr);
			setResult(RESULT_OK, ConstructionSearchActivity.this.getIntent().putExtras(bundle));
			finish();
		});
	}

	protected void searchButtonDidClick(){
		Bundle bundle = new Bundle();
		bundle.putString("NotFinishedOnly", notFinishedOnly);
		TextView tv = findViewById(R.id.queryStrTextView);
		String queryStr = tv.getText().toString();
		bundle.putString("QueryStr", queryStr);
		setResult(RESULT_OK, this.getIntent().putExtras(bundle));
		finish();
	}
}
