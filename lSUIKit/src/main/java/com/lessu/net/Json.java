package com.lessu.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by lessu on 14-6-26.
 */
public class Json{
    private static JsonParser sharedJsonParser;
    public static JsonParser getSharedJsonParser() {
        if (sharedJsonParser == null){
            sharedJsonParser = new JsonParser();
        }
        return sharedJsonParser;
    }




    public static JsonElement gsonElementCopy(JsonElement object){
        String json = object.toString();
        return getSharedJsonParser().parse(json);
    }
    public static JsonArray gsonElementCopy(JsonArray object){
        String json = object.toString();
        return (JsonArray) getSharedJsonParser().parse(json);
    }
    public static JsonObject gsonElementCopy(JsonObject object){
        String json = object.toString();
        return (JsonObject) getSharedJsonParser().parse(json);
    }
}
