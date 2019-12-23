package com.google.gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lessu on 14-6-27.
 */
public class EasyGson {
    public static JsonElement jsonCopy(JsonElement element){
        return element.deepCopy();
    }
    public static JsonObject jsonCopy(JsonObject element){
        return element.deepCopy();
    }
    public static JsonArray jsonCopy(JsonArray element){
        return element.deepCopy();
    }
    public static JsonPrimitive jsonCopy(JsonPrimitive element){
        return element.deepCopy();
    }


    public static JsonParser sharedParser;
    public static JsonParser getSharedParser() {
        if (sharedParser == null){
            sharedParser = new JsonParser();
        }
        return sharedParser;
    }

    public static JsonElement jsonFromString(String jsonString){
        return getSharedParser().parse(jsonString);
    }

    public static List<JsonElement> jsonArrayToList(JsonArray array){
        List ret = new ArrayList<JsonElement>(array.size());
        Iterator<JsonElement> iterator = array.iterator();
        while (iterator.hasNext()){
            JsonElement element = iterator.next();
            ret.add(element);
        }
        return ret;
    }

}
