package com.lessu.xieshi.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * created by ljs
 * on 2020/11/25
 */
public class XSConverterJson extends Converter.Factory {
    private Gson gson = new Gson();
    public static XSConverterJson create(){
        return new XSConverterJson();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new XSResponseBodyConverter<Object>(gson, type);
    }
}
