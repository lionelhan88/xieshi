package com.lessu.net;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lessu.foundation.Validate;
import com.lessu.net.ApiConnectionHandler.ApiConnectionHandlerInterface;
import com.lessu.uikit.easy.EasyCollection;
import com.lessu.uikit.easy.EasyCollection.OnMapEnumlateInterface;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiConnection  implements ApiConnectionHandlerInterface {
	public static String DefaultStandardSuccessKey    = "success";
	public static String DefaultStandardDataKey       = "data";
	public static String DefaultStandardMessageKey    = "message";
	public static String DefaultStandardErrorCodeKey  = "errorcode";

	public String StandardSuccessKey    = "";
	public String StandardDataKey       = "";
	public String StandardMessageKey    = "";
	public String StandardErrorCodeKey  = "";

	protected String                urlString;

	protected Map<String,String>         params;
	protected String 				soapAction;
	protected String				soapUrl;
	protected String requestMethod = "get";
	private ResultType resultType = ResultType.Plain;

	private ApiConnectionCallback callbacks;
	protected ApiConnectionHandler connectionHandler;
	private ApiConnectionPreprocess preprocessor;
	protected RequestHandle requestHandle;

	protected static AsyncHttpClient sharedClient;
	public static AsyncHttpClient getSharedClient() {
		if (sharedClient == null){
			sharedClient = new AsyncHttpClient();
			//2020-07-23 设置超时时间60s，防止有些接口服务响应时间太长提示“超时”
			sharedClient.setTimeout(60*1000);
		}
		return sharedClient;
	}

	public ApiConnection(String urlString){
		super();
		this.urlString = urlString;
		connectionHandler = new ApiConnectionHandler();
		connectionHandler.setCallback(this);
		params = new HashMap<String, String>();
		StandardSuccessKey  = DefaultStandardSuccessKey;
		StandardDataKey		= DefaultStandardDataKey;
		StandardMessageKey	= DefaultStandardMessageKey;
		StandardErrorCodeKey= DefaultStandardErrorCodeKey;
	}

	public void startAsynchronous(){
		Log.d("api_connection:" ,urlString);
		if ( requestMethod.equalsIgnoreCase("GET")) {
			if(Validate.mapNotEmpty(params)){
				//cc_edit  xieshi
				Map<String, String> xieshiMap = new HashMap<String, String>();
				Gson gson = new Gson();
				String paramJsonString = gson.toJson(params);
				xieshiMap.put("param", paramJsonString);
				requestHandle = getSharedClient().get(urlString, new RequestParams(xieshiMap), connectionHandler);
				Log.d("api_params:",xieshiMap.toString());
			}else{
				requestHandle = getSharedClient().get(urlString, connectionHandler);
			}

		}else if (requestMethod.equalsIgnoreCase("POST") ) {
			if(Validate.mapNotEmpty(params)){
				Map<String, String> xieshiMap = new HashMap<String, String>();
				Gson gson = new Gson();
				String paramJsonString = gson.toJson(params);
				xieshiMap.put("param", paramJsonString);
				requestHandle = getSharedClient().post(urlString, new RequestParams(xieshiMap), connectionHandler);
				Log.d("api_params:"     ,xieshiMap.toString());
			}else{
				requestHandle = getSharedClient().post(urlString, connectionHandler);
			}
		}else if(requestMethod.equalsIgnoreCase("SOAP")){
			try {
				Header[] headers =  {
						new BasicHeader("SOAPAction"  , soapUrl+"/"+soapAction),
				};
				StringEntity entity;
				String soapBody = "<${action} xmlns=\"${url}\">${params}</${action}>";
				soapBody = soapBody.replace("${action}" , soapAction).replace("${url}", soapUrl);
				final StringBuffer soapParams = new StringBuffer();
				if(Validate.mapNotEmpty(params)){

					EasyCollection.enumlateMap(params, new OnMapEnumlateInterface() {

						@Override
						public boolean onEmulate(Object key, Object value,long index) {
							soapParams.append(String.format("<%s>%s</%s>", key,value,key));
							return true;
						}
					});
					soapBody = soapBody.replace("${params}", soapParams);

				}else{
					soapBody = soapBody.replace("${params}", "");
				}
				String soapString = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>%s</soap:Body></soap:Envelope>";
				soapString = String.format(soapString, soapBody);
				entity = new StringEntity(soapString);
				//entity.setContentType("text/xml; charset=utf-8");
				String contentType = "text/xml; charset=utf-8";
				requestHandle = getSharedClient().post(null, urlString, headers, entity , contentType, connectionHandler);

				//	                requestHandle = getSharedClient().post(urlString, connectionHandler);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

	}

	private Object proccessResult(String responseString) {
		if(requestMethod.equalsIgnoreCase("SOAP")){
			Pattern pattern = Pattern.compile(">(\\{.+\\})</");

			Matcher matcher = pattern.matcher(responseString);
			if(matcher.find()){
				responseString = matcher.group(1);
			}
		}
		switch (getResultType()) {
			case Standard:
			case Json:{
				JsonElement json = new JsonParser().parse(responseString);
				if (getPreprocessor() != null && json != null) {
					PreprocessResult result = getPreprocessor().preproccess(json);

					JsonObject output = new JsonObject();

					output.addProperty(StandardSuccessKey, result.success);

					if(result.data!=null){
						output.add(StandardDataKey, result.data);
					}
					if(result.message!=null){
						output.addProperty(StandardMessageKey, result.message);
					}

					return output;
				}
				return json;

			}
			case Plain:
			default:
				return responseString;
		}
	}

	private void connectionComplete(String responseString ,Object processedResult){
		if (resultType == ResultType.Standard || preprocessor!=null) {
			JsonObject result = (JsonObject) processedResult;
			System.out.println("result................."+result);
			try{
				if ( callbacks != null){
					callbacks.onSuccess(result);
					callbacks.onSuccessJson(result);
				}
				if ( callbacks != null){
					callbacks.onFinal();
				}
			}catch (Exception e) {
				Log.e("ApiConnection","callback Exception");
				int code = 0;
				ApiError error = new ApiError();
				error.errorDomain = ApiError.ApiResponseNotSuccessErrorDomain;
				error.errorCode   = code;
				error.errorMeesage= "系统内部出错:" + e.getLocalizedMessage();

				if ( callbacks != null){
					callbacks.onFailed(error);
					callbacks.onFinal();
				}

			}
		} else{
			if ( callbacks != null){
				this.onSuccess(responseString);
				callbacks.onFinal();
			}
		}
	}

	@Override
	public void onSuccess(String responseString) {
		Object result = null;
		try{
			result = this.proccessResult(responseString);
		}catch(Exception e){
			Log.e("ApiConnection", "can not process result" + responseString);
		}
		if (responseString == null) {
			ApiError error = new ApiError();
			error.errorDomain = ApiError.ApiServerResponseErrorDomain;
			error.errorCode   = 0;
			error.errorMeesage= "服务器发生错误，请稍后再试";

			if ( callbacks != null){
				try {
					callbacks.onFailed(error);
					callbacks.onFinal();
				}catch (Exception e){
					Log.e("ApiConnection","callback Exception");
				}
			}
		}else{
			this.connectionComplete(responseString, result);
		}

	}


	@Override
	public void onFailure(Throwable error, String content) {
		ApiError apiError = new ApiError();
		apiError.errorDomain = ApiError.ApiConnectionErrorDomain;
		apiError.errorCode   = 0;
		apiError.errorMeesage= content;

		if ( callbacks != null){
			callbacks.onFailed(apiError);
			callbacks.onFinal();
		}
	}

	public ApiConnectionPreprocess getPreprocessor() {
		return preprocessor;
	}

	public void setPreprocessor(ApiConnectionPreprocess preprocessor) {
		this.preprocessor = preprocessor;
	}

	public ApiConnectionCallback getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(ApiConnectionCallback callbacks) {
		this.callbacks = callbacks;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public Map<String, ?> getParams() {
		return params;
	}

	public void setParams(Map<String, ?> params) {
		final HashMap<String,String> newParams = new HashMap<String,String>();
		EasyCollection.enumlateMap(params,new EasyCollection.OnMapEnumlateInterface() {
			@Override
			public boolean onEmulate(Object key, Object value, long index) {
				if (value != null) {

					//这个地方byte转String能行？这么写有问题啊，不好改的
					newParams.put((String) key, value.toString());
				}
				return true;
			}
		});
		this.params = newParams;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public void cancel(boolean b) {
		requestHandle.cancel(b);
	}

	public static HashMap<String,Object> emptyParams(){
		return new HashMap<String, Object>();
	}
	public static class PreprocessResult{
		public boolean success;
		public JsonElement data;
		public String message;
	}

	public abstract static class ApiConnectionCallback{
		public void onSuccess(Object result){}
		public void onSuccessJson(JsonElement result){}
		//		public void onCacheSuccess(Object result){}
		public void onFailed(ApiError error){}
		public void onFinal(){}
	}
	public interface ApiConnectionPreprocess{
		public PreprocessResult preproccess(JsonElement input);
	}
	public enum ResultType{
		Plain,
		Json,
		Standard
	}

}
