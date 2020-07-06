package com.lessu.xieshi.construction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.EasyGson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

public class ConstructionListActivity extends XieShiSlidingMenuActivity implements OnItemClickListener {
	ListPageWrapper wrapper;
	String NotFinishedOnly ;
	String QueryStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.construction_list_activity);
		this.setTitle("工地列表");
		navigationBar.setBackgroundColor(0xFF3598DC);
		
		BarButtonItem	searchButtonitem = new BarButtonItem(this , R.drawable.icon_navigation_search );
        searchButtonitem.setOnClickMethod(this,"searchButtonDidClick");	
    
        navigationBar.setRightBarItem(searchButtonitem);
        
        BarButtonItem	menuButtonitem = new BarButtonItem(this ,R.drawable.icon_navigation_menu);
        menuButtonitem.setOnClickMethod(this,"menuButtonDidClick");	
        //navigationBar.setLeftBarItem(menuButtonitem);
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
				return ApiMethodDescription.get("/ServiceSM.asmx/ProjectList");
			}

			@Override
			protected void onPageToInit(final PageController pageController) {
				// TODO Auto-generated method stub
				String token = LSUtil.valueStatic("Token");
				
				HashMap<String, Object> params = new HashMap<String, Object>();
	            params.put("Token", token);
	            params.put("NotFinishedOnly", NotFinishedOnly);
	            params.put("QueryStr", QueryStr);
	            pageController.setApiParams(params);
	            pageController.pageName = "CurrentPageNo";
	            pageController.stepName = "PageSize";
	            pageController.step = 5;
	            pageController.setPageinfoAdapter(new PageInfoAdapterInterface(){

					@Override
					public PageInfo adapter(JsonElement input) {
						// TODO Auto-generated method stub
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
				LinearLayout listCell = (LinearLayout) View.inflate(ConstructionListActivity.this, R.layout.construction_list_item, null);
				return listCell;
			}

			@Override
			protected void onPageCellSetData(int position, View cell,
					Object data) {
				// TODO Auto-generated method stub
		    	EasyUI.setTextViewText(cell.findViewById(R.id.projectNameTextView), (JsonObject)data, "ProjectName", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.projectStatusTextView), (JsonObject)data, "ProjectStatus", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.projectNatureTextView), (JsonObject)data, "ProjectNature", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.projectRegionTextView), (JsonObject)data, "ProjectRegion", "暂无");
		    	EasyUI.setTextViewText(cell.findViewById(R.id.projectAddressTextView), (JsonObject)data, "ProjectAddress", "暂无");
			}
		};
		
		wrapper.wrap(listView);
			
		listView.setOnItemClickListener(this);
		
      //  ButterKnife.bind(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
		Intent intent = new Intent(ConstructionListActivity.this, TestConstractActivity.class);
        List list = wrapper.getPageController().getList();
		String jsonString = list.get(position-1).toString();
		String projectId = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("ProjectId").getAsString();
		Bundle bundle = new Bundle();
		bundle.putString("ProjectId", projectId);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	public void searchButtonDidClick(){
        Intent intent = new Intent(ConstructionListActivity.this, ConstructionSearchActivity.class);
        startActivityForResult(intent, 4);
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
		NotFinishedOnly =  bundle.get("NotFinishedOnly").toString();
		QueryStr = bundle.get("QueryStr").toString();
		}
	}
	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//	    	exitBy2Click();
//	    	return false;
//	    } else {
//	        return super.dispatchKeyEvent(event);
//	    }
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
}
