package com.lessu.foundation;

public class ValidateHelper {
	/**
	 * 固定电话验证
	 */
	private static final String REGEX_GUDING= "^0(10|2[0-5789]-|\\d{3})-?\\d{7,8}$";
	/**
	 * 验证手机号
	 */
	private static final String REGEX_PHONE ="^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)" +
			"|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	public static boolean validateMail(String data){
		return (RegKit.match(data, "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) != null;
	}
	
	public static boolean validatePhone(String data){
		return (RegKit.match(data, REGEX_PHONE)) != null;
	}

	/**
	 * 验证是否是固定电话
	 * @param data
	 * @return
	 */
	public static boolean validateTel(String data){
		return (RegKit.match(data,REGEX_GUDING))!=null;
	}
}
