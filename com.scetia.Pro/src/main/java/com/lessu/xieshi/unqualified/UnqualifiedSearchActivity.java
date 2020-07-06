package com.lessu.xieshi.unqualified;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.XieShiSlidingMenuActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnqualifiedSearchActivity extends XieShiSlidingMenuActivity implements View.OnClickListener {
	String EntrustType = "-1";
	String ManageUnitID = "";
	String UqExecStatus = "-1";
	String ProjectArea = "";
	String preDate = "";
	String endDate = "";
	String QueryKey = "";
	String Type = "0";
	ArrayList<String> checkNameList = new ArrayList<String>();
	ArrayList<String> checkValueList = new ArrayList<String>();

	private TextView tv_jianceleibie;
	ArrayList<String> itemKindName = new ArrayList<String>();
	ArrayList<String> itemKindId = new ArrayList<String>();
	ArrayList<String> itemitemName = new ArrayList<String>();
	ArrayList<String> itemitemId = new ArrayList<String>();
	String JianceKey="";
	private TextView tv_jiancexiangmu;
	private Button bt_qveding;
	private Button bt_qveding1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unqualified_search_activity);
		navigationBar.setBackgroundColor(0xFF3598DC);
		this.setTitle("不合格信息查询");
		double pi = Math.PI;
		//BarButtonItem	searchButtonitem = new BarButtonItem(this , "搜索" );
		//searchButtonitem.setOnClickMethod(this,"searchButtonDidClick");

		//navigationBar.setRightBarItem(searchButtonitem);

		BarButtonItem	menuButtonitem = new BarButtonItem(this ,R.drawable.icon_navigation_menu);
		menuButtonitem.setOnClickMethod(this,"menuButtonDidClick");
		//navigationBar.setLeftBarItem(menuButtonitem);
		tv_jianceleibie = (TextView)(findViewById(R.id.tv_jianceleibie));
		tv_jiancexiangmu = (TextView)(findViewById(R.id.tv_jiancexiangmu));
		bt_qveding = (Button) findViewById(R.id.bt_qveding);
		bt_qveding1 = (Button) findViewById(R.id.bt_qveding1);
		bt_qveding.setOnClickListener(this);
		bt_qveding1.setOnClickListener(this);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Token", LSUtil.valueStatic("Token"));
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceSource.asmx/BHGItemSource"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				JsonArray jsonArray = result.getAsJsonObject().get("Data").getAsJsonArray();
				for (int i=0;i<jsonArray.size();i++){
					checkNameList.add(jsonArray.get(i).getAsJsonObject().get("BhgItem").getAsString());
					checkValueList.add(jsonArray.get(i).getAsJsonObject().get("BhgId").getAsString());
				}
			}
		});

		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceSource.asmx/ItemKindSource"), params, new EasyAPI.ApiFastSuccessCallBack() {
			@Override
			public void onSuccessJson(JsonElement result) {
				JsonArray jsonArray = result.getAsJsonObject().get("Data").getAsJsonArray();
				for (int i=0;i<jsonArray.size();i++){
					itemKindName.add(jsonArray.get(i).getAsJsonObject().get("Name").getAsString());
					itemKindId.add(jsonArray.get(i).getAsJsonObject().get("Id").getAsString());
				}
			}
		});



		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		String todayString = df.format(today);
		//初始化设置当天日期为截至日期
		((TextView)findViewById(R.id.EndDateEditText)).setText(todayString);

		((TextView)findViewById(R.id.Report_CreateDateEditTextEnd)).setText(todayString);
		//工程检测起始日期默认为前一个月
		calendar.add(Calendar.MONTH,-1);
		String pretodayString = df.format(calendar.getTime());
		((TextView)findViewById(R.id.StartDateEditText)).setText(pretodayString);

		//材料检测起始日期默认为前一天
		calendar.add(Calendar.MONTH,1);
		calendar.add(Calendar.DAY_OF_MONTH,-1);
		String reportCreateDate = df.format(calendar.getTime());
		((TextView)findViewById(R.id.Report_CreateDateEditTextPre)).setText(reportCreateDate);

		Intent intent = getIntent();
		if(intent!=null){
			String projectid = intent.getStringExtra("projectId");
			String projectName = intent.getStringExtra("projectName");
			String projctArea = intent.getStringExtra("projectArea");
			((EditText)findViewById(R.id.ProjectNameEditText)).setText(projectName);
			((Button)(findViewById(R.id.ProjectAreaButton))).setText(projctArea);
		}

		ButterKnife.bind(this);
	}
	/**

	 * 日期控件的事件

	 */

	private DatePickerDialog.OnDateSetListener mDateSetListenerPre = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,

							  int dayOfMonth) {
			int yearDefault = year;
			int monthOfyearDefault = monthOfYear+1;
//    	   if (monthOfYear==0){
//    		   yearDefault = year-1;
//    		   monthOfyearDefault = 12;
//    		   
//    	   }
			preDate = String.valueOf(yearDefault)+"-"+String.valueOf(monthOfyearDefault)+"-"+String.valueOf(dayOfMonth);
			((TextView)findViewById(R.id.Report_CreateDateEditTextPre)).setText(preDate);
		}

	};

	private DatePickerDialog.OnDateSetListener mDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,

							  int dayOfMonth) {

			endDate = String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth);
			((TextView)findViewById(R.id.Report_CreateDateEditTextEnd)).setText(endDate);
		}

	};
	@OnClick(R.id.Report_CreateDateEditTextPre)
	protected void Report_CreateDateEditTextPreDidClick(){
		Time t=new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。  
		int preYear = t.year;
		int preMonth = t.month;
		int preDay = t.monthDay;
		new DatePickerDialog(this, mDateSetListenerPre, preYear, preMonth, preDay).show();

	}

	@OnClick(R.id.Report_CreateDateEditTextEnd)
	protected void Report_CreateDateEditTextEndDidClick(){
		Time t=new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。  
		int endYear = t.year;
		int endMonth = t.month;
		int endDay = t.monthDay;
		new DatePickerDialog(this, mDateSetListenerEnd, endYear, endMonth, endDay).show();
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
								TextView tv = (TextView)(findViewById(R.id.ProjectAreaTextView));
								tv.setText(itemString[which]);
								ProjectArea = valueString[which];
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton("取消", null)
				.show();
	}
	//在这里加所属区县bt_suoshuqvxian   tv_suoshuqvxian

	@OnClick(R.id.EntrustTypeButton)
	protected void typeButtonDidClick(){
		final String[] itemString = {"全部","常规检测","监理平行检测"};
		final String[] valueString = {"-1","0","1"};
		new AlertDialog.Builder(this)
				.setTitle("检测性质")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(itemString, 0,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								TextView tv = (TextView)(findViewById(R.id.EntrustTypeTextView));
								tv.setText(itemString[which]);
								EntrustType = valueString[which];
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton("取消", null)
				.show();
	}

	@OnClick(R.id.ManageUnitIDButton)
	protected void ManageUnitIDButton(){
		final String[] itemString = {"资源服务"};
		new AlertDialog.Builder(this)
				.setTitle("受监站Id")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(itemString, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								TextView tv = (TextView)(findViewById(R.id.ManageUnitIDTextView));
								tv.setText(itemString[which]);
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton("取消", null)
				.show();
	}

	@OnClick(R.id.UqExecStatusButton)
	protected void UqExecStatusButton(){
		final String[] itemString = {"全部","未处理","已处理","处理中"};
		final String[] valueString = {"-1","0","1","2"};
		new AlertDialog.Builder(this)
				.setTitle("处理状态")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(itemString, 0,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								TextView tv = (TextView)(findViewById(R.id.UqExecStatusTextView));
								tv.setText(itemString[which]);
								UqExecStatus = valueString[which];
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton("取消", null)
				.show();
	}

	@OnClick(R.id.bt_jianceleibie)
	protected void bt_jianceleibieDidClick(){
		tv_jiancexiangmu.setText("");
		JianceKey="";
		itemitemName.clear();
		itemitemId.clear();
		final String[] nameString = (String[])itemKindName.toArray(new String[itemKindName.size()]);
		final String[] idString = (String[])itemKindId.toArray(new String[itemKindId.size()]);
		new AlertDialog.Builder(this)
				.setTitle("检测类别")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(nameString, 0,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								HashMap<String, Object> params = new HashMap<String, Object>();
								params.put("Token", LSUtil.valueStatic("Token"));
								params.put("Condition" ,idString[which]);
								System.out.println(params);
								EasyAPI.apiConnectionAsync(UnqualifiedSearchActivity.this, true, false, ApiMethodDescription.get("/ServiceSource.asmx/ItemItemSource"), params, new EasyAPI.ApiFastSuccessCallBack() {
									@Override
									public void onSuccessJson(JsonElement result) {
										JsonArray jsonArray = result.getAsJsonObject().get("Data").getAsJsonArray();
										for (int i=0;i<jsonArray.size();i++){
											itemitemName.add(jsonArray.get(i).getAsJsonObject().get("Name").getAsString());
											itemitemId.add(jsonArray.get(i).getAsJsonObject().get("Id").getAsString());
										}
									}
								});

								tv_jianceleibie.setText(nameString[which]);
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton("取消", null)
				.show();
	}

	@OnClick(R.id.bt_jiancexiangmu)
	protected void bt_jiancexiangmuDidClick(){
		if(itemitemName.size()<=0){
			LSAlert.showAlert(UnqualifiedSearchActivity.this, "请先选择类别！");
			return;
		}
		final String[] itemnameString = (String[])itemitemName.toArray(new String[itemitemName.size()]);
		final String[] itemidString = (String[])itemitemId.toArray(new String[itemitemId.size()]);
		new AlertDialog.Builder(this)
				.setTitle("检测项目")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(itemnameString, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								tv_jiancexiangmu.setText(itemnameString[which]);
								JianceKey = itemidString[which];
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton("取消", null)
				.show();
	}




	@OnClick(R.id.CheckItemButton)
	protected void CheckItemButtonDidClick(){
		final String[] itemString = (String[])checkNameList.toArray(new String[checkNameList.size()]);
		final String[] valueString = (String[])checkValueList.toArray(new String[checkValueList.size()]);
		new AlertDialog.Builder(this)
				.setTitle("检测项目")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(itemString, 0,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								TextView tv = (TextView)(findViewById(R.id.CheckItemTextView));
								tv.setText(itemString[which]);
								QueryKey = valueString[which];
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton("取消", null)
				.show();
	}
	@OnClick(R.id.ll_cailiao)
	protected void materialButtonDidClick(){
		Type = "0";
		findViewById(R.id.materialView).setVisibility(View.VISIBLE);
		findViewById(R.id.ll_cailiao).setBackgroundResource(R.drawable.anniu);
		TextView tv_cailiao = (TextView) findViewById(R.id.tv_cailiao);
		tv_cailiao.setTextColor(this.getResources().getColor(R.color.white));
		findViewById(R.id.iv_cailiao).setBackgroundResource(R.drawable.cailiao);
		TextView tv_gongcheng = (TextView) findViewById(R.id.tv_gongcheng);
		tv_gongcheng.setTextColor(this.getResources().getColor(R.color.textcolor));
		findViewById(R.id.iv_gongcheng).setBackgroundResource(R.drawable.gongcheng2);


		findViewById(R.id.projectView).setVisibility(View.GONE);
		findViewById(R.id.ll_gongchen).setBackgroundColor(Color.TRANSPARENT);
	}

	@OnClick(R.id.ll_gongchen)
	protected void projectButtonDidClick(){
		Type = "1";
		findViewById(R.id.projectView).setVisibility(View.VISIBLE);
		findViewById(R.id.ll_gongchen).setBackgroundResource(R.drawable.anniu);

		TextView tv_cailiao = (TextView) findViewById(R.id.tv_cailiao);
		tv_cailiao.setTextColor(this.getResources().getColor(R.color.textcolor));
		findViewById(R.id.iv_cailiao).setBackgroundResource(R.drawable.cailiao1);
		TextView tv_gongcheng = (TextView) findViewById(R.id.tv_gongcheng);
		tv_gongcheng.setTextColor(this.getResources().getColor(R.color.white));
		findViewById(R.id.iv_gongcheng).setBackgroundResource(R.drawable.gongcheng1);



		findViewById(R.id.materialView).setVisibility(View.GONE);
		findViewById(R.id.ll_cailiao).setBackgroundColor(Color.TRANSPARENT);
	}

	/**

	 * 日期控件的事件

	 */

	private DatePickerDialog.OnDateSetListener mDateSetListenerPreProject = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,

							  int dayOfMonth) {
			int yearDefault = year;
			int monthOfyearDefault = monthOfYear+1;
//    	   if (monthOfYear==0){
//    		   yearDefault = year-1;
//    		   monthOfyearDefault = 12;
//    		   
//    	   }
			preDate = String.valueOf(yearDefault)+"-"+String.valueOf(monthOfyearDefault)+"-"+String.valueOf(dayOfMonth);
			((TextView)findViewById(R.id.StartDateEditText)).setText(preDate);
		}

	};

	private DatePickerDialog.OnDateSetListener mDateSetListenerEndProject = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,

							  int dayOfMonth) {

			endDate = String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth);
			((TextView)findViewById(R.id.EndDateEditText)).setText(endDate);
		}

	};
	@OnClick(R.id.StartDateEditText)
	protected void StartDateEditTextDidClick(){
		Time t=new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。  
		int preYear = t.year;
		int preMonth = t.month;
		int preDay = t.monthDay;
		new DatePickerDialog(this, mDateSetListenerPreProject, preYear, preMonth, preDay).show();

	}

	@OnClick(R.id.EndDateEditText)
	protected void EndDateEditTextDidClick(){
		Time t=new Time("GMT+8"); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。  
		int endYear = t.year;
		int endMonth = t.month;
		int endDay = t.monthDay;
		new DatePickerDialog(this, mDateSetListenerEndProject, endYear, endMonth, endDay).show();
	}

	public void menuButtonDidClick(){
		menu.toggle();
	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			exitBy2Click();
//			return false;
//		} else {
//			return super.dispatchKeyEvent(event);
//		}
//	}
	private static Boolean isExit = false;
	private void exitBy2Click() {
		// TODO Auto-generated method stub
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.bt_qveding:
				preDate = ((TextView)findViewById(R.id.Report_CreateDateEditTextPre)).getText().toString();
				endDate = ((TextView)findViewById(R.id.Report_CreateDateEditTextEnd)).getText().toString();
				Intent intent = new Intent(UnqualifiedSearchActivity.this, TestReportActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("ProjectName", ((EditText)findViewById(R.id.ProjectNameEditText)).getText().toString());
				bundle.putString("ProjectArea", ProjectArea);
				bundle.putString("Report_CreateDate",preDate+","+endDate);
				bundle.putString("EntrustUnitName", ((EditText)findViewById(R.id.EntrustUnitNameEditText)).getText().toString());
				bundle.putString("BuildingReportNumber", ((EditText)findViewById(R.id.BuildingReportNumberEditText)).getText().toString());
				bundle.putString("ItemName", ((EditText)findViewById(R.id.ItemNameEditText)).getText().toString());
				bundle.putString("EntrustType", EntrustType);
				bundle.putString("ManageUnitID", ManageUnitID);
				bundle.putString("UqExecStatus", UqExecStatus);
				bundle.putString("Type",Type);
				bundle.putString("ItemID",JianceKey);
				//debug
//        bundle = new Bundle();
//        bundle.putString("ProjectName", "");
//        bundle.putString("ProjectArea", "");
//        bundle.putString("ItemName", "");
//        bundle.putString("Report_CreateDate", "[\"2014-1-1\",\"2014-12-31\"]");
//        bundle.putString("EntrustUnitName", "");
//        bundle.putString("BuildingReportNumber", "22");
//        bundle.putString("EntrustType", "-1");
//        bundle.putString("ManageUnitID", "");
//        bundle.putString("UqExecStatus", "-1");
//        bundle.putString("Type",Type);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.bt_qveding1:
				Intent intent1 = new Intent(UnqualifiedSearchActivity.this, ConstructionListActivity.class);
				Bundle bundle1 = new Bundle();
				//bundle1.putString("MemberId", ((EditText)findViewById(R.id.MemberIdEditText)).getText().toString());
				String startDate = ((TextView)findViewById(R.id.StartDateEditText)).getText().toString();
				String endDate = ((TextView)findViewById(R.id.EndDateEditText)).getText().toString();
				bundle1.putString("StartDate",startDate);
				bundle1.putString("EndDate",endDate);
				if (QueryKey.isEmpty()){
					LSAlert.showAlert(UnqualifiedSearchActivity.this, "请选择查询项目名称!");
					return;
				}
				bundle1.putString("QueryKey",QueryKey);
				bundle1.putString("KeyName",((TextView)findViewById(R.id.CheckItemTextView)).getText().toString());
				bundle1.putString("QueryPower",((TextView)findViewById(R.id.QueryPowerEditText)).getText().toString());
				bundle1.putString("Type",Type);
				intent1.putExtras(bundle1);
				startActivity(intent1);
				break;
		}
	}
}
