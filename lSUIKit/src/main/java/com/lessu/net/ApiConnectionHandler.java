package com.lessu.net;

import com.lessu.foundation.RegKit;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

class ApiConnectionHandler extends AsyncHttpResponseHandler{
    @Override
    public void onSuccess(int i, Header[] headers, byte[] bytes) {
        String content = new String(bytes);
        //cc_edit:
        //协实项目接口返回解析 
        //2015年1月13日20:59:37
        	String tempString = RegKit.match(content, ">\\{.+\\}</");
        	if (tempString != null){
            	tempString = tempString.replace(">{", "{");
            	tempString = tempString.replace("}</", "}");
        		content = tempString;
        	}
        System.out.println("李李李李。。。。"+content);
        getCallback().onSuccess(content);
    }

    @Override
    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        if (bytes!=null) {
            String content = new String(bytes);
            getCallback().onFailure(throwable, content);
        }else {
            String content = "无法连接到网络，请先检查网络环境吧";
            getCallback().onFailure(throwable, content);
        }
    }

    public interface ApiConnectionHandlerInterface{
		 void onSuccess(String content);
		 void onFailure(Throwable error, String content);
	}
	
	private ApiConnectionHandlerInterface callback;
	

	public ApiConnectionHandlerInterface getCallback() {
		return callback;
	}
	public void setCallback(ApiConnectionHandlerInterface callback) {
		this.callback = callback;
	}
	
	
}