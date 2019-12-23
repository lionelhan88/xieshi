package com.lessu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.R;

import java.io.UTFDataFormatException;

public class WebViewActivity extends NavigationActivity {


    private String url;
    private String html;
    private boolean wrapHtml;
    private String title;
    private WebView webView;
    enum WebWrapper{
        None,
        ResizeZoom640
    }
    public static void putHtmlParams(Intent intent , String html,String title,boolean wrapHtml){
        intent.putExtra("html",html);
        intent.putExtra("title",title);
        intent.putExtra("wrapHtml", wrapHtml);
    }
    public static void putUrlParams(Intent intent , String url,String title){
        intent.putExtra("url",url);
        intent.putExtra("title",title);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wrapHtml = false;
        setContentView(R.layout.activity_web_view_actitivity);
        webView = (WebView) findViewById(R.id.web_view);

        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDefaultTextEncodingName("utf-8");
        html = getIntent().getStringExtra("html");
        url  = getIntent().getStringExtra("url");
        title= getIntent().getStringExtra("title");
        wrapHtml=getIntent().getBooleanExtra("wrapHtml", false);
        setTitle(title);


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (html!=null){
        	String wrpperedHtml;
        	if(wrapHtml){
        		wrpperedHtml= "<html><head><title></title>"
        				+ "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head>"
        				+ "<meta name='viewport' content='width=640,user-scalable=no,target-densitydpi=device-dpi'>"
        				+ "<body>"
        		+ html +
        		"</body></html>";
        	}else{
        		wrpperedHtml = html;
        	}
        	
            webView.loadDataWithBaseURL(null, wrpperedHtml, "text/html", "utf8", null);
        }
        if (url != null) {
            webView.loadUrl(url);
        }
    }
	public boolean isWrapHtml() {
		return wrapHtml;
	}
	public void setWrapHtml(boolean wrapHtml) {
		this.wrapHtml = wrapHtml;
	}

   
}
