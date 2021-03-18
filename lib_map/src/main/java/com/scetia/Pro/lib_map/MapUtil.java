package com.scetia.Pro.lib_map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;

/**
 * created by Lollipop
 * on 2021/3/17
 */
public class MapUtil {
    /**
     * 打开百度地图导航
     * @param context
     * @param startLat 开始坐标
     * @param endLat 终点坐标
     */
    public static void navigateByLocation(final Context context,LatLng startLat, LatLng endLat){
        if(startLat==null){
            Toast.makeText(context,"获取当前位置失败！",Toast.LENGTH_SHORT).show();
            return;
        }
        // 构建 导航参数
        RouteParaOption paraOption = new RouteParaOption();
        paraOption.startPoint(startLat);
        paraOption.startName("从这里开始");
        paraOption.endPoint(endLat);
        paraOption.endName("到这里结束");
        BaiduMapNavigation.setSupportWebNavi(true);
        try {
            boolean isOpen = BaiduMapRoutePlan.openBaiduMapTransitRoute(paraOption, context);
            if (!isOpen) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        //调起结束时及时调用finish方法以释放相关资源
        BaiduMapRoutePlan.finish(context);
    }
    /**
     * 打开地图导航
     *
     * @param startLongitude 起点经度
     * @param startLatitude  起点纬度
     * @param endLongitude   终点经度
     * @param endLatitude    终点纬度
     */
    public static void navigateByLocation(final Context context, double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        LatLng startLat = new LatLng(startLatitude, startLongitude);
        LatLng endLat = new LatLng(endLatitude, endLongitude);
        navigateByLocation(context,startLat,endLat);
    }
}
