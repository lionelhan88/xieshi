package com.lessu.uikit.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListAdapter;

import com.lessu.uikit.R;

/**
 * Created by lessu on 14-7-7.
 */
public class LSAlert {
    public interface AlertCallback{
        void onConfirm();
    }
    public interface DialogCallback{
        void onConfirm();
        void onCancel();
    }
    public interface SelectItemCallback{
        void selectItem(int position);
        default void onCancel(){

        }
    }

    public static void showAlert(Context context,String detail) {
        LSAlert.showAlert(context,"提示",detail,"确认",null);
    }
    public static void showAlert(Context context,String title,String detail) {
        LSAlert.showAlert(context,title,detail,"确认",null);
    }
    public static void showAlert(Context context,String title,String detail,String confirmButtonTitle, final AlertCallback callback){
        showAlert(context,title,detail,confirmButtonTitle,true,callback);
    }
    public static void showAlert(Context context,String title,String detail,String confirmButtonTitle,boolean isTouchCancel ,final AlertCallback callback){
        AlertDialog alertDialog = new AlertDialog.Builder(context).
                setTitle(title).
                setMessage(detail)
                .setCancelable(isTouchCancel)
                .setPositiveButton(confirmButtonTitle, (dialogInterface, i) -> {
                    if (callback!=null) {
                        callback.onConfirm();
                    }
                }).create();
        alertDialog.show();
        setButtonColor(context,alertDialog);
    }
    public static void showDialog(Context context,String title,String detail,String confirmButtonTitle, String cancelButtonTitle,final DialogCallback callback){
        Dialog alertDialog = new AlertDialog.Builder(context).
                setTitle(title).
                setMessage(detail).
                setPositiveButton(confirmButtonTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (callback!=null) {
                            callback.onConfirm();
                        }
                    }
                })
                .setNegativeButton(cancelButtonTitle,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (callback!=null) {
                            callback.onCancel();
                        }
                    }
                })
                .create();
        alertDialog.show();
    }

    public static void showAlert(Context context, String title, View view, String confirmButtonTitle, final AlertCallback callback){
        AlertDialog alertDialog = new AlertDialog.Builder(context).
                setTitle(title).
                setView(view).
                setPositiveButton(confirmButtonTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (callback!=null) {
                            callback.onConfirm();
                        }
                    }
                })
                .create();
        alertDialog.show();
        setButtonColor(context,alertDialog);
    }
    /**
     * 背景透明的dialog
     * @param context
     * @param title
     * @param view
     * @param confirmButtonTitle
     * @param callback
     */
    public static void showTransparentDialog(Context context, String title, View view, String confirmButtonTitle, final AlertCallback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context).
                setTitle(title).
                setView(view);
        if(callback!=null){
            builder.setPositiveButton(confirmButtonTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onConfirm();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * 弹出式菜单选择列表
     * @param context
     * @param title
     * @param adapter
     * @param cancelButtonText
     * @param callback
     */
    public static void showDialog(Context context, String title, @DrawableRes int iconId, ListAdapter adapter, String cancelButtonText, final SelectItemCallback callback){
        AlertDialog alertDialog = new AlertDialog.Builder(context).
                setTitle(title).
                setIcon(iconId).
                setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.selectItem(which);
                    }
                })
                .setNegativeButton(cancelButtonText,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onCancel();
                    }
                }).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attributes);
    }
    /**
     * 弹出式菜单选择列表
     * @param context
     * @param title
     * @param arr
     * @param cancelButtonText
     * @param callback
     */
    public static void showDialogSingleChoice(Context context, String title, @DrawableRes int iconId, String[] arr, String cancelButtonText, final SelectItemCallback callback){
        AlertDialog alertDialog = new AlertDialog.Builder(context).
                setTitle(title).
                setIcon(iconId).
                setItems(arr,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.selectItem(which);
                    }
                })
                .setPositiveButton(cancelButtonText, (dialogInterface, i) -> callback.onCancel()).create();
        alertDialog.show();
        setButtonColor(context,alertDialog);
    }

    private static void setButtonColor(Context context,AlertDialog alertDialog){
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.blue_normal2));
    }


    public static ProgressDialog progressDialog;
    public static void setProgressDialog(ProgressDialog progress){
        progressDialog = progress;
    }

    public static int progressHudCount = 0 ;
    protected static List<String> progressHudText = new ArrayList<String>();
    public static void showProgressHud(Context context,String text){
    	if (progressHudCount==0){
    		if(progressDialog != null){
                progressDialog.dismiss();
            }
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setInverseBackgroundForced(false);
            progressDialog.setTitle(text);

            progressDialog.show();
            
            progressHudText.add(text);
            progressHudCount++;
    	} else{
    		progressDialog.setTitle(text);
    		progressHudText.add(text);
    		progressHudCount++;
    	}
        
    }

    public static void dismissProgressHud() {
        if (progressHudCount == 0) return;
        if (progressHudCount == 1) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
                progressHudText.remove(progressHudText.size() - 1);
                progressHudCount--;
            }
        } else {
            String text = progressHudText.get(progressHudText.size() - 1);
            progressDialog.setTitle(text);

            progressHudText.remove(progressHudText.size() - 1);
            progressHudCount--;
        }

    }
}
