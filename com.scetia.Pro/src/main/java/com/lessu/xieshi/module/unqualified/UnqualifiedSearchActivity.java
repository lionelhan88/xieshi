package com.lessu.xieshi.module.unqualified;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.DateUtil;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class UnqualifiedSearchActivity extends XieShiSlidingMenuActivity {
	String EntrustType = "-1";
	String ManageUnitID = "";
	String UqExecStatus = "-1";
	String ProjectArea = "";
	String QueryKey = "";
	String Type = "0";
	ArrayList<String> checkNameList = new ArrayList<>();
	ArrayList<String> checkValueList = new ArrayList<>();
	ArrayList<String> itemKindName = new ArrayList<>();
	ArrayList<String> itemKindId = new ArrayList<>();
	ArrayList<String> itemItemName = new ArrayList<>();
	ArrayList<String> itemItemId = new ArrayList<>();
	private String JianceKey="";

	@BindView(R.id.Report_CreateDateEditTextPre)
	 TextView tvReportCreateStartDate;
	@BindView(R.id.Report_CreateDateEditTextEnd)
	 TextView tvReportCreateEndDate;
	@BindView(R.id.ProjectAreaTextView)
	TextView tvProjectAreaTextView;
	@BindView(R.id.ProjectNameEditText)
	EditText etProjectName;
	@BindView(R.id.StartDateEditText)
	TextView tvProjectStartDate;
	@BindView(R.id.EndDateEditText)
	TextView tvProjectEndDate;
	@BindView(R.id.tv_jianceleibie)
	TextView tvTestingType;
	@BindView(R.id.tv_jiancexiangmu)
	TextView tvTestingProject;
	@BindView(R.id.bt_material_ok)
	Button btMaterialOk;
	@BindView(R.id.bt_project_ok)
	Button btProjectOk;
	@BindView(R.id.EntrustTypeTextView)
	TextView tvEntrustTypeTextView;
	@BindView(R.id.ManageUnitIDTextView)
	TextView tvManageUnitIDTextView;
	@BindView(R.id.UqExecStatusTextView)
	TextView tvUqExecStatusTextView;

	@Override
	protected int getLayoutId() {
		return R.layout.unqualified_search_activity;
	}

	/**
	 * 初始化控件
	 */
	@Override
	protected void initView(){
		this.setTitle("不合格信息查询");
		BarButtonItem	homeItem = new BarButtonItem(this ,R.drawable.icon_navigation_menu);
		homeItem.setOnClickMethod(this,"menuButtonDidClick");
		Date today = new Date();
		String todayString = DateUtil.FORMAT_BAR_YMD(today);
		//初始化设置当天日期为截至日期
		tvProjectEndDate.setText(todayString);
		tvReportCreateEndDate.setText(todayString);
		//工程检测起始日期默认为前一个月
		String preTodayString = DateUtil.getMonthAgo(-1);
		tvProjectStartDate.setText(preTodayString);
		//材料检测起始日期默认为前一天
		String reportCreateDate = DateUtil.getDayAgo(-1);
		tvReportCreateStartDate.setText(reportCreateDate);
		Intent intent = getIntent();
		if(intent!=null){
			String projectName = intent.getStringExtra("projectName");
			String projectArea = intent.getStringExtra("projectArea");
			etProjectName.setText(projectName);
			tvProjectAreaTextView.setText(projectArea);
		}
	}

	/**
	 * 初始化数据
	 */
	@Override
	protected void initData(){
		HashMap<String, Object> params = new HashMap<>();
		params.put(Constants.User.XS_TOKEN,  Constants.User.GET_TOKEN());
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
	}

	@OnClick(R.id.Report_CreateDateEditTextPre)
	protected void Report_CreateDateEditTextPreDidClick(){
		DateUtil.datePicker(this, new OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date, View v) {
				tvReportCreateStartDate.setText(DateUtil.FORMAT_BAR_YMD(date));
			}
		});
	}

	@OnClick(R.id.Report_CreateDateEditTextEnd)
	protected void Report_CreateDateEditTextEndDidClick(){
		DateUtil.datePicker(this, new OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date, View v) {
				tvReportCreateEndDate.setText(DateUtil.FORMAT_BAR_YMD(date));
			}
		});
	}

	@OnClick(R.id.ProjectAreaButton)
	protected void ProjectAreaButtonDidClick(){
		 String[] itemString = {"金山区","崇明县","浦东新区","南汇区","闵行区","卢湾区","徐汇区","奉贤区","宝山区","杨浦区","普陀区","黄浦区","青浦区","松江区","外省市","闸北区","长宁区","嘉定区","静安区","虹口区"};
		OptionsPickerView<String> build = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
			@Override
			public void onOptionsSelect(int options1, int options2, int options3, View v) {
				tvProjectAreaTextView.setText(itemString[options1]);
				ProjectArea  = itemString[options1];
			}
		}).build();
		build.setPicker(Arrays.asList(itemString));
		build.show();
	}

	@OnClick(R.id.EntrustTypeButton)
	protected void typeButtonDidClick(){
		final String[] itemString = {"全部","常规检测","监理平行检测"};
		LSAlert.showDialogSingleChoice(this, "检测性质", R.drawable.icon_choice_dialog, itemString, "取消",
				position -> {
					tvEntrustTypeTextView.setText(itemString[position]);
					EntrustType = String.valueOf(position-1);
				});

	}

	@OnClick(R.id.ManageUnitIDButton)
	protected void ManageUnitIDButton(){
		final String[] itemString = {"资源服务"};
		LSAlert.showDialogSingleChoice(this,"受监站Id",R.drawable.icon_choice_dialog,itemString,"取消",
				position -> {
					tvManageUnitIDTextView.setText(itemString[position]);
				});
	}

	@OnClick(R.id.UqExecStatusButton)
	protected void UqExecStatusButton(){
		final String[] itemString = {"全部","未处理","已处理","处理中"};
		LSAlert.showDialogSingleChoice(this,"处理状态",R.drawable.icon_choice_dialog,itemString,"取消",
				position -> {
					tvUqExecStatusTextView.setText(itemString[position]);
					UqExecStatus = String.valueOf(position-1);
				});
	}

	@OnClick(R.id.bt_jianceleibie)
	protected void testingTypeDidClick(){
		final String[] nameString = itemKindName.toArray(new String[itemKindName.size()]);
		final String[] idString = itemKindId.toArray(new String[itemKindId.size()]);
		LSAlert.showDialogSingleChoice(this,"检测类别",R.drawable.icon_choice_dialog,nameString,"取消"
				, position -> {
					tvTestingType.setText(nameString[position]);
					HashMap<String, Object> params = new HashMap<>();
					params.put("Token",  Constants.User.GET_TOKEN());
					params.put("Condition", idString[position]);
					System.out.println(params);
					EasyAPI.apiConnectionAsync(UnqualifiedSearchActivity.this, true, false, ApiMethodDescription.get("/ServiceSource.asmx/ItemItemSource"), params, new EasyAPI.ApiFastSuccessCallBack() {
						@Override
						public void onSuccessJson(JsonElement result) {
							tvTestingProject.setText("");
							JianceKey="";
							itemItemName.clear();
							itemItemId.clear();
							JsonArray jsonArray = result.getAsJsonObject().get("Data").getAsJsonArray();
							for (int i = 0; i < jsonArray.size(); i++) {
								itemItemName.add(jsonArray.get(i).getAsJsonObject().get("Name").getAsString());
								itemItemId.add(jsonArray.get(i).getAsJsonObject().get("Id").getAsString());
							}
						}
					});
				});
	}

	@OnClick(R.id.bt_jiancexiangmu)
	protected void testingProjectDidClick(){
		if(itemItemName.size()<=0){
			LSAlert.showAlert(UnqualifiedSearchActivity.this, "请先选择类别！");
			return;
		}
		final String[] itemNameString = itemItemName.toArray(new String[itemItemName.size()]);
		final String[] itemIdString = itemItemId.toArray(new String[itemItemId.size()]);
		LSAlert.showDialogSingleChoice(this, "检测项目", R.drawable.icon_choice_dialog, itemNameString, "取消",
				position -> {
					tvTestingProject.setText(itemNameString[position]);
					JianceKey = itemIdString[position];
				});
	}

	@OnClick(R.id.CheckItemButton)
	protected void CheckItemButtonDidClick(){
		final String[] itemString = checkNameList.toArray(new String[checkNameList.size()]);
		final String[] valueString = checkValueList.toArray(new String[checkValueList.size()]);
		LSAlert.showDialogSingleChoice(this,"检测项目",R.drawable.icon_choice_dialog,itemString,"取消",
				position -> {
					TextView tv = findViewById(R.id.CheckItemTextView);
					tv.setText(itemString[position]);
					QueryKey = valueString[position];
				});
	}

	@OnClick(R.id.ll_cailiao)
	protected void materialButtonDidClick(){
		Type = "0";
		findViewById(R.id.materialView).setVisibility(View.VISIBLE);
		findViewById(R.id.ll_cailiao).setBackgroundResource(R.drawable.anniu);
		TextView tv_cailiao = findViewById(R.id.tv_cailiao);
		tv_cailiao.setTextColor(this.getResources().getColor(R.color.white));
		findViewById(R.id.iv_cailiao).setBackgroundResource(R.drawable.cailiao);
		TextView tv_gongcheng = findViewById(R.id.tv_gongcheng);
		tv_gongcheng.setTextColor(this.getResources().getColor(R.color.blue_normal2));
		findViewById(R.id.iv_gongcheng).setBackgroundResource(R.drawable.gongcheng2);
		findViewById(R.id.projectView).setVisibility(View.GONE);
		findViewById(R.id.ll_gongchen).setBackgroundColor(Color.TRANSPARENT);
	}

	@OnClick(R.id.ll_gongchen)
	protected void projectButtonDidClick(){
		Type = "1";
		findViewById(R.id.projectView).setVisibility(View.VISIBLE);
		findViewById(R.id.ll_gongchen).setBackgroundResource(R.drawable.anniu);
		TextView tv_cailiao = findViewById(R.id.tv_cailiao);
		tv_cailiao.setTextColor(this.getResources().getColor(R.color.blue_normal2));
		findViewById(R.id.iv_cailiao).setBackgroundResource(R.drawable.cailiao1);
		TextView tv_gongcheng = findViewById(R.id.tv_gongcheng);
		tv_gongcheng.setTextColor(this.getResources().getColor(R.color.white));
		findViewById(R.id.iv_gongcheng).setBackgroundResource(R.drawable.gongcheng1);
		findViewById(R.id.materialView).setVisibility(View.GONE);
		findViewById(R.id.ll_cailiao).setBackgroundColor(Color.TRANSPARENT);
	}

	@OnClick(R.id.StartDateEditText)
	protected void StartDateEditTextDidClick(){
		DateUtil.datePicker(this, new OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date, View v) {
				tvProjectStartDate.setText(DateUtil.FORMAT_BAR_YMD(date));
			}
		});
	}

	@OnClick(R.id.EndDateEditText)
	protected void EndDateEditTextDidClick(){
		DateUtil.datePicker(this, new OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date, View v) {
				tvProjectEndDate.setText(DateUtil.FORMAT_BAR_YMD(date));
			}
		});
	}

	public void menuButtonDidClick(){
		menu.toggle();
	}

	@OnClick({R.id.bt_material_ok,R.id.bt_project_ok})
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.bt_material_ok:
				String preDate = tvReportCreateStartDate.getText().toString();
				String reportEndDate = tvReportCreateEndDate.getText().toString();
				try {
					if(DateUtil.sdfDate.parse(preDate).after(DateUtil.sdfDate.parse(reportEndDate))){
						LSAlert.showAlert(this,"起始日期不能大于截止日期");
						return;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(UnqualifiedSearchActivity.this, TestingReportActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("ProjectName", etProjectName.getText().toString());
				bundle.putString("ProjectArea", ProjectArea);
				bundle.putString("Report_CreateDate",preDate+","+reportEndDate);
				bundle.putString("EntrustUnitName", ((EditText)findViewById(R.id.EntrustUnitNameEditText)).getText().toString());
				bundle.putString("BuildingReportNumber", ((EditText)findViewById(R.id.BuildingReportNumberEditText)).getText().toString());
				bundle.putString("ItemName", ((EditText)findViewById(R.id.ItemNameEditText)).getText().toString());
				bundle.putString("EntrustType", EntrustType);
				bundle.putString("ManageUnitID", ManageUnitID);
				bundle.putString("UqExecStatus", UqExecStatus);
				bundle.putString("Type",Type);
				bundle.putString("ItemID",JianceKey);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.bt_project_ok:
				if (QueryKey.isEmpty()){
					LSAlert.showAlert(UnqualifiedSearchActivity.this, "请选择查询项目名称!");
					return;
				}
				String startDate = ((TextView)findViewById(R.id.StartDateEditText)).getText().toString();
				String endDate = ((TextView)findViewById(R.id.EndDateEditText)).getText().toString();
				try {
					if(DateUtil.sdfDate.parse(startDate).after(DateUtil.sdfDate.parse(endDate))){
						LSAlert.showAlert(this,"起始日期不能大于截止日期");
						return;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Intent intent1 = new Intent(UnqualifiedSearchActivity.this, UnqualifiedConstructionListActivity.class);
				Bundle bundle1 = new Bundle();

				bundle1.putString("StartDate",startDate);
				bundle1.putString("EndDate",endDate);
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
