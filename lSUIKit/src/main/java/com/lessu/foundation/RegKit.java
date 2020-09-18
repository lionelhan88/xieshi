package com.lessu.foundation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegKit {

	public static String match(String src,String reg){
		Pattern pattern = Pattern.compile(reg);    
		Matcher matcher = pattern.matcher(src);    
		if (matcher.find()){
			return matcher.group();
		} else {
			return null;
		}
	}
	public static String match(String src,String reg,int groupIndex){
		Pattern pattern = Pattern.compile(reg);    
		Matcher matcher = pattern.matcher(src);    
		if (matcher.find()){
			return matcher.group(groupIndex);
		}   
		else
		{
			return null;
		}
	}
}
