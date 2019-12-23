package com.lessu.xieshi.dataexamine;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.EasyGson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.foundation.LSUtil;
import com.lessu.foundation.RegKit;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiBase;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.net.page.PageController;
import com.lessu.net.page.PageInfoAdapterInterface;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.HttpUrlConnect;
import com.lessu.xieshi.ListViewCell;
import com.lessu.xieshi.R;
import com.lessu.xieshi.XieShiSlidingMenuActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataExamineActivity extends XieShiSlidingMenuActivity implements OnItemClickListener {
	String itemId = "";
	ListPageWrapper wrapper;
	String token =  LSUtil.valueStatic("Token");
	String doneFlag = "";
	String kindId = "";
	String typeTitle = "";
	String projectTitle = "";
	String flagTitle = "";
	int auditedCounter = 0;
	int loseauditedCounter = 0;
	ArrayList<String> arrayList = new ArrayList<String>();
	private String uri;
	private List<JsonElement> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_examine_activity);
		this.setTitle("报告批准");
		navigationBar.setBackgroundColor(0xFF3598DC);


		BarButtonItem	searchButtonitem = new BarButtonItem(this , R.drawable.icon_navigation_search );
		searchButtonitem.setOnClickMethod(this,"searchButtonDidClick");

		navigationBar.setRightBarItem(searchButtonitem);

		BarButtonItem	menuButtonitem = new BarButtonItem(this ,R.drawable.icon_navigation_menu);
		menuButtonitem.setOnClickMethod(this,"menuButtonDidClick");
		System.out.println("报告批准。。。"+token);
		//navigationBar.setLeftBarItem(menuButtonitem);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		//跳转到搜索页
		if (itemId == null || itemId.isEmpty()){
			getDefaultData();
		}
		else{
			getData();
		}
		ButterKnife.inject(this);
	}

	private void getData() {
		// TODO Auto-generated method stub
		if (doneFlag.equals("1")){
			findViewById(R.id.handleView).setVisibility(View.GONE);
		}
		else{
			findViewById(R.id.handleView).setVisibility(View.VISIBLE);
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Token", token);
		if (doneFlag.equals("1")){
			itemId = "1110";
		}

		params.put("ItemId", itemId);
		params.put("PageSize", 10);
		params.put("CurrentPageNo", 1);
		System.out.println("itemId....limian......"+itemId);
		System.out.println(params);
	/*	EasyAPI.apiConnectionAsync(DataExamineActivity.this, true, false, ApiMethodDescription.get(uri), params, new EasyAPI.ApiFastSuccessFailedCallBack() {

			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
			}

			@Override
			public String onFailed(ApiError error) {
				Toast.makeText(DataExamineActivity.this,"当前没有相对应的数据!",Toast.LENGTH_SHORT).show();
				return null;
			}
		});*/
		PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.listView);


		wrapper = new ListPageWrapper<View>(DataExamineActivity.this) {

			@Override
			protected ApiMethodDescription onPageGetApiMethodDescription() {
				// TODO Auto-generated method stub
				uri = "/ServiceUST.asmx/Get_AuditList";
				System.out.println("itemId.........."+itemId);
				if (doneFlag.equals("1")){
					uri = "/ServiceUST.asmx/Get_AppAuditedList";
				}
				return ApiMethodDescription.get(uri);
			}

			@Override
			protected void onPageToInit(final PageController pageController) {
				// TODO Auto-generated method stub
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("Token", token);
				if (doneFlag.equals("1")){
					itemId = "1110";
				}

				params.put("ItemId", itemId);
				pageController.setApiParams(params);
				pageController.pageName = "CurrentPageNo";
				pageController.stepName = "PageSize";
				System.out.println("itemId....limian......"+itemId);
				System.out.println(params);


				pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){
					@Override
					public PageInfo adapter(JsonElement input) {
						System.out.println("aaaaaaaaaaaaaa............"+input);
						PageInfo pageInfo = new PageInfo();
						JsonObject inputJson = input.getAsJsonObject();
						boolean resIsSuccess = inputJson.get("Success").getAsBoolean();
						//返回数据没有内容提示用户
						if(!resIsSuccess){
							String message = inputJson.get("Message").getAsString();
							Toast.makeText(DataExamineActivity.this, message, Toast.LENGTH_SHORT).show();
							return pageInfo;
						}
						pageInfo.isSuccess = true;
						JsonArray inputJsonArray = inputJson.get("Data").getAsJsonArray();
						System.out.println("jsoooooooooooooooooooo..."+inputJsonArray.get(0).toString());
						int size = inputJsonArray.size();
						System.out.println("长度。。。。"+size);
						list = new ArrayList<JsonElement>();
						for (int i=0;i<size;i++){
							list.add(inputJsonArray.get(i));
						}

						pageInfo.listData = list;

						pageInfo.totalPage = pageController.getCurrentPage()+1;
						System.out.println("pageInfo.totalPage..."+pageInfo.totalPage);
						return pageInfo;
					}

				});
			}

			@Override
			protected View onPageCreateCell(int position) {
				// TODO Auto-generated method stub
				int itemViewID = R.layout.data_auditing_item;
				if (doneFlag.equals("1")){
					itemViewID = R.layout.data_audited_item;
				}
				ListViewCell listCell = (ListViewCell) View.inflate(DataExamineActivity.this, itemViewID, null);
				listCell.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// TODO Auto-generated method stub
						ListViewCell cell = (ListViewCell)arg0;
						if (arg1.getAction()==MotionEvent.ACTION_DOWN){
							int i = (int) arg1.getX();
							String positionflag = String.valueOf(cell.getPosition());
							if(!doneFlag.equals("1")) {
								if (i >= 950) {
									if (arrayList.contains(positionflag)) {
										arrayList.remove(positionflag);
										((ImageView) cell.findViewById(R.id.selectIcon)).setImageResource(R.drawable.icon_unchosen);
									} else {
										arrayList.add(positionflag);
										((ImageView) cell.findViewById(R.id.selectIcon)).setImageResource(R.drawable.icon_chosen);
									}
									return true;
								}
							}
						}
						return false;
					}
				});
				return listCell;
			}

			@Override
			protected void onPageCellSetData(int position, View cell,
											 Object data) {
				// TODO Auto-generated method stub

				System.out.println("data......."+data);

				ListViewCell cellview = (ListViewCell) cell;
				cellview.setPosition(position);
				if (doneFlag.equals("1")){
					EasyUI.setTextViewText(cellview.findViewById(R.id.sampleIdTextView), (JsonObject)data, "Report_ID", "暂无");
					EasyUI.setTextViewText(cellview.findViewById(R.id.conSignIdTextView), (JsonObject)data, "ConSign_ID", "暂无");
					EasyUI.setTextViewText(cellview.findViewById(R.id.examTimeTextView), (JsonObject)data, "Report_CreateDate", "暂无");
					EasyUI.setTextViewText(cellview.findViewById(R.id.tv_shouchijishenhe), (JsonObject)data, "Report_Audit_Time", "暂无");
				}
				else{
					if (!arrayList.contains(position)){
						((ImageView)cellview.findViewById(R.id.selectIcon)).setImageResource(R.drawable.icon_unchosen);
					}else{
						((ImageView)cellview.findViewById(R.id.selectIcon)).setImageResource(R.drawable.icon_chosen);
					}
					EasyUI.setTextViewText(cellview.findViewById(R.id.sampleIdTextView), (JsonObject)data, "Report_ID", "暂无");
					EasyUI.setTextViewText(cellview.findViewById(R.id.reportIdTextView), (JsonObject)data, "ConSign_ID", "暂无");
					EasyUI.setTextViewText(cellview.findViewById(R.id.reportTimeTextView), (JsonObject)data, "Report_CreateDate", "暂无");
				}
			}
		};
		System.out.println("这里走了没");
		wrapper.wrap(listView);

		listView.setOnItemClickListener(this);


	}

	private void getDefaultData() {
		// TODO Auto-generated method stub
		//获取分类
		String Type = "0";//报告批准分类
		HashMap<String, Object> paramsType = new HashMap<String, Object>();
		paramsType.put("Token", token);
		paramsType.put("Type", Type);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/Get_ItemKind"), paramsType, new EasyAPI.ApiFastSuccessFailedCallBack() {

			@Override
			public void onSuccessJson(JsonElement result) {
				// TODO Auto-generated method stub
				JsonArray json = result.getAsJsonObject().get("Data").getAsJsonArray();
				if (json.size()>0){
					kindId = json.get(0).getAsJsonObject().get("KindId").getAsString();
					HashMap<String, Object> paramsItem = new HashMap<String, Object>();
					String Type = "0";//报告批准分类
					paramsItem.put("Token", token);
					paramsItem.put("Type", Type);
					paramsItem.put("KindId", kindId);
					EasyAPI.apiConnectionAsync(DataExamineActivity.this, true, false, ApiMethodDescription.get("/ServiceUST.asmx/Get_ItemItem"), paramsItem, new EasyAPI.ApiFastSuccessFailedCallBack() {

						@Override
						public void onSuccessJson(JsonElement result) {
							// TODO Auto-generated method stub
							System.out.println(result);
							JsonArray json = result.getAsJsonObject().get("Data").getAsJsonArray();
							if (json.size()>0){
								itemId = json.get(0).getAsJsonObject().get("ItemId").getAsString();
								getData();
							}
						}

						@Override
						public String onFailed(ApiError error) {
							// TODO Auto-generated method stub
							return null;
						}
					});
				}
				else{
					LSAlert.showAlert(DataExamineActivity.this, "当前用户没有批准权限或当前不存在报告记录");
				}
			}

			@Override
			public String onFailed(ApiError error) {
				// TODO Auto-generated method stub
				int i = 1;
				return null;
			}
		});

	}
	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {

		List list = wrapper.getPageController().getList();
		String jsonString = list.get(position-1).toString();
		String consignId = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("ConSign_ID").getAsString();
		System.out.println("coudin_id..."+consignId);
//		if (doneFlag.equals("1")){
		Intent intent = new Intent(DataExamineActivity.this, ExamineReportDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("ConsignId", consignId);
		intent.putExtras(bundle);
		startActivity(intent);
//		}

	}


	public void searchButtonDidClick(){
		Intent intent = new Intent(DataExamineActivity.this, ExamineSearchActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("TypeTitle", typeTitle);
		bundle.putString("ProjectTitle", projectTitle);
		bundle.putString("FlagTitle", flagTitle);
		bundle.putString("ItemId", itemId);
		bundle.putString("doneFlag", doneFlag);
		bundle.putString("KindId", kindId);
		intent.putExtras(bundle);
		startActivityForResult(intent, 2);


	}



	@OnClick(R.id.examineAllButton)
	protected void auditingAllButtonDidClick(){
		if (wrapper!=null&&wrapper.getPageController().getList().size()>0){
			auditedCounter =0;
			loseauditedCounter = 0;
			LSAlert.showProgressHud(DataExamineActivity.this, "批准");
			new AsyncTask<Void, Void, Void>(){

				@Override
				protected Void doInBackground(Void... arg0) {
					List listTemp = wrapper.getPageController().getList();
					for (int i=0;i<listTemp.size();i++){
						String jsonString = listTemp.get(i).toString();
						JsonObject json = EasyGson.jsonFromString(jsonString).getAsJsonObject();
						operationReuest(json);
					}
					return null;
				}
				protected void onPostExecute(Void result) {
					String alertMessage = String.valueOf(auditedCounter)+"个数据审核完毕！" + String.valueOf(loseauditedCounter)+"个数据审核失败！";
					LSAlert.dismissProgressHud();
					LSAlert.showAlert(DataExamineActivity.this, alertMessage);
					arrayList.clear();
					//wrapper.refreshNoMerge();
					getData();
					wrapper.shuaxin();
				}

			}.execute();
		}
		else{
			LSAlert.showAlert(this, "没有选择批准的条目！");
		}
	}
	private void operationReuest(JsonObject json) {
		String ConSign_ID = json.get("ConSign_ID").getAsString();
		String Sample_ID = json.get("Report_ID").getAsString();
		String Exam_Time = json.get("Report_CreateDate").getAsString();
		HashMap<String, Object> handlParams = new HashMap<String, Object>();
		handlParams.put("Token", token);
		handlParams.put("ItemId", itemId);
		handlParams.put("ConSign_ID", ConSign_ID);
		handlParams.put("Report_ID", Sample_ID);
		handlParams.put("Report_CreateDate", Exam_Time);
		String url = "http://"+ApiBase.sharedInstance().apiUrl+"/ServiceUST.asmx/Set_AuditList";
		HttpUrlConnect httpConnect = new HttpUrlConnect(url,handlParams);
		if (httpConnect.startConnection()){
			String result = httpConnect.getResultString();
			System.out.println("里面的结果。。。。"+result);
			String jsonString = RegKit.match(result, ">(\\{.+\\})</",1);

			JsonObject jsonResult = EasyGson.jsonFromString(jsonString).getAsJsonObject();
			if (jsonResult.get("Success").getAsString().equalsIgnoreCase("true")){
				auditedCounter = auditedCounter+1;
			}
			else{
				loseauditedCounter = loseauditedCounter+1;
			}
		}
		else{
			loseauditedCounter = loseauditedCounter+1;
		}
	}

	@OnClick(R.id.examineChoosenButton)
	protected void auditingChoosenButtonDidClick(){
		if (arrayList.size()>0){
			auditedCounter =0;
			loseauditedCounter = 0;
			LSAlert.showProgressHud(DataExamineActivity.this, "批准");
			new AsyncTask<Void, Void, Void>(){

				@Override
				protected Void doInBackground(Void... arg0) {
					List listTemp = wrapper.getPageController().getList();
					for (int i=0;i<arrayList.size();i++){
						int position = Integer.parseInt(arrayList.get(i));
						String jsonString = listTemp.get(position).toString();
						JsonObject json = EasyGson.jsonFromString(jsonString).getAsJsonObject();
						operationReuest(json);
					}
					return null;
				}
				protected void onPostExecute(Void result) {
					String alertMessage = String.valueOf(auditedCounter)+"个数据审核完毕！" + String.valueOf(loseauditedCounter)+"个数据审核失败！";
					LSAlert.dismissProgressHud();
					LSAlert.showAlert(DataExamineActivity.this, alertMessage);

					arrayList.clear();
					//wrapper.refreshNoMerge();
					getData();
				};

			}.execute();
		}
		else{
			LSAlert.showAlert(this, "没有选择批准的条目！");
		}

	}
	public void menuButtonDidClick(){
		menu.toggle();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2 != null){
			Bundle bundle = arg2.getExtras();
			kindId = bundle.getString("KindId");
			itemId = bundle.getString("ItemId");
			doneFlag = bundle.getString("doneFlag");
			typeTitle = bundle.getString("TypeTitle");
			projectTitle = bundle.getString("ProjectTitle");
			flagTitle = bundle.getString("FlagTitle");
		}
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
			isExit = true; // 准备�?��
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消�?��
				}
			}, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任�?

		} else {
			finish();
			System.exit(0);
		}
	}
}