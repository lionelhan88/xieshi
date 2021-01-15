package com.lessu.net;

import java.util.HashMap;
import java.util.Map;

public class ApiBase {

    protected static ApiBase apiInstance;
    public static ApiBase sharedInstance(){
        if(apiInstance == null){
            apiInstance = new ApiBase();
            return apiInstance;
        }
        return apiInstance;
    }

    public ApiBase() {
        init();
    }

    public void init(){
        httpMethod = "http://";
    }

    public String getAbsoluteUrlString(String relativeString){
        return this.httpMethod + this.apiUrl + relativeString;
    }
    public ApiConnection getConnectionWithApiMethod(ApiMethodDescription description,HashMap<String,Object> params){
        if (params == null){
            params =  new HashMap<String,Object>();
        }
        

        String method;
        String absoluteUrlString;
        if(description.connectionMethod == ApiMethodDescription.ConnectionMethod.Get){
        	method = "get";
        	absoluteUrlString = getAbsoluteUrlString(description.apiUrl);
        }else if(description.connectionMethod == ApiMethodDescription.ConnectionMethod.Post){
        	method = "post";
        	absoluteUrlString = getAbsoluteUrlString(description.apiUrl);
        }else if(description.connectionMethod == ApiMethodDescription.ConnectionMethod.Soap){
        	method = "soap";
        	absoluteUrlString = this.httpMethod + this.apiUrl;
        }else{
        	method = "get";
        	absoluteUrlString = getAbsoluteUrlString(description.apiUrl);
        }
        System.out.println("absoluteUrlString......"+absoluteUrlString);
        ApiConnection retConnection = new ApiConnection(absoluteUrlString);
        retConnection.setParams(params);
        retConnection.setResultType(ApiConnection.ResultType.values()[description.resultType.ordinal()]);
        System.out.println(retConnection.urlString);
        retConnection.setRequestMethod(method);
        if(description.connectionMethod == ApiMethodDescription.ConnectionMethod.Soap){
        	retConnection.soapAction = description.soapAction;
        	retConnection.soapUrl 	 = description.apiUrl;
        }

        return retConnection;
    }

    public String apiUrl;
    public String httpMethod;
    public String preprocess;

}
