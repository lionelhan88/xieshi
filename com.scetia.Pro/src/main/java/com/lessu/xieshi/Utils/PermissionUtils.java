package com.lessu.xieshi.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.lessu.uikit.views.LSAlert;

import java.util.List;

public class PermissionUtils {
    public interface permissionResult{
        void hasPermission(List<String> granted, boolean isAll);
    }
    public static void requestPermission(final Activity context, final permissionResult permissionResult, String... permissions){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&!XXPermissions.isHasPermission(context,permissions)){
                XXPermissions.with(context).permission(permissions).request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        permissionResult.hasPermission(granted,isAll);
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if(quick){
                            LSAlert.showDialog(context, "提示", "使用此功能需要设置权限", "去设置",
                                    "取消", new LSAlert.DialogCallback() {
                                        @Override
                                        public void onConfirm() {
                                            XXPermissions.gotoPermissionSettings(context,true);
                                        }

                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                        }
                    }
                });
        }else{
            //android小于6.0或者已经授权过的执行此方法
            permissionResult.hasPermission(null,true);
        }
    }
}
