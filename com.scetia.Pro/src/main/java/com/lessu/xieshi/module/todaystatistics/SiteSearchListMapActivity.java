package com.lessu.xieshi.module.todaystatistics;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.good.permission.annotation.PermissionDenied;
import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionSettingPage;
import com.lessu.navigation.BarButtonItem;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.GsonUtil;
import com.lessu.xieshi.utils.ToastUtil;
import com.lessu.xieshi.base.XieShiSlidingMenuActivity;
import com.lessu.xieshi.bean.SiteSearchProject;
import com.lessu.xieshi.http.service.CommonApiService;
import com.lessu.xieshi.module.todaystatistics.adapter.SiteSearchListMapAdapter;
import com.lessu.xieshi.view.ZoomControlsView;
import com.scetia.Pro.baseapp.uitls.LogUtil;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.lib_map.BaiduMapLifecycle;
import com.scetia.Pro.lib_map.MapUtil;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.bean.XSResultData;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.manage.XSRetrofit;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class SiteSearchListMapActivity extends XieShiSlidingMenuActivity {
    private String projectName = "";
    private String projectArea = "";
    private String baogaobianhao = "";
    private String jianshedanwei = "";
    private String shigongdanwei = "";
    private String jianlidanwei = "";
    private String jiancedanwei = "";
    private LatLng myLocation;
    private Boolean isFirstLoc = true;
    private double latitude = 0.0;
    private double longitude = 0.0;
    @BindView(R.id.map_jinritongji)  MapView mapView;
    @BindView(R.id.lv_ditu)  ListView siteSearchListView;
    @BindView(R.id.ll_yeshu)  LinearLayout llNumberOfPages;
    @BindView(R.id.ll_daohang)  LinearLayout llNavigation;
    @BindView(R.id.ll_near_by_list)  LinearLayout llNearByList;
    @BindView(R.id.iv_ditu)  ImageView ivSiteMap;
    @BindView(R.id.iv_liebiao)  ImageView ivSiteList;
    @BindView(R.id.iv_xiala)  ImageView ivDropDown;
    @BindView(R.id.tv_ditu)  TextView tvSiteMap;
    @BindView(R.id.tv_liebiao)  TextView tvSiteList;
    @BindView(R.id.tv_daohangtitle)  TextView tvNavigationTitle;
    @BindView(R.id.tv_fujin)  TextView tvNearBy;
    @BindView(R.id.tv_yeshuxianshi)  TextView tvNumberOfPages;
    @BindView(R.id.tv_daohangaddress)  TextView tvNavigationAddress;
    @BindView(R.id.bt_shangyiye)  Button btnLastPage;
    @BindView(R.id.bt_xiayiye)  Button btnNextPage;
    @BindView(R.id.rl_map)  RelativeLayout rl_map;
    @BindView(R.id.rl_daohang)  RelativeLayout rlNavigation;
    @BindView(R.id.rl_xinxichaxun)  RelativeLayout rlInfoQuery;
    //表示范围：-1表示五范围，其他数据表示具体范围数值
    private int rangeIndex = 1;
    //是否时第一次进入页面
    private boolean isFirstInto = true;
    //当前列表总页数
    private int pageCount;
    private int currentPageNo=1;
    private int oldCurPageNo=1;
    private boolean rangeListIsOpen = false;
    private SiteSearchListMapAdapter listMapAdapter;
    private BaiduMap mBaiduMap;
    private String token;
    private List<SiteSearchProject.DataBean.ListContentBean> siteProjectItems;
    static {
        System.loadLibrary("locSDK7a");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.admin_today_statistics_activity;
    }

    @Override
    protected void initView() {
        setTitle("工地查询");
        mapView.showZoomControls(false);
        listMapAdapter = new SiteSearchListMapAdapter();
        siteSearchListView.setAdapter(listMapAdapter);
        BarButtonItem searchBtnItem = new BarButtonItem(this, R.drawable.shuaxin);
        searchBtnItem.setOnClickMethod(this, "searchButtonDidClick");
        navigationBar.setRightBarItem(searchBtnItem);
        initBaiduLocation();
    }

    @Override
    protected void initData() {
        token = Constants.User.GET_TOKEN();
    }

    /**
     * 初始化地图
     */
    @PermissionNeed(Manifest.permission.ACCESS_FINE_LOCATION)
    private void initBaiduLocation() {
        //获取地图map对象
        mBaiduMap = mapView.getMap();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //开是定位初始化
        BaiduMapLifecycle baiduMapLifecycle = new BaiduMapLifecycle(this,mapView);
        SiteSearchLocationListener locationListener = new SiteSearchLocationListener();
        //设置定位监听回调
        baiduMapLifecycle.setBdLocationListener(locationListener);
        //加入lifecycle自动管理生命周期
        getLifecycle().addObserver(baiduMapLifecycle);
        ZoomControlsView mapViewZoomControl = findViewById(R.id.zcv_zoom);
        mapViewZoomControl.setMapView(mapView);
        mBaiduMap.setOnMarkerClickListener(marker -> {
            final int index = marker.getZIndex();
            SiteSearchProject.DataBean.ListContentBean siteProjectItem = siteProjectItems.get(index);
            String title = siteProjectItem.getProjectName();
            String address = siteProjectItem.getProjectAddress();
            llNavigation.setVisibility(View.VISIBLE);
            tvNavigationTitle.setText(title);
            tvNavigationAddress.setText(address);
            rlNavigation.setOnClickListener(v -> navigationByBaiduMap(index));
            rlInfoQuery.setOnClickListener(v -> {
                llNavigation.setVisibility(View.GONE);
                Intent intent = new Intent(this, SiteDetailInfoSearchActivity.class);
                intent.putExtra("projectid", siteProjectItem.getProjectId());
                //工地名称
                intent.putExtra("projectName", siteProjectItem.getProjectName());
                //工地区县
                intent.putExtra("projectArea", siteProjectItem.getProjectRegion());
                startActivity(intent);
            });
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
                    () -> PermissionSettingPage.start(SiteSearchListMapActivity.this, true));
        }
    }

    /**
     * 带有范围的查询
     *
     * @param pageIndex 当前传入的页数
     * @param range     范围条件
     */
    private void getSiteProjectByRange(final int pageIndex, int range) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        params.put("Type", 1);
        params.put("ProjectName", projectName);
        params.put("ProjectArea", projectArea);
        params.put("BuildingReportNumber", baogaobianhao);
        params.put("BuildUnitName", shigongdanwei);
        params.put("ConstructUnitName", jianshedanwei);
        params.put("SuperviseUnitName", jianlidanwei);
        params.put("DetectionUnitName", jiancedanwei);
        if (range != -1) {
            //如果传入有效范围才加入这个参数
            //如果用户选择了范围不限，则不加入这个参数
            params.put("CurrentLocation", getMyself());
            params.put("DistanceRange", range);
        }
        params.put("PageSize", 10);
        params.put("CurrentPageNo", pageIndex);
        LSAlert.showProgressHud(this);
        XSRetrofit.getInstance().getService(CommonApiService.class)
                .getSiteProject(GsonUtil.mapToJsonStr(params))
                .compose(XSRetrofit.<XSResultData<SiteSearchProject.DataBean>, SiteSearchProject.DataBean>applyTransformer())
                .subscribe(new ResponseObserver<SiteSearchProject.DataBean>() {
                    @Override
                    public void success(SiteSearchProject.DataBean dataBean) {
                        LSAlert.dismissProgressHud();
                        mBaiduMap.clear();
                        List<SiteSearchProject.DataBean.ListContentBean> listContent = dataBean.getListContent();
                        siteProjectItems = listContent;
                        makeMarkListAtMap(listContent);
                        listMapAdapter.setNewData(listContent);
                        currentPageNo = dataBean.getCurrentPageNo();
                        oldCurPageNo = currentPageNo;
                        pageCount = dataBean.getPageCount();
                        tvNumberOfPages.setText(currentPageNo + "/" + pageCount);
                        if (currentPageNo == 1) {
                            btnLastPage.setTextColor(ContextCompat.getColor(SiteSearchListMapActivity.this,R.color.site_search_list_map_btn_page_disable));
                            btnLastPage.setClickable(false);
                        } else {
                            btnLastPage.setTextColor(ContextCompat.getColor(SiteSearchListMapActivity.this,R.color.site_search_list_map_btn_page_enable));
                            btnLastPage.setClickable(true);
                        }
                        if (currentPageNo ==pageCount) {
                            btnNextPage.setTextColor(ContextCompat.getColor(SiteSearchListMapActivity.this,R.color.site_search_list_map_btn_page_disable));
                            btnNextPage.setClickable(false);
                        } else {
                            btnNextPage.setTextColor(ContextCompat.getColor(SiteSearchListMapActivity.this,R.color.site_search_list_map_btn_page_enable));
                            btnNextPage.setClickable(true);
                        }
                    }

                    @Override
                    public void failure(ExceptionHandle.ResponseThrowable throwable) {
                        if(throwable.code==2000){
                            //查询成功，没有数据
                            pageCount = 0;
                            currentPageNo=0;
                            oldCurPageNo=0;
                            notData();
                        }
                        //网络连接失败返回前一页
                        if(currentPageNo>oldCurPageNo){
                            currentPageNo--;
                        }else  if(currentPageNo<oldCurPageNo){
                            currentPageNo++;
                        }
                        LSAlert.dismissProgressHud();
                        ToastUtil.showShort(throwable.message);
                    }
                });
    }

    /**
     * 地图上标注工程坐标
     */
    private void makeMarkListAtMap(List<SiteSearchProject.DataBean.ListContentBean> siteProjectList) {
        for (int i = 0; i < siteProjectList.size(); i++) {
            String projectCoordinates = siteProjectList.get(i).getProjectCoordinates();
            if(!TextUtils.isEmpty(projectCoordinates)){
                //因为没有经纬度信息，附近几公里的功能失效，用户点了没反应！！加载进去后主界面地图空白！！！
                String[] split = projectCoordinates.split(",");
                double x = Double.parseDouble(split[1]);
                double y = Double.parseDouble(split[0]);
                if (i == 0) {
                    //设定中心点坐标
                    LatLng centerPoint = new LatLng(x, y);
                    //定义地图状态
                    MapStatus mMapStatus = new MapStatus.Builder().target(centerPoint).build();
                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    //改变地图状态
                    mBaiduMap.setMapStatus(mMapStatusUpdate);
                }
                //定义Maker坐标点
                LatLng point = new LatLng(x, y);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(point).zIndex(i).icon(bitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);
            }
        }
    }

    /**
     * 如果获取不到数据
     */
    private void notData() {
        mBaiduMap.clear();
        listMapAdapter.clear();
        tvNumberOfPages.setText(currentPageNo + "/" + pageCount);
        btnLastPage.setTextColor(0xff8f8f8f);
        btnLastPage.setClickable(false);
        btnNextPage.setTextColor(0xff8f8f8f);
        btnNextPage.setClickable(false);
    }

    @OnClick({R.id.ll_ditu,R.id.ll_liebiao,R.id.bt_shangyiye,R.id.bt_xiayiye,R.id.bt_sousuo,R.id.tv_1,R.id.tv_2
    ,R.id.tv_3,R.id.tv_max,R.id.ll_spinner})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_ditu:
                ivSiteMap.setImageResource(R.drawable.ditu1a);
                tvSiteMap.setTextColor(0xff118bca);
                ivSiteList.setImageResource(R.drawable.liebiao);
                tvSiteList.setTextColor(0xff9b9cae);
                rl_map.setVisibility(View.VISIBLE);
                siteSearchListView.setVisibility(View.GONE);
                llNumberOfPages.setVisibility(View.GONE);
                break;
            case R.id.ll_liebiao:
                ivSiteMap.setImageResource(R.drawable.ditua);
                tvSiteMap.setTextColor(0xff9b9cae);
                ivSiteList.setImageResource(R.drawable.liebiao1);
                tvSiteList.setTextColor(0xff118bca);
                rl_map.setVisibility(View.GONE);
                siteSearchListView.setVisibility(View.VISIBLE);
                llNumberOfPages.setVisibility(View.VISIBLE);
                ivDropDown.setBackgroundResource(R.drawable.xialaa);
                llNearByList.setVisibility(View.GONE);
                rangeListIsOpen = false;
                break;
            case R.id.bt_shangyiye:
                getSiteProjectByRange(--currentPageNo, rangeIndex);
                break;
            case R.id.bt_xiayiye:
                getSiteProjectByRange(++currentPageNo, rangeIndex);
                break;
            case R.id.bt_sousuo:
                //点击了地图上的查询按钮，进入查询页面
                ivDropDown.setBackgroundResource(R.drawable.xialaa);
                llNearByList.setVisibility(View.GONE);
                rangeListIsOpen = false;
                Intent intent = new Intent(SiteSearchListMapActivity.this, SiteSearchByConditionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ProjectArea", projectArea);
                bundle.putString("ProjectName", projectName);
                bundle.putString("baogaobianhao", baogaobianhao);
                bundle.putString("jianshedanwei", jianshedanwei);
                bundle.putString("shigongdanwei", shigongdanwei);
                bundle.putString("jianlidanwei", jianlidanwei);
                bundle.putString("jiancedanwei", jiancedanwei);
                bundle.putInt("range", rangeIndex);
                bundle.putString("CurrentLocation", getMyself());
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_1:
                //点击了附近1公里
                tvNearBy.setText("附近:1公里");
                ivDropDown.setBackgroundResource(R.drawable.xialaa);
                llNearByList.setVisibility(View.GONE);
                rangeListIsOpen = false;
                rangeIndex = 1;
                getSiteProjectByRange(1, rangeIndex);
                break;
            case R.id.tv_2:
                //修改 2018-10-23 由公里改为2公里
                tvNearBy.setText("附近:2公里");
                ivDropDown.setBackgroundResource(R.drawable.xialaa);
                llNearByList.setVisibility(View.GONE);
                rangeListIsOpen = false;
                rangeIndex = 2;
                getSiteProjectByRange(1, rangeIndex);
                break;
            case R.id.tv_3:
                //修改 2018-10-23 由5公里改为3公里
                tvNearBy.setText("附近:3公里");
                ivDropDown.setBackgroundResource(R.drawable.xialaa);
                llNearByList.setVisibility(View.GONE);
                rangeListIsOpen = false;
                rangeIndex = 3;
                getSiteProjectByRange(1, rangeIndex);
                break;
            case R.id.tv_max:
                tvNearBy.setText("附近:不限");
                ivDropDown.setBackgroundResource(R.drawable.xialaa);
                llNearByList.setVisibility(View.GONE);
                rangeListIsOpen = false;
                rangeIndex = -1;
                getSiteProjectByRange(1, rangeIndex);
                break;
        }
    }

    @OnClick(R.id.ll_spinner)
    public void rangeListDidClick(){
        if (!rangeListIsOpen) {
            ivDropDown.setBackgroundResource(R.drawable.xiala1);
            llNearByList.setVisibility(View.VISIBLE);
        } else {
            ivDropDown.setBackgroundResource(R.drawable.xialaa);
            llNearByList.setVisibility(View.GONE);
        }
        rangeListIsOpen = !rangeListIsOpen;
    }

    /**
     * listView item点击事件
     * @param adapter
     * @param cell
     * @param position 点击项的下标索引
     * @param id
     */
    @OnItemClick(R.id.lv_ditu)
    public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
        String projectId = siteProjectItems.get(position).getProjectId();
        Intent intent = new Intent(SiteSearchListMapActivity.this, SiteDetailInfoSearchActivity.class);
        //工地名称
        intent.putExtra(Constants.Site.KEY_PROJECT_ID, projectId);
        intent.putExtra(Constants.Site.KEY_PROJECT_NAME, siteProjectItems.get(position).getProjectName());
        //工地区县
        intent.putExtra(Constants.Site.KEY_PROJECT_AREA, siteProjectItems.get(position).getProjectRegion());
        startActivity(intent);
    }

    /**
     * 点击右上角的刷新按钮，目前刷新操作 ===>加载下一页
     * 刷新下一页
     */
    public void searchButtonDidClick() {
        if (pageCount == 1||(currentPageNo==pageCount&&currentPageNo!=0)) {
            ToastUtil.showShort("当前没有更多新内容!");
            return;
        }
        getSiteProjectByRange(++currentPageNo, rangeIndex);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg2 != null) {
            Bundle bundle = arg2.getExtras();
            projectArea = bundle.getString("ProjectArea");
            projectName = bundle.getString("ProjectName");
            baogaobianhao = bundle.getString("baogaobianhao");
            jianshedanwei = bundle.getString("jianshedanwei");
            shigongdanwei = bundle.getString("shigongdanwei");
            jianlidanwei = bundle.getString("jianlidanwei");
            jiancedanwei = bundle.getString("jiancedanwei");
            tvNearBy.setText("附近:不限");
            rangeIndex = -1;
            currentPageNo = 1;
            ivDropDown.setBackgroundResource(R.drawable.xialaa);
            llNearByList.setVisibility(View.GONE);
            getSiteProjectByRange(currentPageNo, rangeIndex);
        }
    }

    @Override
    protected void onDestroy() {
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    /**
     * 获取当前位置的坐标
     *
     * @return
     */
    private String getMyself() {
        if (myLocation != null) {
            latitude = myLocation.latitude;
            longitude = myLocation.longitude;
        }else if (!isFirstInto) {
            ToastUtil.showShort("请点右上方刷新一下");
        }
        DecimalFormat df = new DecimalFormat("#.000000");
        String dx = df.format(latitude);
        String dy = df.format(longitude);
        System.out.println(dy + "," + dx);
        return dy + "," + dx;
    }

    /**
     * 点击到了地图页面上的“导航”按钮，去导航路线
     * @param position 点击项的索引
     */
    private void navigationByBaiduMap(int position) {
        String projectCoordinates = siteProjectItems.get(position).getProjectCoordinates();
        String[] split = projectCoordinates.split(",");
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[0]);
        LatLng endLat = new LatLng(x, y);
        MapUtil.navigateByLocation(this,myLocation,endLat);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            int[] xy = {0, 0};
            llNavigation.getLocationInWindow(xy);
            int left = xy[0], top = xy[1];
            int bottom = top + llNavigation.getHeight();
            int right = left + llNavigation.getWidth();
            if (!(ev.getX() > left) || !(ev.getX() < right)
                    || !(ev.getY() > top) || !(ev.getY() < bottom)) {
                        llNavigation.setVisibility(View.GONE);
                    }

        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 定位SDK监听函数
     */
    public class SiteSearchLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null)
                return;
            double x = location.getLatitude();
            double y = location.getLongitude();
            myLocation = new LatLng(x, y);
            LogUtil.showLogD("我的位置====>" + myLocation + "," + location.getLocType());
            if (isFirstInto) {
                getSiteProjectByRange(1, rangeIndex);
                isFirstInto = false;
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
        }
    }
}