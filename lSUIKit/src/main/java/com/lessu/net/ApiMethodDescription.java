package com.lessu.net;

/**
 * Created by lessu on 14-7-1.
 */
public class ApiMethodDescription {
    public enum ConnectionMethod {
        Get,
        Post,
        Soap
    }

    public enum ResultType {
        Plain,
        Json,
        Standard
    }

    public String apiUrl = "";
    public String soapAction = "";
    public ConnectionMethod connectionMethod = ConnectionMethod.Get;
    public ResultType resultType = ResultType.Json;

    public static ApiMethodDescription newDescription(String apiUrl, ConnectionMethod method, ResultType resultType) {
        ApiMethodDescription description = new ApiMethodDescription();
        description.apiUrl = apiUrl;
        description.connectionMethod = method;
        description.resultType = resultType;
        return description;

    }

    public static ApiMethodDescription get(String apiUrl, ResultType resultType) {
        return newDescription(apiUrl, ConnectionMethod.Get, resultType);
    }

    public static ApiMethodDescription get(String apiUrl) {
        return get(apiUrl, ResultType.Standard);
    }

    public static ApiMethodDescription post(String apiUrl, ResultType resultType) {
        return newDescription(apiUrl, ConnectionMethod.Post, resultType);
    }

    public static ApiMethodDescription post(String apiUrl) {
        return post(apiUrl, ResultType.Standard);
    }

    public static ApiMethodDescription soap(String apiUrl, String soapAction, ResultType resultType) {
        ApiMethodDescription description = new ApiMethodDescription();
        description.apiUrl = apiUrl;
        description.soapAction = soapAction;
        description.connectionMethod = ConnectionMethod.Soap;
        description.resultType = resultType;
        return description;
    }

    public static ApiMethodDescription soap(String apiUrl, String soapAction) {
        return soap(apiUrl, soapAction, ResultType.Standard);
    }

}
