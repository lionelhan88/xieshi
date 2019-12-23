package com.lessu.uikit.views;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

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

//    public static Context sharedContext;
//    public static void init(Context context){
//        sharedContext = context;
//    }

//    public static Context getSharedContext() {
//        if (sharedContext == null){
//
//        }
//        return sharedContext;
//    }
    public static void showAlert(Context context,String detail) {
        LSAlert.showAlert(context,"提示",detail,"确认",null);
    }
    public static void showAlert(Context context,String title,String detail) {
        LSAlert.showAlert(context,title,detail,"确认",null);
    }
    public static void showAlert(Context context,String title,String detail,String confirmButtonTitle, final AlertCallback callback){
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
                .create();
        alertDialog.show();
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
    	}
    	else{
    		progressDialog.setTitle(text);
    		progressHudText.add(text);
    		progressHudCount++;
    	}
        
    }
    public static void dismissProgressHud(){
    	if (progressHudCount==0)     	return;
    	if (progressHudCount==1){
	        if (progressDialog != null) {
	            progressDialog.dismiss();
	        }
	        progressDialog = null;
	        progressHudText.remove(progressHudText.size()-1);
	        progressHudCount--;
    	}
    	else{
    		String text = progressHudText.get(progressHudText.size()-1);
    		progressDialog.setTitle(text);

    		progressHudText.remove(progressHudText.size()-1);
    		progressHudCount--;
    	}

    }
}
