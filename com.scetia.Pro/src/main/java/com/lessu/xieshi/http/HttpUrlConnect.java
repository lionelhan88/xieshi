package com.lessu.xieshi.http;


import com.lessu.uikit.easy.EasyCollection;
import com.lessu.uikit.easy.EasyCollection.OnMapEnumlateInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class HttpUrlConnect {
	public String errorMessage = "";
	public String resultData = "";
	private String urlString = "";
	private HashMap<String, Object> params;
	private String paramsString = "";
	public HttpUrlConnect(String url,HashMap<String, Object> incomeParams){
		urlString = url;
		params = incomeParams;

	}

	public boolean startConnection(){
		EasyCollection.enumlateMap(params, new OnMapEnumlateInterface() {

			@Override
			public boolean onEmulate(Object key, Object value, long index) {
				paramsString = paramsString + "\"" + key + "\""+":" + "\"" + value + "\""+",";
				return true;
			}
		});
		String connectString = "";
		if (!paramsString.isEmpty()){
			paramsString = paramsString.substring(0, paramsString.length()-1);
			paramsString = "{" + paramsString+"}";
			try{
				paramsString = URLEncoder.encode(paramsString, "UTF-8");
			}
			catch (Exception e){
				errorMessage = e.getMessage();
				return false;
			}
			connectString = urlString+"?param="+paramsString;
		}
		else{
			connectString = urlString;
		}


		URL url = null;
		try {
			url = new URL(connectString);//构造一个url对象
		} catch (MalformedURLException e) {
			errorMessage = "FormatError";
			e.printStackTrace();
		}
		if(url!=null){
			try {
				HttpURLConnection urlConnection;//使用HttpURLConnection打开连接
				urlConnection = (HttpURLConnection)url.openConnection();
				if (urlConnection!=null){
					int responseCode = urlConnection.getResponseCode();
					if (responseCode == 200) {
						InputStream stream = urlConnection.getInputStream();//得到读取的内容

						InputStreamReader in = new InputStreamReader(stream);
						BufferedReader buffere = new BufferedReader(in);    //为输出创建BufferedReader
						String line = null;
						while((line = buffere.readLine()) != null){//使用while循环来取得获取的数据
							resultData += line + "\n";//我们要在每一行的后面加上一个反斜杠来换行
						}
						in.close();//关闭InputStreamReader
						urlConnection.disconnect();//关闭http连接
						if(resultData.equals("")){
							errorMessage="没取到数据";
							return false;
						}

						return true;
					}
					else{
						errorMessage="connect Open Failed";
						return false;
					}
				}
				errorMessage="访问失败";
				return false;
			} catch (IOException e) {
				String error = e.getMessage();
				errorMessage = "IOException";
				e.printStackTrace();
				return false;
			}
		}else{
			errorMessage = "url null";
			return false;
		}
	}

	public String getResultString(){
		return resultData;
	}
}
