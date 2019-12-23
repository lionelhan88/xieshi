package com.lessu.data;

import java.io.Serializable;

public interface LSData extends Serializable{
	
	public LSData data(String keyPath);
	public LSData data(String keyPath,LSData defaultValue);
	public LSData data(String keyPath,String fromString);
	
	public String string(String keyPath);
	public String string(String keyPath,String defaultValue);
	
	public boolean bool(String keyPath,boolean defaultValue);
	public Number number(String keyPath,Number defaultValue);
	
//	public LSData setData(String keyPath,LSData value);
//	public LSData setData(String keyPath,String fromString);
	
//	public void setString(String keyPath,String string);
//	public void setBool(String keyPath,boolean value);
//	public void setNumber(String keyPath,Number value);
	
	abstract public void load(String fromString);
	abstract public String toString();
	
	public int arrayLength(String keyPath,int defaultValue);
	public boolean isArray(String keyPath);
	public boolean isDict(String keyPath);
	
	
}
