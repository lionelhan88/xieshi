package com.lessu.xieshi.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.google.gson.EasyGson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.uikit.views.LSAlert.DialogCallback;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lessu.xieshi.R.id.tv_huiyuanhao;
import static com.lessu.xieshi.R.id.tv_zhaunghao;

public class ProjectListActivity  extends XieShiSlidingMenuActivity implements OnItemClickListener {
	/**
	 * 跳转查询页面返回后需要的request_code;
	 */
	public static final int SEARCH_REQUEST_CODE = 3;
	private ArrayList<Marker> markerlist = new ArrayList<>();
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	Boolean isFirstLoc = true;
	JsonArray list = new JsonArray();
	JsonArray sortList = new JsonArray();
	String memberCode = "";
	String hour = "4";
	LatLng myLocation;
	private ListView listView;
	private BarButtonItem handleButtonItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_list_activity);
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		this.setTitle("基桩静载");
		navigationBar.setBackgroundColor(0xFF3598DC);
		handleButtonItem = new BarButtonItem(this, R.drawable.back);
		handleButtonItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		navigationBar.setLeftBarItem(handleButtonItem);
		initBaiduMap();
		BarButtonItem	searchButtonitem = new BarButtonItem(this , R.drawable.icon_navigation_search );
		searchButtonitem.setOnClickMethod(this,"searchButtonDidClick");
		navigationBar.setRightBarItem(searchButtonitem);
		ButterKnife.bind(this);
		getProjectList();
	}

	/**
	 * 初始化百度地图配置
	 */
	private void initBaiduMap(){
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		//监听定位变化
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		//间隔1s刷新位置
		option.setScanSpan(1000);
		mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				final int i = arg0.getZIndex();
				JsonObject itemObject = list.get(i).getAsJsonObject();
				String title = itemObject.get("stakeName").getAsString();
				LSAlert.showDialog(ProjectListActivity.this, "工程信息", title, "导航", "取消", new DialogCallback() {

					@Override
					public void onConfirm() {
						naviByBaiduMap(i);
					}

					@Override
					public void onCancel() {

					}
				});
				return false;
			}
		});
		mLocClient.setLocOption(option);
		mLocClient.start();


	}
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置  
			if (location == null || mMapView == null)
				return;
			double x = location.getLatitude();
			double y = location.getLongitude();
			myLocation = new LatLng(x, y);

			LogUtil.showLogD("我的位置gongcheng。。。。。"+myLocation);
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		@Override
		public void onConnectHotSpotMessage(String s, int i) {

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 获取工程信息
	 */
	private void getProjectList(){
		if (memberCode == null || hour == null || hour.isEmpty()){
//			Intent intent = new Intent(this, ProjectSearchActivity.class);
//			startActivityForResult(intent, 3);

		}
		else{
			HashMap<String, Object> params = new HashMap<String, Object>();
			String token = LSUtil.valueStatic("Token");
			params.put("Token", token);
			params.put("Hour", Integer.valueOf(hour));
			params.put("MemberCode",memberCode);
			LogUtil.showLogD("jizhuangjingzai ..."+params.toString());
			EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceStake.asmx/GetMapList"), params , new EasyAPI.ApiFastSuccessCallBack() {
				@Override
				public void onSuccessJson(JsonElement result) {
					System.out.println("asdasdasda..............."+result);
					String jsonString = result.getAsJsonObject().get("Data").toString();
					final JsonElement jsonElement = EasyGson.jsonFromString(jsonString);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mBaiduMap.clear();
							if(markerlist!=null) {
								for (int i = 0; i < markerlist.size(); i++) {
									markerlist.get(i).remove();
								}
							}
							markerlist.clear();
							System.out.println("asdasdasda..............."+jsonElement.isJsonArray());
							if (jsonElement.isJsonArray()){
								list = jsonElement.getAsJsonArray();
								sortList = list;
								for (int i = 0;i<list.size();i++){
									JsonObject thisPoint = list.get(i).getAsJsonObject();
									String mapAble = thisPoint.get("map").getAsString();
									if (mapAble.equals("true")){
										float x = thisPoint.get("x").getAsFloat();
										float y = thisPoint.get("y").getAsFloat();
										if (i==0){
											//设定中心点坐标
											LatLng cenpt = new LatLng(x,y);

											//定义地图状态
											MapStatus mMapStatus = new MapStatus.Builder()
													.target(cenpt)
													.build();
											//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

											MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
											//改变地图状态
											mBaiduMap.setMapStatus(mMapStatusUpdate);
										}
										//定义Maker坐标点
										LatLng point = new LatLng(x, y);
										//构建Marker图标
										BitmapDescriptor bitmap = BitmapDescriptorFactory
												.fromResource(R.drawable.icon_gcoding);
										//构建MarkerOption，用于在地图上添加Marker
										OverlayOptions option = new MarkerOptions()
												.position(point).zIndex(i)
												.icon(bitmap);
										//在地图上添加Marker，并显示
										Marker marker = (Marker) mBaiduMap.addOverlay(option);

										markerlist.add(marker);

										//mBaiduMap.addOverlay(option);
									}
								}
								listView.setAdapter(adapter);
							}else{
								for (int i = 0; i <list.size() ; i++) {
									list.remove(i);
								}
								listView.setAdapter(null);
							}

						}
					});
				}
			});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
		final int i = position;
		JsonObject itemObject = list.get(i).getAsJsonObject();
		String title = itemObject.get("stakeName").getAsString();
		LSAlert.showDialog(ProjectListActivity.this, "工程信息", title, "导航", "取消", new DialogCallback() {

			@Override
			public void onConfirm() {
				naviByBaiduMap(i);
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 导航
	 * @param position
	 */
	private void naviByBaiduMap(int position) {
		if (myLocation != null){
			LogUtil.showLogD("myLocation....."+myLocation);
			JsonObject itemObject = list.get(position).getAsJsonObject();
			double x = itemObject.get("x").getAsDouble();
			double y = itemObject.get("y").getAsDouble();
			LatLng pt1 = myLocation;
			LatLng pt2 = new LatLng(x, y);
			// 构建 导航参数
			NaviParaOption para = new NaviParaOption();
			para.startPoint(pt1);
			para.startName("从这里开始");
			para.endPoint(pt2);
			para.endName("到这里结束");
			try {
				BaiduMapNavigation.openBaiduMapNavi(para, ProjectListActivity.this);
			} catch (BaiduMapAppNotSupportNaviException e) {
				e.printStackTrace();
				AlertDialog.Builder builder = new AlertDialog.Builder(ProjectListActivity.this);
				builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						BaiduMapNavigation.finish(ProjectListActivity.this);
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		}
		else{
			LSAlert.showAlert(ProjectListActivity.this, "获取您的位置信息失败");
		}
	}
	 BaseAdapter adapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View getView(int position, View view, ViewGroup parentViewGroup) {
			ViewHolder viewHolder =null;
			if (view == null){
				view = View.inflate(ProjectListActivity.this, R.layout.project_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.projectTextView = (TextView) view.findViewById(R.id.projectNameTextView);
				viewHolder.tv_huiyuanhao = (TextView) view.findViewById(tv_huiyuanhao);
				viewHolder.tv_shebeibianhao = (TextView) view.findViewById(R.id.tv_shebeibianhao);
				viewHolder.tv_zhaunghao = (TextView) view.findViewById(tv_zhaunghao);
				viewHolder.tv_projectName = (TextView)view.findViewById(R.id.tv_project_name);
				view.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) view.getTag();
			}

			JsonObject itemObject = list.get(position).getAsJsonObject();
			String stakeName = itemObject.get("stakeName").getAsString();
			String[] split = stakeName.split(",");
			String huiyuanhao = split[0];
			String gongchenname = split[1];
            String shebeihao =split[2];
            String zhuanghao =split[3];
			String projectName = split[4];
			System.out.println("itemObject....."+itemObject.toString());
			viewHolder.projectTextView.setText(gongchenname);
			viewHolder.tv_huiyuanhao.setText(huiyuanhao);
			viewHolder.tv_shebeibianhao.setText(shebeihao);
			viewHolder.tv_zhaunghao.setText(zhuanghao);
			viewHolder.tv_projectName.setText(projectName);
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
		 class ViewHolder {
			 TextView projectTextView;
			 TextView tv_huiyuanhao ;
			 TextView tv_shebeibianhao ;
			 TextView tv_zhaunghao ;
			 TextView tv_projectName;
		 }
	};


	@OnClick(R.id.mapButton)
	protected void mapButtonDidClick(){
		findViewById(R.id.bmapView).setVisibility(View.VISIBLE);
		findViewById(R.id.mapButton).setBackgroundResource(R.drawable.juxing);
		TextView tv = (TextView) findViewById(R.id.tv_map);
		tv.setTextColor(this.getResources().getColor(R.color.white));
		findViewById(R.id.projectButton).setBackgroundResource(R.drawable.yuanjiaojuxing);
		TextView tv1 = (TextView) findViewById(R.id.tv_project);
		tv1.setTextColor(this.getResources().getColor(R.color.textcolor));
		findViewById(R.id.iv_map).setBackgroundResource(R.drawable.ditu1);
		findViewById(R.id.iv_project).setBackgroundResource(R.drawable.zuijin);
		findViewById(R.id.listView).setVisibility(View.GONE);
	}
	@OnClick(R.id.projectButton)
	protected void projectButtonDidClick(){
		findViewById(R.id.listView).setVisibility(View.VISIBLE);
		findViewById(R.id.mapButton).setBackgroundResource(R.drawable.yuanjiaojuxing);
		TextView tv = (TextView) findViewById(R.id.tv_map);
		tv.setTextColor(this.getResources().getColor(R.color.textcolor));
		findViewById(R.id.projectButton).setBackgroundResource(R.drawable.juxing);
		TextView tv1 = (TextView) findViewById(R.id.tv_project);
		tv1.setTextColor(this.getResources().getColor(R.color.white));
		findViewById(R.id.iv_map).setBackgroundResource(R.drawable.ditu);
		findViewById(R.id.iv_project).setBackgroundResource(R.drawable.zuijin1);
		findViewById(R.id.bmapView).setVisibility(View.GONE);
	}

	/**
	 * 按会员名称，时效时间查询工程
	 */
	public void searchButtonDidClick(){
		Intent intent = new Intent(this, ProjectSearchActivity.class);
		startActivityForResult(intent, SEARCH_REQUEST_CODE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				memberCode = bundle.get("MemberCode").toString();
				hour = bundle.get("Hour").toString();
				getProjectList();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
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
}
