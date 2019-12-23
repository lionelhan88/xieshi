package com.lessu.xieshi.unqualified;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.google.gson.EasyGson;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.page.PageController;
import com.lessu.net.page.PageInfoAdapterInterface;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.xieshi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class TestReportActivity extends NavigationActivity implements OnItemClickListener {
	ListPageWrapper wrapper;
	String Type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.construction_list_activity);
		this.setTitle("检测报告");
		navigationBar.setBackgroundColor(0xFF3598DC);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.listView);
		wrapper = new ListPageWrapper<View>(TestReportActivity.this) {

			@Override
			protected ApiMethodDescription onPageGetApiMethodDescription() {
				// TODO Auto-generated method stub
				return ApiMethodDescription.get("/ServiceUQ.asmx/ReportList");
			}

			@Override
			protected void onPageToInit(final PageController pageController) {
				// TODO Auto-generated method stub
				String token = LSUtil.valueStatic("Token");
				Bundle bundle = TestReportActivity.this.getIntent().getExtras();
				String ProjectName = bundle.getString("ProjectName");
				String ProjectArea = bundle.getString("ProjectArea");
				String Report_CreateDate = bundle.getString("Report_CreateDate");
				String EntrustUnitName = bundle.getString("EntrustUnitName");
				String BuildingReportNumber = bundle.getString("BuildingReportNumber");
				String EntrustType = bundle.getString("EntrustType");
				String ManageUnitID = bundle.getString("ManageUnitID");
				String UqExecStatus = bundle.getString("UqExecStatus");
				String ItemName = bundle.getString("ItemName");
				String type = bundle.getString("Type");
				String ItemID = bundle.getString("ItemID");

				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("Token", token);
				params.put("Type", type);
				params.put("ProjectName", ProjectName);
				params.put("ProjectArea", ProjectArea);
				params.put("EntrustUnitName", EntrustUnitName);
				params.put("BuildingReportNumber", BuildingReportNumber);
				params.put("ItemID",ItemID);
				params.put("Report_CreateDate",Report_CreateDate);
				params.put("ItemName", ItemName);
				params.put("EntrustType", EntrustType);
				params.put("ManageUnitID", ManageUnitID);
				params.put("UqExecStatus", UqExecStatus);
				System.out.println(params);
				pageController.setApiParams(params);
				String s = new Gson().toJson(params);
				System.out.println(s);
				pageController.pageName = "CurrentPageNo";
				pageController.stepName = "PageSize";
				pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){

					@Override
					public PageInfo adapter(JsonElement input) {
						// TODO Auto-generated method stub
						PageInfo pageInfo = new PageInfo();
						pageInfo.isSuccess = true;
						JsonObject inputJson = input.getAsJsonObject().get("Data").getAsJsonObject();
						Type = inputJson.get("Type").getAsString();
						JsonArray inputJsonArray = inputJson.get("ListContent").getAsJsonArray();
						int size = inputJsonArray.size();
						List<JsonElement> list = new ArrayList<JsonElement>();
						for (int i=0;i<size;i++){
							list.add(inputJsonArray.get(i));
						}

						pageInfo.listData = list;

						pageInfo.totalPage = pageController.getCurrentPage()+1;
						return pageInfo;
					}
				});

			}

			@Override
			protected View onPageCreateCell(int position) {
				// TODO Auto-generated method stub
				LinearLayout listCell = (LinearLayout) View.inflate(TestReportActivity.this, R.layout.test_report_item, null);
				return listCell;
			}

			@Override
			protected void onPageCellSetData(int position, View cell,
											 Object data) {
				// TODO Auto-generated method stub
				EasyUI.setTextViewText(cell.findViewById(R.id.Report_IDTextView), (JsonObject)data, "Report_ID", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.ProJectNameTextView), (JsonObject)data, "ProJectName", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.IdentifyingCodeTextView), (JsonObject)data, "IdentifyingCode", "暂无");
				String uqExecStatus = ((JsonObject)data).get("UqExecStatus").getAsString();
				if (uqExecStatus.equals("1")){
					((JsonObject)data).remove("UqExecStatus");
					((JsonObject)data).addProperty("UqExecStatus", "已处理");
				}
				else if (uqExecStatus.equals("0")){
					((JsonObject)data).remove("UqExecStatus");
					((JsonObject)data).addProperty("UqExecStatus", "未处理");
				}
				else if (uqExecStatus.equals("2")){
					((JsonObject)data).remove("UqExecStatus");
					((JsonObject)data).addProperty("UqExecStatus", "处理中");
				}
				EasyUI.setTextViewText(cell.findViewById(R.id.UqExecStatusTextView), (JsonObject)data, "UqExecStatus", "暂无");
				//Button bt_chuli = (Button) cell.findViewById(R.id.bt_chuli);
//				bt_chuli.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//
//
//					}
//				});

			}
		};

		wrapper.wrap(listView);

		listView.setOnItemClickListener(this);

		ButterKnife.inject(this);
	}
	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
		Intent intent = new Intent (TestReportActivity.this, ReportContentActivity.class) ;
		List list = wrapper.getPageController().getList();
		String jsonString = list.get(position-1).toString();
		String Report_id = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("Report_ID").getAsString();
		String Checksum = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("IdentifyingCode").getAsString();
		Bundle bundle = new Bundle();

		bundle.putString("Report_id", Report_id);
		bundle.putString("Checksum", Checksum);
		intent.putExtras(bundle);
		startActivity(intent);
	}


}
