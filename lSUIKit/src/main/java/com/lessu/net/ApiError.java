package com.lessu.net;

public class ApiError {
	public static final String ApiConnectionErrorDomain = "ApiConnectionErrorDomain";
	public static final String ApiNotStandartErrorDomain = "ApiNotStandartErrorDomain";
	public static final String ApiServerResponseErrorDomain = "ApiServerResponseErrorDomain";
	public static final String ApiResponseNotSuccessErrorDomain = "ApiResponseNotSuccessErrorDomain";

	public String errorDomain = "error";
	public int    errorCode   = 0;
	public String errorMeesage= "";
}
