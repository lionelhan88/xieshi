package com.lessu.xieshi.construction;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.Buttons.Button;
import com.lessu.xieshi.R;

import butterknife.ButterKnife;

public class ConstructionSearchActivity extends NavigationActivity {
	String notFinishedOnly = "true";
	private RadioGroup rg_search;
	private RadioButton rb_yes;
	private RadioButton rb_no;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.construction_search_activity);
		navigationBar.setBackgroundColor(0xFF3598DC);
		ButterKnife.inject(this);
		//TextView tv = (TextView)(findViewById(R.id.notFinishedOnlyTextView));
		rg_search = (RadioGroup) findViewById(R.id.rg_search);
		rb_yes = (RadioButton) findViewById(R.id.rb_yes);
		rb_no = (RadioButton) findViewById(R.id.rb_no);
		Button bt_search = (Button) findViewById(R.id.bt_search);
		//tv.setText("否");
        this.setTitle("工地搜索");
        
        BarButtonItem searchButtonitem = new BarButtonItem(this , "确认" );
        searchButtonitem.setOnClickMethod(this,"searchButtonDidClick");
    
        //navigationBar.setRightBarItem(searchButtonitem);
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
				setResult(RESULT_OK, ConstructionSearchActivity.this.getIntent().putExtras(bundle));
				finish();
			}
		});
	}

//	@OnClick(R.id.finishedButton)
//	protected void finishedButtonDidClick(){
//		final String[] itemString = {"否","是"};
//		new AlertDialog.Builder(this)
//		.setTitle("已完工")
//		.setIcon(android.R.drawable.ic_dialog_info)
//		.setSingleChoiceItems(itemString, 0,
//		  new DialogInterface.OnClickListener() {
//
//		     public void onClick(DialogInterface dialog, int which) {
//		    	 if (which==0){
//		    		 notFinishedOnly = "true";
//		    	 }
//		    	 else{
//		    		 notFinishedOnly = "false";
//		    	 }
//		        TextView tv = (TextView)(findViewById(R.id.notFinishedOnlyTextView));
//		        tv.setText(itemString[which]);
//		        dialog.dismiss();
//		     }
//		  }
//		)
//		.setNegativeButton("取消", null)
//		.show();
//	}
	
	protected void searchButtonDidClick(){
		Bundle bundle = new Bundle();
		bundle.putString("NotFinishedOnly", notFinishedOnly);
		TextView tv = (TextView)(findViewById(R.id.queryStrTextView));
		String queryStr = tv.getText().toString();
		bundle.putString("QueryStr", queryStr);
		setResult(RESULT_OK, this.getIntent().putExtras(bundle));
		finish();
	}
}
