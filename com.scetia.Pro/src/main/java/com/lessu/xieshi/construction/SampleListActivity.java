package com.lessu.xieshi.construction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.google.gson.EasyGson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.page.PageController;
import com.lessu.net.page.PageInfoAdapterInterface;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.xieshi.QRCodeActivity;
import com.lessu.xieshi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SampleListActivity extends NavigationActivity implements OnItemClickListener {
	ListPageWrapper wrapper;
	String type;
	String NotFinishedOnly ;
	String QueryStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_list_activity);
		navigationBar.setBackgroundColor(0xFF3598DC);

		BarButtonItem	searchButtonitem = new BarButtonItem(this , R.drawable.icon_navigation_search );
		searchButtonitem.setOnClickMethod(this,"searchButtonDidClick");

		navigationBar.setRightBarItem(searchButtonitem);
		this.setTitle("样品信息查询");

		ButterKnife.inject(this);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.listView);
		wrapper = new ListPageWrapper<View>(SampleListActivity.this) {

			@Override
			protected ApiMethodDescription onPageGetApiMethodDescription() {
				// TODO Auto-generated method stub
				return ApiMethodDescription.get("/ServiceSM.asmx/SampleList");
			}

			@Override
			protected void onPageToInit(final PageController pageController) {
				// TODO Auto-generated method stub
				String token = LSUtil.valueStatic("Token");
				Bundle bundle = SampleListActivity.this.getIntent().getExtras();
				String ContractSignNumber = bundle.getString("ContractSignNumber");

				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("Token", token);
				params.put("ContractSignNumber", ContractSignNumber);
				params.put("NotCancelOnly", NotFinishedOnly);
				params.put("QueryStr", QueryStr);
				pageController.setApiParams(params);
				pageController.pageName = "CurrentPageNo";
				pageController.stepName = "PageSize";
				pageController.step = 10;
				pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){

					@Override
					public PageInfo adapter(JsonElement input) {
						// TODO Auto-generated method stub
						System.out.println(input);
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
				RelativeLayout listCell = (RelativeLayout) View.inflate(SampleListActivity.this, R.layout.sample_list_item, null);
				return listCell;
			}

			@Override
			protected void onPageCellSetData(int position, View cell,
											 Object data) {
				// TODO Auto-generated method stub
				EasyUI.setTextViewText(cell.findViewById(R.id.SampleNameTextView), (JsonObject)data, "SampleName", "暂无");
//		    	EasyUI.setTextViewText(cell.findViewById(R.id.IdTextView), (JsonObject)data, "Id", "暂无");
//		    	((TextView)cell.findViewById(R.id.TypeTextView)).setText(type);
				//EasyUI.setTextViewText(cell.findViewById(R.id.CoreCodeNoTextView), (JsonObject)data, "CoreCodeNo", "暂无");
//		    	EasyUI.setTextViewText(cell.findViewById(R.id.ReportNumberTextView), (JsonObject)data, "ReportNumber", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.Sample_StatusTextView), (JsonObject)data, "Sample_Status", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.Exam_ResultTextView), (JsonObject)data, "Exam_Result", "暂无");
				//EasyUI.setTextViewText(cell.findViewById(R.id.MemberCodeTextView), (JsonObject)data, "MemberCode", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.CreateDateTimeTextView), (JsonObject)data, "CreateDateTime", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.daibiaoshuliang), (JsonObject)data, "Delegate_Quan", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.guige), (JsonObject)data, "SpecName", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.dengjiqiangdu), (JsonObject)data, "GradeName", "暂无");
				EasyUI.setTextViewText(cell.findViewById(R.id.gongchengbuwei), (JsonObject)data, "ProJect_Part", "暂无");
			}
		};

		wrapper.wrap(listView);

		listView.setOnItemClickListener(this);

		ButterKnife.inject(this);
	}
	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
		Intent intent = new Intent (SampleListActivity.this, SampleDetailActivity.class) ;
		Bundle bundle = new Bundle();
		List list = wrapper.getPageController().getList();
		String jsonString = list.get(position-1).toString();
		String Id = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("Id").getAsString();
		String MemberCode = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("MemberCode").getAsString();
		bundle.putString("Id", Id);
		bundle.putString("MemberCode", MemberCode);
		intent.putExtras(bundle);
		startActivity(intent);
	}



	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2 != null){
			Bundle bundle = arg2.getExtras();
			NotFinishedOnly =  bundle.get("NotFinishedOnly").toString();
			QueryStr = bundle.get("QueryStr").toString();
		}
	}

	@OnClick(R.id.qrcodeScanButton)
	protected void qrcodeScanButton(){
		Intent intent = new Intent(SampleListActivity.this, QRCodeActivity.class);
		startActivity(intent);
	}

	public void searchButtonDidClick(){
		Intent intent = new Intent(SampleListActivity.this, SampleSearchActivity.class);
		startActivityForResult(intent, 3);
	}
}
