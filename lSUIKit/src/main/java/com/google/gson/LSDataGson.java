package com.google.gson;

import com.lessu.data.LSData;

public class LSDataGson implements LSData{
	
	private static final long serialVersionUID = 320222L;
	
	protected JsonElement gson;
	
	public LSDataGson (JsonElement gson) {
		this.gson = gson;
	}
	public LSDataGson (String fromString) {
		this.load(fromString);
	}
	
	@Override
	public void load(String fromString) {
		this.gson = EasyGson.jsonFromString(fromString);
	}
	
	@Override
	public String toString() {

		return this.gson.toString();
	}
	
	@Override
	public String string(String keyPath) {
		return string(keyPath,null);
	}

	@Override
	public String string(String keyPath, String defaultValue) {
		return GsonValidate.getStringByKeyPath(gson, keyPath,defaultValue);
	}
	@Override
	public LSData data(String keyPath){
		return data(keyPath, "");
	}
	
	@Override
	public LSData data(String keyPath, LSData defaultValue) {
		JsonElement jsonElement = GsonValidate.getElementByKeyPath(gson, keyPath ,null);
		if (jsonElement != null){
			return new LSDataGson( jsonElement );	
		}else{
			return defaultValue;
		}
	}
	@Override
	public LSData data(String keyPath, String fromString) {
		JsonElement jsonElement = GsonValidate.getElementByKeyPath(gson, keyPath ,null);
		if (jsonElement != null){
			return new LSDataGson( jsonElement );	
		}else{
			return new LSDataGson(fromString);
		}
	}
	@Override
	public boolean bool(String keyPath, boolean defaultValue) {
		JsonElement jsonElement = GsonValidate.getElementByKeyPath(gson, keyPath ,null);
		try {
			return jsonElement.getAsBoolean();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public Number number(String keyPath, Number defaultValue) {
		JsonElement jsonElement = GsonValidate.getElementByKeyPath(gson, keyPath ,null);
		try {
			return jsonElement.getAsNumber();
		} catch (Exception e) {
			return defaultValue;
		}
	}
	public int arrayLength(String keyPath,int defaultValue){
		JsonElement jsonElement = GsonValidate.getElementByKeyPath(gson, keyPath ,null);
		if(jsonElement!=null && jsonElement.isJsonArray()){
			return jsonElement.getAsJsonArray().size();
		}else{
			return defaultValue;
		}
	}
	public boolean isArray(String keyPath){
		JsonElement jsonElement = GsonValidate.getElementByKeyPath(gson, keyPath ,null);
		return jsonElement!=null&&jsonElement.isJsonArray();
	}
	public boolean isDict(String keyPath){
		JsonElement jsonElement = GsonValidate.getElementByKeyPath(gson, keyPath ,null);
		return jsonElement!=null&&jsonElement.isJsonObject();
	}


	
//	public LSData setData(String keyPath, LSDataGson value) {
//		return null;
//	}
//	
//	public LSData setData(String keyPath, String fromString) {
//		return null;
//	}
//	@Override
//	public String setString(String keyPath, String string) {
//

//		return null;
//	}
//
//	@Override
//	public String setBool(String keyPath, boolean value) {
//

//		return null;
//	}
//
//	@Override
//	public String setNumber(String keyPath, Number value) {
//

//		return null;
//	}
//	
//	
}
