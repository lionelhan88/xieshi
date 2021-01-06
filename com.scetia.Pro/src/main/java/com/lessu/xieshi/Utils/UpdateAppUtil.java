package com.lessu.xieshi.Utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.good.permission.annotation.PermissionNeed;
import com.good.permission.util.PermissionUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;

import java.util.HashMap;

import constant.UiType;
import listener.OnInitUiListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

/**
 * created by ljs
 * on 2020/11/20
 */
public class UpdateAppUtil {

    /**
     * 检查app是否需要更新
     */
    public static void checkUpdateApp(Context context,boolean isShowDialog){
        HashMap<String, Object> updateparams = new HashMap<>();
        updateparams.put("PlatformType", "1");//1为安卓
        updateparams.put("SystemType", "2");//2为内部版
        EasyAPI.apiConnectionAsync(context, isShowDialog, false, ApiMethodDescription.get("/ServiceUST.asmx/GetAppVersion"), updateparams,
                new EasyAPI.ApiFastSuccessCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        JsonObject json = result.getAsJsonObject().get("Data").getAsJsonArray().get(0).getAsJsonObject();
                        //服务器app版本号
                        final String serviceVersion = json.get("Version").getAsString();
                        boolean isUpdate =isShouldUpdate(context,serviceVersion);
                        if (isUpdate) {
                            //TODO:如果需要更新，弹出更新提示
                            final String urlString = json.get("Update_Url").getAsString();
                            //是否强制更新标识
                            final boolean isMustBeUpdate = json.get("Update_Flag").getAsInt()==1;
                            String description = "";
                            if (isMustBeUpdate) {
                                //TODO:弹出强制跟新弹窗，用户点击取消就退出app
                                description = "更新内容:\r\n" + json.get("Description").getAsString()
                                        + "\r\n此更新为强制更新，必须更新后尚可继续使用！" + " 是否立即前往更新？";
                            } else {
                                description = "更新内容:\r\n" + json.get("Description").getAsString() + "是否立即前往更新？";
                            }
                            showUpdateDialog(context,serviceVersion, description, urlString, isMustBeUpdate);
                        } else {
                            if (isShowDialog) {
                                LSAlert.showAlert(context, "已经是最新版本了");
                            }
                        }
                    }
                });
    }

    /**
     * 比较两个app版本号
     * @param serverVersion 服务器返回的app版本号,是否需要更新
     */
    private static boolean isShouldUpdate(Context context,String serverVersion) {
        boolean updateFlag = false;
        try {
            final String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            //本地的版本号
            String[] localVersionArray = versionName.split("\\.");
            String[] serviceVersionArray = serverVersion.split("\\.");
            int localCount = localVersionArray.length;
            int serviceCount = serviceVersionArray.length;
            int count = localCount;
            if (localCount > serviceCount) {
                count = serviceCount;
            }
            for (int i = 0; i < count; i++) {
                if (Integer.parseInt(localVersionArray[i]) < Integer.parseInt(serviceVersionArray[i])) {
                    updateFlag = true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
      return updateFlag;
    }

  /**
     * 是否显示强制更新提示框
     * @param version
     * @param description
     * @param apkUrl
     * @param isMustBeUpdate*/
    private static void showUpdateDialog(Context context, String version, String description, String apkUrl,
                                 final boolean isMustBeUpdate){
        //下载配置
        UpdateConfig updateConfig = new UpdateConfig();
        //是否强制更新
        updateConfig.setForce(isMustBeUpdate);
        //是否检测wifi状态
        updateConfig.setCheckWifi(false);
        //是否在通知栏显示
        updateConfig.setShowNotification(false);
        //显示下载进度
        updateConfig.setAlwaysShowDownLoadDialog(true);
        //设置保存文件的路径
        updateConfig.setApkSavePath(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath());
        //设置保存文件的名称
        updateConfig.setApkSaveName(context.getPackageName()+"V"+version);
        //UI界面配置
        UiConfig uiConfig = new UiConfig();
        uiConfig.setUiType(UiType.SIMPLE);

        UpdateAppUtils.getInstance().apkUrl(apkUrl)
                .updateTitle("发现新版本V"+version)
                .updateContent(description)
                .uiConfig(uiConfig)
                .updateConfig(updateConfig)
                .update();
    }


}
