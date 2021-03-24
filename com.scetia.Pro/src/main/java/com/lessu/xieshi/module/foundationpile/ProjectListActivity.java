package com.lessu.xieshi.module.foundationpile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
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
import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.google.gson.JsonArray;
import com.lessu.navigation.BarButtonItem;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.lessu.xieshi.http.service.CommonApiService;
import com.scetia.Pro.common.Util.SPUtil;
import com.scetia.Pro.lib_map.BaiduMapLifecycle;
import com.scetia.Pro.lib_map.MapUtil;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class ProjectListActivity extends XieShiSlidingMenuActivity {
    /**
     * 跳转查询页面返回后需要的request_code;
     */
    public static final int SEARCH_REQUEST_CODE = 3;
    private BaiduMap mBaiduMap;
    private Boolean isFirstLoc = true;
    private String memberCode = "";
    private String hour = "4";
    private LatLng myLocation;
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.mapButton)
    LinearLayout llMapButton;
    @BindView(R.id.tv_map)
    TextView tvMapText;
    @BindView(R.id.projectButton)
    LinearLayout llProjectButton;
    @BindView(R.id.tv_project)
    TextView tvProjectText;
    @BindView(R.id.iv_map)
    ImageView ivMap;
    @BindView(R.id.iv_project)
    ImageView ivProject;
    private FoundationPileListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.project_list_activity;
    }

    @Override
    protected void initView() {
        this.setTitle("基桩静载");
        BarButtonItem searchButtonItem = new BarButtonItem(this, R.drawable.icon_navigation_search);
        searchButtonItem.setOnClickMethod(this, "searchButtonDidClick");
        navigationBar.setRightBarItem(searchButtonItem);
        initBaiduLocation();
    }

    /**
     * 初始化地图
     */
    @PermissionNeed(Manifest.permission.ACCESS_FINE_LOCATION)
    private void initBaiduLocation() {
        //获取地图map对象
        mBaiduMap = mMapView.getMap();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //开是定位初始化
        BaiduMapLifecycle baiduMapLifecycle = new BaiduMapLifecycle(this);
        ProjectListMapLocationListener locationListener = new ProjectListMapLocationListener();
        //设置定位监听回调
        baiduMapLifecycle.setBdLocationListener(locationListener);
        //加入lifecycle自动管理生命周期
        getLifecycle().addObserver(baiduMapLifecycle);
        mBaiduMap.setOnMarkerClickListener(arg0 -> {
            projectDetailInfo(arg0.getZIndex());
            return false;
        });
    }

    /**
     * 如果用户永久拒绝了，就要打开
     */
    @PermissionDenied
    private void shouldOpenLocation(int requestCode) {
        if (requestCode == 1) {
            LSAlert.showDialog(this, "提示", "地图功能需要定位权限，请在系统设置中打开权限！", "去设置", "不设置",
                    () -> PermissionSettingPage.start(ProjectListActivity.this, true));
        }
    }

    @Override
    protected void initData() {
        listAdapter = new FoundationPileListAdapter();
        listView.setAdapter(listAdapter);
        getProjectList();
    }

    /**
     * 定位SDK监听函数
     */
    public class ProjectListMapLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            double x = location.getLatitude();
            double y = location.getLongitude();
            myLocation = new LatLng(x, y);
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
    }

    /**
     * 获取工程信息
     */
    private void getProjectList() {
        if (memberCode == null || hour == null || hour.isEmpty()) {
            return;
        }
        LSAlert.showProgressHud(this);
        HashMap<String, Object> params = new HashMap<>();
        String token = SPUtil.getSPLSUtil("Token", "");
        params.put("Token", token);
        params.put("Hour", Integer.valueOf(hour));
        params.put("MemberCode", memberCode);
        XSRetrofit.getInstance().getService(CommonApiService.class)
                .getFoundationPile(GsonUtil.mapToJsonStr(params))
                .compose(XSRetrofit.<XSResultData<List<FoundationPileBean>>, List<FoundationPileBean>>applyTransformer())
                .subscribe(new ResponseObserver<List<FoundationPileBean>>() {
                    @Override
                    public void success(List<FoundationPileBean> foundationPileBeans) {
                        LSAlert.dismissProgressHud();
                        mBaiduMap.clear();
                        for (int i = 0; i < foundationPileBeans.size(); i++) {
                            FoundationPileBean bean = foundationPileBeans.get(i);
                            if (bean.isMap()) {
                                makeMarkListAtMap(bean, i);
                            }
                        }
                        listAdapter.setNewData(foundationPileBeans);
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        if (throwable.code == 1000) {
                            mBaiduMap.clear();
                            listAdapter.clearData();
                        }
                        LSAlert.dismissProgressHud();
                        ToastUtil.showShort(throwable.message);
                    }
                });
    }

    /**
     * 地图上标注工程坐标
     * @param foundationPileBean 工程对象
     * @param position 当前项的索引
     */
    private void makeMarkListAtMap(FoundationPileBean foundationPileBean, int position) {
        if (position == 0) {
            //设定中心点坐标
            LatLng centerLat = new LatLng(foundationPileBean.getX(), foundationPileBean.getY());
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder().target(centerLat).build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }
        //定义Maker坐标点
        LatLng point = new LatLng(foundationPileBean.getX(), foundationPileBean.getY());
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).zIndex(position).icon(bitmap);
        //在地图上添加Marker，并显示
        Marker marker = (Marker) mBaiduMap.addOverlay(option);
    }

    @OnItemClick(R.id.listView)
    public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
        projectDetailInfo(position);
    }

    /**
     * 显示工程的详细信息
     *
     * @param position 当前点击的索引
     */
    private void projectDetailInfo(int position) {
        FoundationPileBean item = (FoundationPileBean) listAdapter.getItem(position);
        LSAlert.showDialog(ProjectListActivity.this, "工程信息", item.getStakeName(),
                "导航", "取消", () -> {
                    double x = item.getX();
                    double y = item.getY();
                    LatLng endLat = new LatLng(x, y);
                    MapUtil.navigateByLocation(this, myLocation, endLat);
                });
    }

    /**
     * 查看地图点击事件
     */
    @OnClick(R.id.mapButton)
    protected void mapButtonDidClick() {
        mMapView.setVisibility(View.VISIBLE);
        llMapButton.setBackgroundResource(R.drawable.juxing);
        tvMapText.setTextColor(this.getResources().getColor(R.color.white));
        llProjectButton.setBackgroundResource(R.drawable.yuanjiaojuxing);
        tvProjectText.setTextColor(this.getResources().getColor(R.color.textcolor));
        ivMap.setBackgroundResource(R.drawable.ditu1);
        ivProject.setBackgroundResource(R.drawable.zuijin);
        listView.setVisibility(View.GONE);
    }

    /**
     * 查看列表的点击事件
     */
    @OnClick(R.id.projectButton)
    protected void projectButtonDidClick() {
        mMapView.setVisibility(View.GONE);
        llMapButton.setBackgroundResource(R.drawable.yuanjiaojuxing);
        tvMapText.setTextColor(this.getResources().getColor(R.color.textcolor));
        llProjectButton.setBackgroundResource(R.drawable.juxing);
        tvProjectText.setTextColor(this.getResources().getColor(R.color.white));
        ivMap.setBackgroundResource(R.drawable.ditu);
        ivProject.setBackgroundResource(R.drawable.zuijin1);
        listView.setVisibility(View.VISIBLE);
    }

    /**
     * 按会员名称，时效时间查询工程
     */
    public void searchButtonDidClick() {
        Intent intent = new Intent(this, ProjectSearchActivity.class);
        startActivityForResult(intent, SEARCH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
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

    public void menuButtonDidClick() {
        menu.toggle();
    }
}
