package com.lessu.xieshi.Utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fhm on 2016/7/15.
 */
public class GsonUtil {
    public static <T> T JsonToObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return t;
    }

    public static <T> List<T> JsonToList(String jsonString, final Class<T> cls) {
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new ParameterizedType() {
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
    public static List<Map<String, Object>> JsonToMap(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString,
                    new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

}
