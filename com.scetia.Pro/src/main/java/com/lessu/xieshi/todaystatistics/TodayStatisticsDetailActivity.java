package com.lessu.xieshi.todaystatistics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.net.page.PageController;
import com.lessu.net.page.PageInfoAdapterInterface;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TodayStatisticsDetailActivity extends NavigationActivity implements OnItemClickListener {
	JsonArray list = new JsonArray();
	String projectName = "";
	String projectArea = "";
	String baogaobianhao="";
	String jianshedanwei="";
	String shigongdanwei="";
	String jianlidanwei="";
	String jiancedanwei="";
	private Bundle bundle;
	private int rangeindex;
	private String currentLocation;
	private ListPageWrapper<View> wrapper;
	private PullToRefreshListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.construction_detail_activity);
		listView = (PullToRefreshListView) findViewById(R.id.listView);
		//listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		Intent intent = getIntent();
		//type为1为查询工程检测，为0则为查询材料检测
		if(intent.getIntExtra("buttonId",0)==1){
			this.setTitle("工程检测");
		}else{
			this.setTitle("材料检测");
		}
		navigationBar.setBackgroundColor(0xFF3598DC);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		bundle = getIntent().getExtras();
		if(bundle.getBoolean("isxianshall")){
			connectall();
		}else{
			listView.setAdapter(adapter);
			connectlist();
		}
	}

	private void connectall() {
//		String token = LSUtil.valueStatic("Token");
//		String type = "1";
//		//HashMap<String, Object> params = new HashMap<String, Object>();
//		projectArea = bundle.getString("ProjectArea");
//		projectName = bundle.getString("ProjectName");
//		baogaobianhao = bundle.getString("baogaobianhao");
//		jianshedanwei = bundle.getString("jianshedanwei");
//		shigongdanwei = bundle.getString("shigongdanwei");
//		jianlidanwei = bundle.getString("jianlidanwei");
//		jiancedanwei = bundle.getString("jiancedanwei");
//		rangeindex = bundle.getInt("DistanceRange");
//
//		System.out.println("最后。。。。。。。。"+rangeindex);
//		currentLocation = bundle.getString("CurrentLocation");

//		params.put("Token", token);
//		params.put("Type", 1);
//		params.put("ProjectName", projectName);
//		params.put("ProjectArea", projectArea);
//		params.put("BuildingReportNumber", baogaobianhao);
//		params.put("BuildUnitName", shigongdanwei);
//		params.put("ConstructUnitName", jianshedanwei);
//		params.put("SuperviseUnitName", jianlidanwei);
//		params.put("DetectionUnitName", jiancedanwei);
//		params.put("CurrentLocation",currentLocation);
//		System.out.println("getmyself......"+currentLocation);
//		params.put("DistanceRange",rangeindex;
//		params.put("PageSize", 8);
//		params.put("CurrentPageNo", 1);
//		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceTS.asmx/ManageUnitTodayStatisProjectDetectInfo"), params, new EasyAPI.ApiFastSuccessCallBack() {
//			@Override
//			public void onSuccessJson(JsonElement result) {
//				JsonObject jsonObject = result.getAsJsonObject().get("Data").getAsJsonObject();
//				System.out.println(jsonObject);
//				list = jsonObject.get("ListContent").getAsJsonArray();
//				if (list==null||list.isJsonNull()||list.size()==0){
//					LSAlert.showAlert(TodayStatisticsDetailActivity.this, "无相关记录");
//					return;
//				}
//				adapter.notifyDataSetChanged();
//			}
//		});
		wrapper = new ListPageWrapper<View>(TodayStatisticsDetailActivity.this) {
			@Override
			protected ApiMethodDescription onPageGetApiMethodDescription() {
				// TODO Auto-generated method stub
				return ApiMethodDescription.get("/ServiceTS.asmx/ManageUnitTodayStatisProjectDetectInfo");
			}
			@Override
			protected void onPageToInit(final PageController pageController) {
				// TODO Auto-generated method stub
				//这里要改1
				String token = LSUtil.valueStatic("Token");
				HashMap<String, Object> params = new HashMap<String, Object>();
				projectArea = bundle.getString("ProjectArea");
				projectName = bundle.getString("ProjectName");
				baogaobianhao = bundle.getString("baogaobianhao");
				jianshedanwei = bundle.getString("jianshedanwei");
				shigongdanwei = bundle.getString("shigongdanwei");
				jianlidanwei = bundle.getString("jianlidanwei");
				jiancedanwei = bundle.getString("jiancedanwei");
				rangeindex = bundle.getInt("DistanceRange");
				currentLocation = bundle.getString("CurrentLocation");

				params.put("Token", token);
				params.put("Type", 1);
				params.put("ProjectName", projectName);
				params.put("ProjectArea", projectArea);
				params.put("BuildingReportNumber", baogaobianhao);
				params.put("BuildUnitName", shigongdanwei);
				params.put("ConstructUnitName", jianshedanwei);
				params.put("SuperviseUnitName", jianlidanwei);
				params.put("DetectionUnitName", jiancedanwei);
				//2019-05-27　修改，勾选“显示所有工地统计信息”时，如果有工地名称，不能有距离参数和坐标参数
				if(projectName.length()==0||projectName.equals("")){
					params.put("CurrentLocation",currentLocation);
					System.out.println("getmyself......"+currentLocation);
					params.put("DistanceRange",rangeindex);
				}
				System.out.println(params);
				pageController.setApiParams(params);
				pageController.pageName = "CurrentPageNo";
				pageController.stepName = "PageSize";
				pageController.step = 9;
				pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){
					@Override
					public PageInfo adapter(JsonElement input) {
						// TODO Auto-generated method stub
						PageInfo pageInfo = new PageInfo();
						pageInfo.isSuccess = true;
						System.out.println(input);
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
				LinearLayout listCell = (LinearLayout) View.inflate(TodayStatisticsDetailActivity.this, R.layout.construction_detail_item, null);
				return listCell;
			}
			@Override
			protected void onPageCellSetData(int position, View cell,
											 Object data) {
				// TODO Auto-generated method stub
				EasyUI.setTextViewText(cell.findViewById(R.id.ItemNameTextView), (JsonObject)data, "ItemName", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.QualifiedRateTextView),(JsonObject)data, "QualifiedRate", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.DetectedSampleCountTextView), (JsonObject)data, "DetectedSampleCount", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.UnqualifiedSampleCountTextView), (JsonObject)data, "UnqualifiedSampleCount", "暂无");
			}
		};
		wrapper.wrap(listView);
	}

	private void connectlist() {
		String token = LSUtil.valueStatic("Token");
		HashMap<String, Object> params = new HashMap<String, Object>();
		bundle = getIntent().getExtras();
		String projectId = bundle.getString("ProjectId");
		String type = "1";
		params.put("Token", token);
		params.put("Type", type);
		params.put("ProjectId", projectId);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceTS.asmx/ManageUnitTodayStatisInfoList"), params, new EasyAPI.ApiFastSuccessCallBack() {
					@Override
					public void onSuccessJson(JsonElement result) {
						JsonObject jsonObject = result.getAsJsonObject().get("Data").getAsJsonObject();
						System.out.println(jsonObject);
						list = jsonObject.get("ListContent").getAsJsonArray();
						if (list==null||list.isJsonNull()||list.size()==0){
							LSAlert.showAlert(TodayStatisticsDetailActivity.this, "无相关记录");
							return;
						}
						adapter.notifyDataSetChanged();
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
	}
	
	protected BaseAdapter adapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return list.size();
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parentViewGroup) {
			if (view == null){
				view = View.inflate(TodayStatisticsDetailActivity.this, R.layout.construction_detail_item, null);
			}
			EasyUI.setTextViewText(view.findViewById(R.id.ItemNameTextView), (JsonObject)list.get(position), "ItemName", "暂无");
			EasyUI.setTextViewText(view.findViewById(R.id.QualifiedRateTextView), (JsonObject)list.get(position), "QualifiedRate", "暂无");
			EasyUI.setTextViewText(view.findViewById(R.id.DetectedSampleCountTextView), (JsonObject)list.get(position), "DetectedSampleCount", "暂无");
			EasyUI.setTextViewText(view.findViewById(R.id.UnqualifiedSampleCountTextView), (JsonObject)list.get(position), "UnqualifiedSampleCount", "暂无");
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
