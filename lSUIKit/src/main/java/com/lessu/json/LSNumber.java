package com.lessu.json;

import com.google.gson.JsonPrimitive;

/**
 * Created by lessu on 14-6-27.
 */
public class LSNumber extends LSObject {
    JsonPrimitive jsonPrimitive;
    LSNumber(Object primitive) {
        super();
        jsonElement = jsonPrimitive = new JsonPrimitive((String) primitive);
    }
    LSNumber(String string) {
        super();
        jsonElement = jsonPrimitive = new JsonPrimitive(string);
    }

    LSNumber(int number) {
        super();
        jsonElement = jsonPrimitive = new JsonPrimitive(number);
    }
    LSNumber(long number) {
        super();
        jsonElement = jsonPrimitive = new JsonPrimitive(number);
    }
    LSNumber(double number) {
        super();
        jsonElement = jsonPrimitive = new JsonPrimitive(number);
    }
    LSNumber(float number) {
        super();
        jsonElement = jsonPrimitive = new JsonPrimitive(number);
    }
    LSNumber(boolean number) {
        super();
        jsonElement = jsonPrimitive = new JsonPrimitive(number);
    }
    @Override
    public LSObject copy() {
        return new LSNumber(this.toString());
    }

    public int getAsInt(){
        return jsonPrimitive.getAsInt();
    }
    public long getAsLong(){
        return jsonPrimitive.getAsLong();
    }
    public float getAsFloat(){
        return jsonPrimitive.getAsFloat();
    }
    public double getAsDouble(){
        return jsonPrimitive.getAsDouble();
    }
    public boolean getAsBoolean(){
        return jsonPrimitive.getAsBoolean();
    }
}
