package com.lessu.xieshi.module.construction;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.Buttons.Button;
import com.lessu.xieshi.R;

import butterknife.ButterKnife;

public class SampleSearchActivity extends NavigationActivity {
	String notFinishedOnly = "true";
	private RadioGroup rg_search;
	private RadioButton rb_yes;
	private RadioButton rb_no;

/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.construction_search_activity);
		this.setTitle("样品查询");
		ButterKnife.bind(this);
		rg_search = (RadioGroup) findViewById(R.id.rg_search);
		rb_yes = (RadioButton) findViewById(R.id.rb_yes);
		rb_no = (RadioButton) findViewById(R.id.rb_no);
		Button bt_search = (Button) findViewById(R.id.bt_search);
		rg_search.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				if(i==rb_yes.getId()){
					notFinishedOnly = "true";
				}else if(i==rb_no.getId()){
					notFinishedOnly = "false";
				}
			}
		});
		bt_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putString("NotFinishedOnly", notFinishedOnly);
				TextView tv = (TextView)(findViewById(R.id.queryStrTextView));
				String queryStr = tv.getText().toString();
				bundle.putString("QueryStr", queryStr);
				setResult(RESULT_OK, SampleSearchActivity.this.getIntent().putExtras(bundle));
				finish();
			}
		});
	}*/

	@Override
	protected int getLayoutId() {
		return R.layout.construction_search_activity;
	}

	@Override
	protected void initView() {
		this.setTitle("样品查询");
		rg_search = (RadioGroup) findViewById(R.id.rg_search);
		rb_yes = (RadioButton) findViewById(R.id.rb_yes);
		rb_no = (RadioButton) findViewById(R.id.rb_no);
		Button bt_search = (Button) findViewById(R.id.bt_search);
		rg_search.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				if(i==rb_yes.getId()){
					notFinishedOnly = "true";
				}else if(i==rb_no.getId()){
					notFinishedOnly = "false";
				}
			}
		});
		bt_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putString("NotFinishedOnly", notFinishedOnly);
				TextView tv = (TextView)(findViewById(R.id.queryStrTextView));
				String queryStr = tv.getText().toString();
				bundle.putString("QueryStr", queryStr);
				setResult(RESULT_OK, SampleSearchActivity.this.getIntent().putExtras(bundle));
				finish();
			}
		});
	}

	public void searchButtonDidClick(){
		Bundle bundle = new Bundle();
		bundle.putString("NotFinishedOnly", notFinishedOnly);
		TextView tv = (TextView)(findViewById(R.id.queryStrTextView));
		String queryStr = tv.getText().toString();
		bundle.putString("QueryStr", queryStr);
		setResult(RESULT_OK, this.getIntent().putExtras(bundle));
		finish();
	}
}
