package com.lessu.xieshi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.easy.EasyCollection;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public abstract class TemplatedWebViewActivity extends NavigationActivity
		implements TemplatedWebViewActivityDelegate {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// webView.requestFocus();
		// webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	}

	@Override
	protected void onStart() {
		WebView webView = getWebView();
		webView.getSettings().setJavaScriptEnabled(true);
		super.onStart();

		WebSettings webSettings = webView.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//		webSettings.setUseWideViewPort(true);
//		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		webView.setBackgroundColor(Color.WHITE);
		webView.getSettings().setDefaultTextEncodingName("utf-8");

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				try {
					String eventPrefix = "app://";
					url = url.replace(eventPrefix, "");
					String event;
					String[] urlContainer ;
					if (url.contains("?")) {
						urlContainer = url.split("\\?");
					}else{
						urlContainer = new String[0];
					}
					boolean result = false;
					
					HashMap<String, String> params = new HashMap<String, String>();
					if (urlContainer.length == 2) {
						event = urlContainer[0];
						String paramsString = urlContainer[1];
						String[] paramsContainer;
						if (paramsString.contains("&")){
							paramsContainer 	= paramsString.split("&");
						}else{
							paramsContainer = new String[1];
							paramsContainer[0]  = paramsString;
						}
						
						for (int i = 0; i < paramsContainer.length; i++) {
							String[] param = paramsContainer[i].split("=");
							if (param.length == 2) {
								params.put(param[0], param[1]);
							}
						}
					} 
					else{
						event = url;
					}
					result = onReceiveEvent(event, params);
					return result;
				} catch (Exception e) {
					return super.shouldOverrideUrlLoading(view, url);
				}

			}
		});
	}

	public void loadHtmlFile(String path, String baseUrl, Object data) {
		loadHtmlFile(path, baseUrl, data, 8192);
	}

	public void loadHtmlFile(String path, String baseUrl, Object data,
			int bufferSize) {

		InputStream fis = null;
		try {
			fis = getAssets().open(path);
			byte[] buffer = new byte[bufferSize];
			int len = 0;
			StringBuffer wrapperBuffer = new StringBuffer();
			while ((len = fis.read(buffer)) != -1) {
				wrapperBuffer.append(new String(buffer, 0, len));
			}
			String html = wrapperBuffer.toString();

			Pattern pattern = Pattern.compile("\\$\\{([^}]*)\\}");

			Matcher matcher = pattern.matcher(html);
			StringBuffer htmlStringBuffer = new StringBuffer();
			while (matcher.find()) {
				String key = matcher.group(1);
				try{
					
					String result = this.onStringReplacementForText(key, data);
					result = result.replaceAll("\\\\", "\\\\\\\\");
					matcher.appendReplacement(htmlStringBuffer,
							result);
				}catch(Exception e){
					matcher.appendReplacement(htmlStringBuffer,"error");
				}

			}
			
			matcher.appendTail(htmlStringBuffer);
			getWebView().loadDataWithBaseURL(baseUrl,
					htmlStringBuffer.toString(), "text/html", "utf8", "");

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
