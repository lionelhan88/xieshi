package com.lessu.xieshi.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * created by ljs
 * on 2020/11/25
 */
public class XSResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Gson gson;
    private Type type;
    private TypeAdapter adapter;

    public XSResponseBodyConverter(Gson gson, Type type, TypeAdapter adapter) {
        this.gson = gson;
        this.type = type;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String result = value.string();
        result = result.subSequence(result.indexOf("{"),result.lastIndexOf("}")+1).toString();
        return gson.fromJson(result,type);
    }
}
