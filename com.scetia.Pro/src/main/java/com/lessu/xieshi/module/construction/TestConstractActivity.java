package com.lessu.xieshi.module.construction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.google.gson.EasyGson;
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

public class TestConstractActivity extends NavigationActivity implements OnItemClickListener {
	ListPageWrapper wrapper;
	String type="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_constract_activity);

		this.setTitle("检测合同");
		navigationBar.setBackgroundColor(0xFF3598DC);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.listView);

		
		wrapper = new ListPageWrapper<View>(TestConstractActivity.this) {

			@Override
			protected ApiMethodDescription onPageGetApiMethodDescription() {
				// TODO Auto-generated method stub
				return ApiMethodDescription.get("/ServiceSM.asmx/ConsignList");
			}

			@Override
			protected void onPageToInit(final PageController pageController) {
				// TODO Auto-generated method stub
				String token = LSUtil.valueStatic("Token");
				HashMap<String, Object> params = new HashMap<String, Object>();
				Bundle bundelForData=TestConstractActivity.this.getIntent().getExtras(); 
				String projectId = bundelForData.getString("ProjectId");
				
				params.put("Token", token);
				params.put("ProjectId", projectId);
				pageController.setApiParams(params);
				pageController.pageName = "CurrentPageNo";
	            pageController.stepName = "PageSize";
	            pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){

					@Override
					public PageInfo adapter(JsonElement input) {
						// TODO Auto-generated method stub
						PageInfo pageInfo = new PageInfo();
						pageInfo.isSuccess = true;
						JsonObject inputJson = input.getAsJsonObject().get("Data").getAsJsonObject();
						type = inputJson.get("Type").getAsString();
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
				LinearLayout listCell = (LinearLayout) View.inflate(TestConstractActivity.this, R.layout.test_constract_item, null);
				return listCell;
			}

			@Override
			protected void onPageCellSetData(int position, View cell,
					Object data) {
				// TODO Auto-generated method stub
		    	EasyUI.setTextViewText(cell.findViewById(R.id.contractStateTextView), (JsonObject)data, "ContractState", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.buildingReportNumberTextView), (JsonObject)data, "BuildingReportNumber", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.contractSignNumberTextView), (JsonObject)data, "ContractSignNumber", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.detectionUnitNameTextView), (JsonObject)data, "DetectionUnitName", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.buildUnitNameTextView), (JsonObject)data, "BuildUnitName", "暂无");
		    	
			}
		};
		
		wrapper.wrap(listView);
			
		listView.setOnItemClickListener(this);
		
       // ButterKnife.bind(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
		Intent intent = new Intent (TestConstractActivity.this, SampleListActivity.class) ;
		Bundle bundle = new Bundle();  
		List list = wrapper.getPageController().getList();
		String jsonString = list.get(position-1).toString();
		String ContractSignNumber = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("ContractSignNumber").getAsString();
		bundle.putString("ContractSignNumber", ContractSignNumber);
		intent.putExtras(bundle);
		startActivity(intent);  
	}
	

}
