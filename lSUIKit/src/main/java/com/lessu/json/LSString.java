package com.lessu.json;

import com.google.gson.JsonPrimitive;

/**
 * Created by lessu on 14-6-27.
 */
public class LSString extends LSObject {
    JsonPrimitive jsonPrimitive;

    public LSString(String string) {
        super();
        jsonElement = jsonPrimitive = new JsonPrimitive(string);
    }
    public String getAsString(){
        return jsonPrimitive.getAsString();
    }
    @Override
    public LSObject copy() {
        return new LSString(this.toString());
    }

    @Override
    public String toString() {
        return jsonPrimitive.getAsString();
    }
}


