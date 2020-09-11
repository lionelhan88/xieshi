package com.lessu.xieshi.todaystatistics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.Utils.Shref;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminTodayStatisticsSearchActivity extends NavigationActivity implements View.OnClickListener {
	String projectName = "";
	String projectArea = "";
	String baogaobianhao="";
	String jianshedanwei="";
	String shigongdanwei="";
	String jianlidanwei="";
	String jiancedanwei="";
	//private RadioButton rb_xainshitongji;
	//private RadioButton rb_xainshiall;
	private boolean isxianshiall;
	private int xianshistage=-1;
	private int rangeindex;
	private String currentLocation;
	private TextView tv_xianshi;
	private CheckBox cb_xainshi;
	private BarButtonItem handleButtonItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//解决输入框弹出键盘向上挤压布局，导致输入内容不能及时显示
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.admin_today_statistics_search_activity);
		navigationBar.setBackgroundColor(0xFF3598DC);
		this.setTitle("工地搜索");
		handleButtonItem = new BarButtonItem(this,R.drawable.back);
		handleButtonItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		navigationBar.setLeftBarItem(handleButtonItem);
		// BarButtonItem	searchButtonitem = new BarButtonItem(this , "确认" );
		//searchButtonitem.setOnClickMethod(this,"searchButtonDidClick");
		// navigationBar.setRightBarItem(searchButtonitem);

		Bundle bundle = getIntent().getExtras();
		projectName = bundle.getString("ProjectName");
		projectArea = bundle.getString("ProjectArea");
		baogaobianhao = bundle.getString("baogaobianhao");
		jianshedanwei = bundle.getString("jianshedanwei");
		shigongdanwei = bundle.getString("shigongdanwei");
		jianlidanwei = bundle.getString("jianlidanwei");
		jiancedanwei = bundle.getString("jiancedanwei");
		rangeindex = bundle.getInt("range");
		currentLocation = bundle.getString("CurrentLocation");
		System.out.println("统计详情。。。"+rangeindex);
		TextView tv1 = (TextView)(findViewById(R.id.projectNameTextView));
		tv1.setText(projectName);
		Button bt_qveren = (Button) findViewById(R.id.bt_qveren);
		bt_qveren.setOnClickListener(this);

		TextView tv2 = (TextView)(findViewById(R.id.projectAreaTextView));
		tv2.setText(projectArea);
		TextView tv3 = (TextView)(findViewById(R.id.baogaobianhao));
		tv3.setText(baogaobianhao);
		TextView tv4 = (TextView)(findViewById(R.id.jianshedanwei));
		tv4.setText(jianshedanwei);
		TextView tv5 = (TextView)(findViewById(R.id.shigongdanwei));
		tv5.setText(shigongdanwei);
		TextView tv6 = (TextView)(findViewById(R.id.jianlidanwei));
		tv6.setText(jianlidanwei);
		TextView tv7 = (TextView)(findViewById(R.id.jiancedanwei));
		tv7.setText(jiancedanwei);

		cb_xainshi = (CheckBox) findViewById(R.id.cb_xainshi);
		xianshistage=1;
		if(Shref.getString(AdminTodayStatisticsSearchActivity.this, Common.USERNAME,"").equals("gly")){
			//xianshistage=2;
			//tv_xianshi.setText("显示所有工地统计信息");
		}else{
			//xianshistage=1;
			//tv_xianshi.setText("显示当前监督站所属工地统计信息");
		}


		cb_xainshi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					xianshistage=2;
				}else{
					xianshistage=1;
				}
			}
		});
		ButterKnife.bind(this);
	}

	@OnClick(R.id.ProjectAreaButton)
	protected void ProjectAreaButtonDidClick(){
		final String[] itemString = {"金山区","崇明县","浦东新区","南汇区","闵行区","卢湾区","徐汇区","奉贤区","宝山区","杨浦区","普陀区","黄浦区","青浦区","松江区","外省市","闸北区","长宁区","嘉定区","静安区","虹口区"};
		final String[] valueString = itemString;
		new AlertDialog.Builder(this)
				.setTitle("工程区县")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(itemString, 0,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								TextView tv = (TextView)(findViewById(R.id.projectAreaTextView));
								tv.setText(itemString[which]);
								projectArea = valueString[which];
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton("取消", null)
				.show();
	}

	@Override
	public void onClick(View view) {

		Bundle bundle = new Bundle();

		TextView tv1 = (TextView) (findViewById(R.id.projectNameTextView));
		projectName = tv1.getText().toString();
		bundle.putString("ProjectName", projectName);
		TextView tv2 = (TextView) (findViewById(R.id.projectAreaTextView));
		projectArea = tv2.getText().toString();
		bundle.putString("ProjectArea", projectArea);
		TextView tv3 = (TextView) (findViewById(R.id.baogaobianhao));
		baogaobianhao = tv3.getText().toString();
		bundle.putString("baogaobianhao", baogaobianhao);
		TextView tv4 = (TextView) (findViewById(R.id.jianshedanwei));
		jianshedanwei = tv4.getText().toString();
		bundle.putString("jianshedanwei", jianshedanwei);
		TextView tv5 = (TextView) (findViewById(R.id.shigongdanwei));
		shigongdanwei = tv5.getText().toString();
		bundle.putString("shigongdanwei", shigongdanwei);
		TextView tv6 = (TextView) (findViewById(R.id.jianlidanwei));
		jianlidanwei = tv6.getText().toString();
		bundle.putString("jianlidanwei", jianlidanwei);
		TextView tv7 = (TextView) (findViewById(R.id.jiancedanwei));
		jiancedanwei = tv7.getText().toString();
		bundle.putString("jiancedanwei", jiancedanwei);
		bundle.putBoolean("sousuo", true);
		if(xianshistage==1) {
			//tv_xianshi.setText("显示当前监督站所属工地统计信息");
			setResult(RESULT_OK, this.getIntent().putExtras(bundle));
			finish();
		}else if(xianshistage==2){
			//tv_xianshi.setText("显示所有工地统计信息");
			bundle.putBoolean("isxianshall", true);
			bundle.putInt("DistanceRange",rangeindex);
			System.out.println("发送。。。。。。。。。"+rangeindex);
			bundle.putString("CurrentLocation",currentLocation);
			Intent intent = new Intent(AdminTodayStatisticsSearchActivity.this, TodayStatisticsDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else{
			ToastUtil.showShort("请选择显示的内容");
		}
	}
}
