package com.scetia.app_sand.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fhm on 2016/7/15.
 */
public class GsonUtil {
    private static Gson mGson = new Gson();
    /**
     * json转换为类对象
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> T JsonToObject(String jsonString, Class<T> cls) {
        return mGson.fromJson(jsonString,cls);
    }

    /**
     * json字符串转换为list集合
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> JsonToList(String jsonString, final Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            list = mGson.fromJson(jsonString, new ParameterizedType() {
                @NonNull
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[]{cls};
                }

                @NonNull
                @Override
                public Type getRawType() {
                    return List.class;
                }

                @Nullable
                @Override
                public Type getOwnerType() {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将对象转换为json字符串
     * @param o
     * @return
     */
    public static String toJsonStr(Object o){
        return mGson.toJson(o);
    }

    /**
     * 将Map集合转换为json字符串
     * @return
     */
    public static String mapToJsonStr(Map<?,Object> map){
        return mGson.toJson(map);
    }

}
