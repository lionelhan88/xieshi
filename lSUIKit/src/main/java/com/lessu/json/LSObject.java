package com.lessu.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.IllegalFormatCodePointException;

/**
 * Created by lessu on 14-6-26.
 */
public abstract class LSObject extends Object{
    public JsonElement jsonElement;
    LSObject(){
        jsonElement = null;
    }
    LSObject(String jsonString){
        jsonElement = new JsonParser().parse(jsonString);
    }

    public abstract LSObject copy();

    public boolean isArray(){
        return jsonElement.isJsonArray();
    }
    public boolean isDictionary(){
        return jsonElement.isJsonObject();
    }
    public boolean isString(){
        if(jsonElement.isJsonPrimitive()){
            JsonPrimitive primitive = (JsonPrimitive) jsonElement;
            return primitive.isString();
        }
        return false;

    }
    public boolean isNumber(){
        if(jsonElement.isJsonPrimitive()){
            JsonPrimitive primitive = (JsonPrimitive) jsonElement;
            return primitive.isNumber();
        }
        return false;
    }
    public LSObject get(){
        return this;
    }

    public LSDictionary getAsDictionary(){
        if (this.isDictionary()){
            return (LSDictionary) this;
        }
        throw new IllegalStateException("json:" + this.toString() + "is not a dictionary");
    }

    public LSArray getAsArray(){
        if (this.isArray()){
            return (LSArray) this;
        }
        throw new IllegalStateException("json:" + this.toString() + "is not a array");
    }
    public String toString(){
        return jsonElement.toString();
    }

    private static Class<?>[] NUMBER_TYPES = {
            int.class,
            long.class,
            float.class,
            double.class,
            boolean.class
    };
    protected boolean objectIsNumber(Object object){

        for (int i = 0 ;i < NUMBER_TYPES.length ; i++){
            Class<?> clazz = NUMBER_TYPES[i];
            if(clazz.isAssignableFrom(object.getClass())){
                return true;
            }
        }
        return false;
    }
}
