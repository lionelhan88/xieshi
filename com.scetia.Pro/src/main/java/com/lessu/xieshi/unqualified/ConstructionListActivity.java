package com.lessu.xieshi.unqualified;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.foundation.LSUtil;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.page.PageController;
import com.lessu.net.page.PageInfoAdapterInterface;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.xieshi.R;
import com.lessu.xieshi.XieShiSlidingMenuActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

public class ConstructionListActivity extends XieShiSlidingMenuActivity implements OnItemClickListener {
	ListPageWrapper wrapper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.construction_list_activity);
		this.setTitle("工程列表");
		navigationBar.setBackgroundColor(0xFF3598DC);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//跳转到搜索页面
		PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.listView);
		wrapper = new ListPageWrapper<View>(ConstructionListActivity.this) {

			@Override
			protected ApiMethodDescription onPageGetApiMethodDescription() {
				// TODO Auto-generated method stub
				return ApiMethodDescription.get("/ServiceUQ.asmx/ReportList");
			}

			@Override
			protected void onPageToInit(final PageController pageController) {
				String token = LSUtil.valueStatic("Token");

				Bundle bundle = getIntent().getExtras();
				String MemberId = bundle.getString("MemberId");
				String StartDate = bundle.getString("StartDate");
				String EndDate = bundle.getString("EndDate");
				String QueryKey = bundle.getString("QueryKey");
				String KeyName = bundle.getString("KeyName");
				String QueryPower = bundle.getString("QueryPower");
				String Type = bundle.getString("Type");
				
				HashMap<String, Object> params = new HashMap<String, Object>();
	            params.put("Token", token);
	            params.put("MemberId", MemberId);
	            params.put("StartDate", StartDate);
	            params.put("EndDate", EndDate);
	            params.put("QueryKey", QueryKey);
	            params.put("KeyName", KeyName);
	            params.put("QueryPower", QueryPower);
	            params.put("Type", Type);
	            
	            pageController.setApiParams(params);
	            pageController.pageName = "CurrentPageNo";
	            pageController.stepName = "PageSize";
	            pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){

					@Override
					public PageInfo adapter(JsonElement input) {
						PageInfo pageInfo = new PageInfo();
						pageInfo.isSuccess = true;
						JsonObject inputJson = input.getAsJsonObject().get("Data").getAsJsonObject();
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
				LinearLayout listCell = (LinearLayout) View.inflate(ConstructionListActivity.this, R.layout.un_construction_list_item, null);
				return listCell;
			}

			@Override
			protected void onPageCellSetData(int position, View cell,
					Object data) {
				// TODO Auto-generated method stub
		    	EasyUI.setTextViewText(cell.findViewById(R.id.projectNameTextView), (JsonObject)data, "ProjectName", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.MemberIdTextView), (JsonObject)data, "MemberId", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.ReportIDTextView), (JsonObject)data, "ReportId", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.LinkmanTextView), (JsonObject)data, "Linkman", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.TelTextView), (JsonObject)data, "Tel", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.ProjectNameTextView), (JsonObject)data, "ProjectName", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.BuildUnitTextView), (JsonObject)data, "BuildUnit ", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.ConstructionUnitTextView), (JsonObject)data, "ConstructionUnit", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.SupervisorUnitTextView), (JsonObject)data, "SupervisorUnit", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.SuperviserKeyTextView), (JsonObject)data, "SuperviserKey", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.ModifyTimeTextView), (JsonObject)data, "ModifyTime", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.IsSuperviserTextView), (JsonObject)data, "IsSuperviser", "暂无");
			}
		};
		
		wrapper.wrap(listView);
			
		listView.setOnItemClickListener(this);
		
        ButterKnife.inject(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
//		Intent intent = new Intent(ConstructionListActivity.this, TestConstractActivity.class);
//        List list = wrapper.getPageController().getList();
//		String jsonString = list.get(position-1).toString();
//		String projectId = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("ProjectId").getAsString();
//		Bundle bundle = new Bundle();
//		bundle.putString("ProjectId", projectId);
//		intent.putExtras(bundle);
//		startActivity(intent);
	}
	
	
	public void menuButtonDidClick(){
		menu.toggle();
    }
	
	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//	    	return false;
//	    } else {
//	        return super.dispatchKeyEvent(event);
//	    }
//	}
}
