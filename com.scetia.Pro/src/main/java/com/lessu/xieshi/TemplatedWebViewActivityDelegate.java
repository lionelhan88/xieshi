package com.lessu.xieshi;

import java.util.Map;

import android.webkit.WebView;

public  interface TemplatedWebViewActivityDelegate{
	public WebView getWebView();
	public String  onStringReplacementForText(String text,Object data);
	public boolean onReceiveEvent(String event,Map<String, String> params);
}