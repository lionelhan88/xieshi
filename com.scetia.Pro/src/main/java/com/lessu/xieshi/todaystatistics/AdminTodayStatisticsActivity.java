package com.lessu.xieshi.todaystatistics;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lessu.foundation.LSUtil;
import com.lessu.navigation.BarButtonItem;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.refreashAndLoad.page.ListPageWrapper;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.XieShiSlidingMenuActivity;
import com.lessu.xieshi.bean.Jinritongjiditu;
import com.lessu.xieshi.customView.ZoomControlsView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

public class AdminTodayStatisticsActivity extends XieShiSlidingMenuActivity implements OnItemClickListener, View.OnClickListener {
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	String token = LSUtil.valueStatic("Token");
	String projectName = "";
	String projectArea = "";
	String baogaobianhao="";
	String jianshedanwei="";
	String shigongdanwei="";
	String jianlidanwei="";
	String jiancedanwei="";
	ListPageWrapper wrapper;
	ArrayList<Integer> arrayList = new ArrayList<Integer>();
	JsonArray list = new JsonArray();
	String Type = "";
	PullToRefreshListView listView;
	private MapView map_jinritongji;
	LatLng myLocation;
	Boolean isFirstLoc = true;
	private List<Jinritongjiditu.DataBean.ListContentBean> listContent;
	private ListView lv_ditu;
	private MyAdapter madapter;
	private LinearLayout ll_ditu;
	private ImageView iv_ditu;
	private TextView tv_ditu;
	private LinearLayout ll_liebiao;
	private ImageView iv_liebiao;
	private TextView tv_liebiao;
	private LinearLayout ll_yeshu;
	private Button bt_shangyiye;
	private Button bt_xiayiye;
	private Jinritongjiditu jinritongjiditu;
	private ArrayList<Marker> markerlist = new ArrayList<Marker>();;
	private RelativeLayout rl_map;
	private Button bt_sousuo;
	private boolean isfirstmap=true;
	private LinearLayout ll_daohang;
	private TextView tv_daohangtitle;
	private Button bt_danghang;
	private Button bt_qvxiao;
	private TextView tv_fujin;
	private LinearLayout ll_spinner;
	private LinearLayout ll_xialaliebiao;
	private ImageView iv_xiala;
	private boolean isxialaopen=false;
	private TextView tv_1;
	private TextView tv_3;
	private TextView tv_5;
	private TextView tv_10;
	private int rangeindex=1;
	private ZoomControlsView zcv_zoom;
	private boolean sousuo;
	private TextView tv_yeshuxianshi;
	private TextView tv_max;
	private boolean ismax;
	private boolean isfirstinto=true;
	private int pageCount;
	private TextView tv_daohangaddress;
	private RelativeLayout rl_daohang;
	private RelativeLayout rl_xinxichaxun;
	private LinearLayout ll_pparent;
	private boolean isclickmark;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_today_statistics_activity);
		this.setTitle("工地查询");
		navigationBar.setBackgroundColor(0xFF3598DC);
		try{
			System.loadLibrary("locSDK7a");
			LogUtil.showLogD("百度地图加载成功");
		}catch(Throwable  ex){
			LogUtil.showLogE("百度地图加载失败...");
			ex.printStackTrace();
		}

		ll_pparent = (LinearLayout) findViewById(R.id.ll_pparent);

		ll_ditu = (LinearLayout) findViewById(R.id.ll_ditu);
		iv_ditu = (ImageView) findViewById(R.id.iv_ditu);
		tv_ditu = (TextView) findViewById(R.id.tv_ditu);

		ll_liebiao = (LinearLayout) findViewById(R.id.ll_liebiao);
		iv_liebiao = (ImageView) findViewById(R.id.iv_liebiao);
		tv_liebiao = (TextView) findViewById(R.id.tv_liebiao);

		ll_yeshu = (LinearLayout) findViewById(R.id.ll_yeshu);
		bt_shangyiye = (Button) findViewById(R.id.bt_shangyiye);
		bt_xiayiye = (Button) findViewById(R.id.bt_xiayiye);
		rl_map = (RelativeLayout) findViewById(R.id.rl_map);
		bt_sousuo = (Button) findViewById(R.id.bt_sousuo);

		ll_daohang = (LinearLayout) findViewById(R.id.ll_daohang);
		tv_daohangtitle = (TextView) findViewById(R.id.tv_daohangtitle);
		tv_daohangaddress = (TextView) findViewById(R.id.tv_daohangaddress);
		rl_daohang = (RelativeLayout) findViewById(R.id.rl_daohang);
		rl_xinxichaxun = (RelativeLayout) findViewById(R.id.rl_xinxichaxun);

		ll_xialaliebiao = (LinearLayout) findViewById(R.id.ll_xialaliebiao);
		tv_fujin = (TextView) findViewById(R.id.tv_fujin);
		ll_spinner = (LinearLayout) findViewById(R.id.ll_spinner);
		iv_xiala = (ImageView) findViewById(R.id.iv_xiala);
		lv_ditu=(ListView) findViewById(R.id.lv_ditu);
		tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_3 = (TextView) findViewById(R.id.tv_3);
		tv_5 = (TextView) findViewById(R.id.tv_5);
		tv_10 = (TextView) findViewById(R.id.tv_10);
		tv_max = (TextView) findViewById(R.id.tv_max);
		tv_yeshuxianshi = (TextView) findViewById(R.id.tv_yeshuxianshi);
		ll_spinner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isxialaopen){
					iv_xiala.setBackgroundResource(R.drawable.xiala1);
					ll_xialaliebiao.setVisibility(View.VISIBLE);
				}else{
					iv_xiala.setBackgroundResource(R.drawable.xialaa);
					ll_xialaliebiao.setVisibility(View.GONE);
				}
				isxialaopen=!isxialaopen;

			}
		});

		BarButtonItem searchButtonitem = new BarButtonItem(this , R.drawable.shuaxin );

		searchButtonitem.setOnClickMethod(this,"searchButtonDidClick");

		navigationBar.setRightBarItem(searchButtonitem);
		map_jinritongji = (MapView) findViewById(R.id.map_jinritongji);
		map_jinritongji.showZoomControls(false);
		mBaiduMap = map_jinritongji.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		ll_ditu.setOnClickListener(this);
		ll_liebiao.setOnClickListener(this);
		bt_shangyiye.setOnClickListener(this);
		bt_xiayiye.setOnClickListener(this);
		bt_sousuo.setOnClickListener(this);
		tv_1.setOnClickListener(this);
		tv_3.setOnClickListener(this);
		tv_5.setOnClickListener(this);
		tv_10.setOnClickListener(this);
		tv_max.setOnClickListener(this);
		lv_ditu.setOnItemClickListener(this);
		rl_map.setOnClickListener(this);

		zcv_zoom = (ZoomControlsView) findViewById(R.id.zcv_zoom);
		zcv_zoom.setMapView(map_jinritongji);
		ButterKnife.bind(this);

	}
	@Override
	protected void onStart() {
		super.onStart();
	/*	if(jinritongjiditu!=null) {
			if (sousuo) {
				ConnectNeta(1, rangeindex);
				*//*if(ismax){
					ConnectNetb(1);
				}else {
					ConnectNeta(1, rangeindex);
				}*//*
			} else {
				ConnectNeta(jinritongjiditu.getData().getCurrentPageNo(), rangeindex);
				*//*if(ismax){
					ConnectNetb(jinritongjiditu.getData().getCurrentPageNo());
				}else {
					ConnectNeta(jinritongjiditu.getData().getCurrentPageNo(), rangeindex);
				}*//*
			}
		}else{
			ConnectNeta(1, 3);
		}
		sousuo=false;*/
	}

	/**
	 * 带有范围的查询
	 * @param pageindex
	 * @param range 范围条件
	 */
	private void ConnectNeta(final int pageindex, int range) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Token", token);
		params.put("Type", 1);
		params.put("ProjectName", projectName);
		params.put("ProjectArea", projectArea);
		params.put("BuildingReportNumber", baogaobianhao);
		params.put("BuildUnitName", shigongdanwei);
		params.put("ConstructUnitName", jianshedanwei);
		params.put("SuperviseUnitName", jianlidanwei);
		params.put("DetectionUnitName", jiancedanwei);
		params.put("CurrentLocation",getMyself());
		//params.put("CurrentLocation","121.4558,31.18644");
		if(range!=-1){
			//如果传入有效范围才加入这个参数
			//如果用户选择了范围不限，则不加入这个参数
			params.put("DistanceRange", range);
		}
		params.put("PageSize", 10);
		params.put("CurrentPageNo", pageindex);
		System.out.println("params....."+params);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceTS.asmx/ManageUnitTodayStatisProjectList"),params,new EasyAPI.ApiFastSuccessFailedCallBack(){
			@Override
			public void onSuccessJson(JsonElement result) {
				jinritongjiditu = GsonUtil.JsonToObject(result.toString(), Jinritongjiditu.class);
				System.out.println("connect----a"+result);
				if(jinritongjiditu.isSuccess()) {
					pageCount = jinritongjiditu.getData().getPageCount();
					listContent = jinritongjiditu.getData().getListContent();

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mBaiduMap.clear();
							if (markerlist != null) {
								for (int i = 0; i < markerlist.size(); i++) {
									markerlist.get(i).remove();
								}
							}
							markerlist.clear();
							for (int i = 0; i < listContent.size(); i++) {
								String projectCoordinates = listContent.get(i).getProjectCoordinates();

								if (projectCoordinates != null && !projectCoordinates.equals("")) {
									//这里是服务器未传工地的经纬度信息，保证列表信息可以刷出来，不确定这种写法是否ok,
									//因为没有经纬度信息，附近几公里的功能失效，用户点了没反应！！加载进去后主界面地图空白！！！
									System.out.println("mylocation..." + projectCoordinates);
									String[] split = projectCoordinates.split(",");
									double x = Double.parseDouble(split[1]);
									double y = Double.parseDouble(split[0]);
									if (i == 0) {
										//设定中心点坐标
										LatLng cenpt = new LatLng(x, y);

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
								}
							}

							tv_yeshuxianshi.setText(pageindex + "/" + pageCount);


							if (jinritongjiditu.getData().getCurrentPageNo() == 1) {
								bt_shangyiye.setTextColor(0xff8f8f8f);
								bt_shangyiye.setClickable(false);
							} else {
								bt_shangyiye.setTextColor(0xff333333);
								bt_shangyiye.setClickable(true);
							}
							if (jinritongjiditu.getData().getCurrentPageNo() == jinritongjiditu.getData().getPageCount()) {
								bt_xiayiye.setTextColor(0xff8f8f8f);
								bt_xiayiye.setClickable(false);
							} else {
								bt_xiayiye.setTextColor(0xff333333);
								bt_xiayiye.setClickable(true);
							}

							mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

								@Override
								public boolean onMarkerClick(Marker arg0) {
									// TODO Auto-generated method stub
									final int i = arg0.getZIndex();
									String title = listContent.get(i).getProjectName();
									String address = listContent.get(i).getProjectAddress();
									ll_daohang.setVisibility(View.VISIBLE);
									tv_daohangtitle.setText(title);
									tv_daohangaddress.setText(address);
									rl_daohang.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											naviByBaiduMap(i);
										}
									});
									rl_xinxichaxun.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											ll_daohang.setVisibility(View.GONE);
											Intent intenta = new Intent(AdminTodayStatisticsActivity.this, AdminTodayinfoActivity.class);
											intenta.putExtra("projectid", listContent.get(i).getProjectId());
											//工地名称
											intenta.putExtra("projectName",listContent.get(i).getProjectName());
											//工地区县
											intenta.putExtra("projectArea",listContent.get(i).getProjectRegion());
											startActivity(intenta);
										}
									});
									return false;
								}
							});
							if (madapter == null) {
								madapter = new MyAdapter();
							} else {
								madapter.notifyDataSetChanged();
							}
							lv_ditu.setAdapter(madapter);
						}
					});
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AdminTodayStatisticsActivity.this,jinritongjiditu.getMessage(),Toast.LENGTH_SHORT).show();
						}
					});
				}

			}

			@Override
			public String onFailed(ApiError error) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mBaiduMap.clear();
						lv_ditu.setAdapter(null);
						tv_yeshuxianshi.setText(0+"/"+0);
						bt_shangyiye.setTextColor(0xff8f8f8f);
						bt_shangyiye.setClickable(false);
						bt_xiayiye.setTextColor(0xff8f8f8f);
						bt_xiayiye.setClickable(false);
					}
				});
				System.out.println("失败了。。。。。。。。。。。。。。。。。。。"+error.errorMeesage);
				return error.errorMeesage;
			}
		});
	}

	/**
	 * 查询范围不限
	 * @param pageindex
	 */
	private void ConnectNetb(final int pageindex) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("Token", token);
		params.put("Type", 1);
		params.put("ProjectName", projectName);
		params.put("ProjectArea", projectArea);
		params.put("BuildingReportNumber", baogaobianhao);
		params.put("BuildUnitName", shigongdanwei);
		params.put("ConstructUnitName", jianshedanwei);
		params.put("SuperviseUnitName", jianlidanwei);
		params.put("DetectionUnitName", jiancedanwei);
		//params.put("CurrentLocation",getMyself());
		System.out.println("getmyself......"+getMyself());
		//params.put("DistanceRange",range);
		params.put("PageSize", 10);
		params.put("CurrentPageNo", pageindex);
		EasyAPI.apiConnectionAsync(this, true, false, ApiMethodDescription.get("/ServiceTS.asmx/ManageUnitTodayStatisProjectList"),params,new EasyAPI.ApiFastSuccessFailedCallBack(){
			@Override
			public void onSuccessJson(JsonElement result) {
				jinritongjiditu = GsonUtil.JsonToObject(result.toString(), Jinritongjiditu.class);
				System.out.println(result);

				if(jinritongjiditu.isSuccess()) {

					pageCount = jinritongjiditu.getData().getPageCount();
					listContent = jinritongjiditu.getData().getListContent();

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mBaiduMap.clear();
							if (markerlist != null) {
								for (int i = 0; i < markerlist.size(); i++) {
									markerlist.get(i).remove();
								}
							}
							markerlist.clear();
							for (int i = 0; i < listContent.size(); i++) {
								String projectCoordinates = listContent.get(i).getProjectCoordinates();

								if (projectCoordinates != null && !projectCoordinates.equals("")) {
									//这里是服务器未传工地的经纬度信息，保证列表信息可以刷出来，不确定这种写法是否ok,
									//因为没有经纬度信息，附近几公里的功能失效，用户点了没反应！！加载进去后主界面地图空白！！！
									String[] split = projectCoordinates.split(",");
									double x = Double.parseDouble(split[1]);
									double y = Double.parseDouble(split[0]);
									if (i == 0) {
										//设定中心点坐标
										LatLng cenpt = new LatLng(x, y);
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
									mBaiduMap.addOverlay(option);
									Marker marker = (Marker) mBaiduMap.addOverlay(option);

									markerlist.add(marker);
								}
							}

							tv_yeshuxianshi.setText(pageindex + "/" + pageCount);


							if (jinritongjiditu.getData().getCurrentPageNo() == 1) {
								bt_shangyiye.setTextColor(0xff8f8f8f);
								bt_shangyiye.setClickable(false);
							} else {
								bt_shangyiye.setTextColor(0xff333333);
								bt_shangyiye.setClickable(true);
							}
							if (jinritongjiditu.getData().getCurrentPageNo() == jinritongjiditu.getData().getPageCount()) {
								bt_xiayiye.setTextColor(0xff8f8f8f);
								bt_xiayiye.setClickable(false);
							} else {
								bt_xiayiye.setTextColor(0xff333333);
								bt_xiayiye.setClickable(true);
							}

							mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

								@Override
								public boolean onMarkerClick(Marker arg0) {
									// TODO Auto-generated method stub
									final int i = arg0.getZIndex();
									String title = listContent.get(i).getProjectName();
									String address = listContent.get(i).getProjectAddress();
									ll_daohang.setVisibility(View.VISIBLE);
									tv_daohangtitle.setText(title);
									tv_daohangaddress.setText(address);
									rl_daohang.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											naviByBaiduMap(i);
										}
									});
									rl_xinxichaxun.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											ll_daohang.setVisibility(View.GONE);
											Intent intentb = new Intent(AdminTodayStatisticsActivity.this, AdminTodayinfoActivity.class);
											intentb.putExtra("projectid", listContent.get(i).getProjectId());
											//工地名称
											intentb.putExtra("projectName",listContent.get(i).getProjectName());
											//工地区县
											intentb.putExtra("projectArea",listContent.get(i).getProjectRegion());
											startActivity(intentb);
										}
									});
									return false;
								}
							});
							if (madapter == null) {
								madapter = new MyAdapter();
							} else {
								madapter.notifyDataSetChanged();
							}
							lv_ditu.setAdapter(madapter);
						}
					});
				}else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AdminTodayStatisticsActivity.this,jinritongjiditu.getMessage(),Toast.LENGTH_SHORT).show();
						}
					});
				}

			}

			@Override
			public String onFailed(ApiError error) {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mBaiduMap.clear();
						lv_ditu.setAdapter(null);
						tv_yeshuxianshi.setText(0+"/"+0);
						bt_shangyiye.setTextColor(0xff8f8f8f);
						bt_shangyiye.setClickable(false);
						bt_xiayiye.setTextColor(0xff8f8f8f);
						bt_xiayiye.setClickable(false);
					}
				});

				System.out.println("失败了。。。。。。。。。。。。。。。。。。。"+error.errorMeesage);
				return error.errorMeesage;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ll_ditu:
				iv_ditu.setImageResource(R.drawable.ditu1a);
				tv_ditu.setTextColor(0xff118bca);
				iv_liebiao.setImageResource(R.drawable.liebiao);
				tv_liebiao.setTextColor(0xff9b9cae);
				rl_map.setVisibility(View.VISIBLE);
				lv_ditu.setVisibility(View.GONE);
				ll_yeshu.setVisibility(View.GONE);
				break;

			case R.id.ll_liebiao:
				iv_ditu.setImageResource(R.drawable.ditua);
				tv_ditu.setTextColor(0xff9b9cae);
				iv_liebiao.setImageResource(R.drawable.liebiao1);
				tv_liebiao.setTextColor(0xff118bca);
				rl_map.setVisibility(View.GONE);
				lv_ditu.setVisibility(View.VISIBLE);
				ll_yeshu.setVisibility(View.VISIBLE);
				iv_xiala.setBackgroundResource(R.drawable.xialaa);
				ll_xialaliebiao.setVisibility(View.GONE);
				isxialaopen=false;
				break;

			case R.id.bt_shangyiye:
				ConnectNeta(jinritongjiditu.getData().getCurrentPageNo() - 1, rangeindex);
				/*if(ismax){
					ConnectNetb(jinritongjiditu.getData().getCurrentPageNo() - 1);
				}else {
					ConnectNeta(jinritongjiditu.getData().getCurrentPageNo() - 1, rangeindex);
				}*/
				break;

			case R.id.bt_xiayiye:
				ConnectNeta(jinritongjiditu.getData().getCurrentPageNo() +1, rangeindex);
				/*if(ismax){
					ConnectNetb(jinritongjiditu.getData().getCurrentPageNo() + 1);
				}else {
					ConnectNeta(jinritongjiditu.getData().getCurrentPageNo() + 1, rangeindex);
				}*/
				break;

			case R.id.bt_sousuo:
				//点击了地图上的查询按钮，进入查询页面
				iv_xiala.setBackgroundResource(R.drawable.xialaa);
				ll_xialaliebiao.setVisibility(View.GONE);
				isxialaopen=false;
				Intent intent = new Intent(AdminTodayStatisticsActivity.this, AdminTodayStatisticsSearchActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("ProjectArea", projectArea);
				bundle.putString("ProjectName", projectName);
				bundle.putString("baogaobianhao", baogaobianhao);
				bundle.putString("jianshedanwei", jianshedanwei);
				bundle.putString("shigongdanwei", shigongdanwei);
				bundle.putString("jianlidanwei", jianlidanwei);
				bundle.putString("jiancedanwei", jiancedanwei);

				bundle.putInt("range",rangeindex);
				bundle.putString("CurrentLocation",getMyself());
				intent.putExtras(bundle);
				startActivityForResult(intent, 1);
				break;
			case R.id.tv_1:
				//点击了附近1公里
				ismax=false;
				tv_fujin.setText("附近:1公里");
				iv_xiala.setBackgroundResource(R.drawable.xialaa);
				ll_xialaliebiao.setVisibility(View.GONE);
				isxialaopen=false;
				rangeindex=1;
				ConnectNeta(1,rangeindex);
				break;
			case R.id.tv_3:
				//修改 2018-10-23 由公里改为2公里
				ismax=false;
				tv_fujin.setText("附近:2公里");
				iv_xiala.setBackgroundResource(R.drawable.xialaa);
				ll_xialaliebiao.setVisibility(View.GONE);
				isxialaopen=false;
				rangeindex=2;
				ConnectNeta(1,rangeindex);
				break;
			case R.id.tv_5:
				//修改 2018-10-23 由5公里改为3公里
				ismax=false;
				tv_fujin.setText("附近:3公里");
				iv_xiala.setBackgroundResource(R.drawable.xialaa);
				ll_xialaliebiao.setVisibility(View.GONE);
				isxialaopen=false;
				rangeindex=3;
				ConnectNeta(1,rangeindex);
				break;
			case R.id.tv_max:
				tv_fujin.setText("附近:不限");
				iv_xiala.setBackgroundResource(R.drawable.xialaa);
				ll_xialaliebiao.setVisibility(View.GONE);
				isxialaopen=false;
				ismax = true;
				rangeindex=-1;
				ConnectNeta(1,rangeindex);
				//ConnectNetb(1);
				break;
		}
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return listContent.size();
		}

		@Override
		public Object getItem(int position) {
			return listContent.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(AdminTodayStatisticsActivity.this,R.layout.admin_today_statistics_item,null);
				viewHolder.ProjectNameTextView = (TextView) convertView.findViewById(R.id.ProjectNameTextView);
				viewHolder.ProjectStatusTextView = (TextView) convertView.findViewById(R.id.ProjectStatusTextView);
				viewHolder.ProjectNatureTextView = (TextView) convertView.findViewById(R.id.ProjectNatureTextView);
				viewHolder.ProjectRegionTextView = (TextView) convertView.findViewById(R.id.ProjectRegionTextView);
				viewHolder.ProjectAddressTextView = (TextView) convertView.findViewById(R.id.ProjectAddressTextView);
				convertView.setTag(viewHolder);
			} else {
				//直接通过holder获取下面三个子控件，不必使用findviewbyid，加快了 UI 的响应速度
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.ProjectNameTextView.setText(listContent.get(position).getProjectName());
			viewHolder.ProjectStatusTextView.setText(listContent.get(position).getProjectStatus());
			viewHolder.ProjectNatureTextView.setText(listContent.get(position).getProjectNature());
			viewHolder.ProjectRegionTextView.setText(listContent.get(position).getProjectRegion());
			viewHolder.ProjectAddressTextView.setText(listContent.get(position).getProjectAddress());
			return  convertView;
		}
	}
	static class ViewHolder{
		TextView ProjectNameTextView;
		TextView ProjectStatusTextView;
		TextView ProjectNatureTextView;
		TextView ProjectRegionTextView;
		TextView ProjectAddressTextView;
	}



	@Override
	public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
		//List list = wrapper.getPageController().getList();
		//String jsonString = list.get(position-1).toString();
		//String projectId = EasyGson.jsonFromString(jsonString).getAsJsonObject().get("ProjectId").getAsString();

		String projectId=listContent.get(position).getProjectId();
		//Intent intent = new Intent(AdminTodayStatisticsActivity.this, TodayStatisticsDetailActivity.class);
		Intent intent = new Intent(AdminTodayStatisticsActivity.this, AdminTodayinfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("projectid", projectId);
		//工地名称
		intent.putExtra("projectid",projectId);
		intent.putExtra("projectName",listContent.get(position).getProjectName());
		//工地区县
		intent.putExtra("projectArea",listContent.get(position).getProjectRegion());
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void searchButtonDidClick(){
		if(jinritongjiditu.getData().getPageCount()==1){
			Toast.makeText(AdminTodayStatisticsActivity.this,"当前没有更多新内容!",Toast.LENGTH_SHORT).show();
		}
		int  currentpage=jinritongjiditu.getData().getCurrentPageNo();
		if(currentpage==jinritongjiditu.getData().getPageCount()){
			currentpage=0;
		}
		ConnectNeta(currentpage + 1, rangeindex);
		//2020-07-03不再用ismax来标识是否是不限范围，用rangeIndex=-1来标识，这样可以统一范围
		/*if(ismax){
			ConnectNetb(currentpage + 1);
		}else {
			ConnectNeta(currentpage + 1, rangeindex);
		}*/
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	/*	if (arg2 != null){
			Bundle bundle = arg2.getExtras();
			projectArea = bundle.getString("ProjectArea");
			projectName = bundle.getString("ProjectName");
			baogaobianhao = bundle.getString("baogaobianhao");
			jianshedanwei = bundle.getString("jianshedanwei");
			shigongdanwei = bundle.getString("shigongdanwei");
			jianlidanwei = bundle.getString("jianlidanwei");
			jiancedanwei = bundle.getString("jiancedanwei");
			sousuo = bundle.getBoolean("sousuo");
			tv_fujin.setText("附近:不限");
			iv_xiala.setBackgroundResource(R.drawable.xialaa);
			ll_xialaliebiao.setVisibility(View.GONE);
			isxialaopen=false;
			ismax = true;
		}*/
		if (arg2 != null){
			Bundle bundle = arg2.getExtras();
			projectArea = bundle.getString("ProjectArea");
			projectName = bundle.getString("ProjectName");
			baogaobianhao = bundle.getString("baogaobianhao");
			jianshedanwei = bundle.getString("jianshedanwei");
			shigongdanwei = bundle.getString("shigongdanwei");
			jianlidanwei = bundle.getString("jianlidanwei");
			jiancedanwei = bundle.getString("jiancedanwei");
			sousuo = bundle.getBoolean("sousuo");
			tv_fujin.setText("附近:不限");
			rangeindex=-1;
			iv_xiala.setBackgroundResource(R.drawable.xialaa);
			ll_xialaliebiao.setVisibility(View.GONE);
			ConnectNeta(1,rangeindex);
		}
	}
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || map_jinritongji == null)
				return;
			Double x = location.getLatitude();
			Double y = location.getLongitude();
			System.out.println(x +","+ y);
			myLocation = new LatLng(x, y);
			System.out.println("我的位置。。。。。"+myLocation+","+location.getLocType());
			if(isfirstinto){
				ConnectNeta(1,rangeindex);
				isfirstinto=false;
			}
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
			System.out.println("hot...s..."+s);
			System.out.println("hot...i..."+i);

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
		map_jinritongji.onDestroy();
		map_jinritongji = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		map_jinritongji.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		map_jinritongji.onPause();
	}
	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (PendingIntent.CanceledException e) {
			e.printStackTrace();
		}
	}

	double latitude=0.0;
	double longitude =0.0;
	private String getMyself() {

		if(myLocation!=null){
			latitude=myLocation.latitude;
			longitude=myLocation.longitude;
		}else {
			if(isfirstinto==false) {
				Toast.makeText(AdminTodayStatisticsActivity.this, "请点右上方刷新一下。", Toast.LENGTH_SHORT).show();
			}
		}
		DecimalFormat df = new DecimalFormat("#.000000");
		String dx = df.format(latitude);
		String dy = df.format(longitude);
		System.out.println(dy + "," + dx);
		return dy + "," + dx;
	}
	private void naviByBaiduMap(int position) {
		// TODO Auto-generated method stub
		if (myLocation != null){
			String projectCoordinates = listContent.get(position).getProjectCoordinates();
			System.out.println("mylocation..."+projectCoordinates);
			String[] split = projectCoordinates.split(",");
			double x = Double.parseDouble(split[1]);
			double y = Double.parseDouble(split[0]);
			// TODO Auto-generated method stub
			LatLng pt1 = myLocation;
			LatLng pt2 = new LatLng(x, y);
			// 构建 导航参数
			NaviParaOption para = new NaviParaOption();
			para.startPoint(pt1);
			para.startName("从这里开始");
			para.endPoint(pt2);
			para.endName("到这里结束");
			try {
				BaiduMapNavigation.openBaiduMapNavi(para, AdminTodayStatisticsActivity.this);
			} catch (BaiduMapAppNotSupportNaviException e) {
				e.printStackTrace();
				AlertDialog.Builder builder = new AlertDialog.Builder(AdminTodayStatisticsActivity.this);
				builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						BaiduMapNavigation.finish(AdminTodayStatisticsActivity.this);
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		}
		else{
			LSAlert.showAlert(AdminTodayStatisticsActivity.this, "获取您的位置信息失败");
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			int[] xy = {0, 0};
			ll_daohang.getLocationInWindow(xy);
			int left = xy[0],
					top = xy[1],
					bottom = top + ll_daohang.getHeight(),
					right = left + ll_daohang.getWidth();
			if (ev.getX() > left && ev.getX() < right
					&& ev.getY() > top && ev.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				//ll_daohang.setVisibility(View.VISIBLE);
			} else {
				ll_daohang.setVisibility(View.GONE);
			}

		}
		return super.dispatchTouchEvent(ev);
	}
}