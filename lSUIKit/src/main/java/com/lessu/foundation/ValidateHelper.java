package com.lessu.foundation;

public class ValidateHelper {
	public static boolean validateMail(String data){
		return (RegKit.match(data, "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) != null;
	}
	
	public static boolean validatePhone(String data){
		return (RegKit.match(data, "1[3|4|5|7|8|9][0-9]\\d{4,8}")) != null;
	}
	
	
}
