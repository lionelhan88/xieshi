package com.lessu.net;

import android.content.Context;

import com.google.gson.JsonElement;
import com.lessu.uikit.R;
import com.lessu.uikit.views.LSAlert;

import java.util.HashMap;

/**
 * Created by lessu on 14-7-7.
 */
public class EasyAPI {
    public interface ApiFastSuccessCallBack{
         void onSuccessJson(JsonElement result);
    }
    public interface ApiFastSuccessFailedCallBack{

         void onSuccessJson(JsonElement result);
         String onFailed(ApiError error);

    }
    public static void apiConnectionAsync(final Context context,final boolean indicator,boolean useCache,ApiMethodDescription method,HashMap<String,Object> params, final ApiFastSuccessFailedCallBack callBack) {
        ApiConnection connection = ApiBase.sharedInstance().getConnectionWithApiMethod(method,params);
        if( indicator ){
        	LSAlert.showProgressHud(context,"请稍候");
        }
        connection.setCallbacks(new ApiConnection.ApiConnectionCallback() {

            @Override
            public void onSuccessJson(JsonElement result) {
                super.onSuccessJson(result);
                callBack.onSuccessJson(result);
                if( indicator ){
                	LSAlert.dismissProgressHud();
                }
            }

            @Override
            public void onFailed(ApiError error) {
                super.onFailed(error);
                if( indicator ){
                	LSAlert.dismissProgressHud();
                }
                String result = callBack.onFailed(error);
                if (result != null) {
                	if( indicator ){
                		LSAlert.showAlert(context, context.getString( R.string.api_connection_failed_error ), result , "确定", null);
                	}
                }
            }

        });
        connection.startAsynchronous();
    }
    
    public static void apiConnectionAsync(final Context context,boolean indicator,boolean useCache,ApiMethodDescription method,HashMap<String,Object> params, final ApiFastSuccessCallBack callBack){
        EasyAPI.apiConnectionAsync(context,indicator,useCache,method,params,new ApiFastSuccessFailedCallBack() {
            @Override
            public void onSuccessJson(JsonElement result) {
                if (callBack!= null){
                    callBack.onSuccessJson(result);
                }
            }

            @Override
            public String onFailed(ApiError error) {
                return error.errorMeesage;
            }
        });
    }
}
