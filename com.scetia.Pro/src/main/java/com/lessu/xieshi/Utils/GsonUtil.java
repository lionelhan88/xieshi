package com.lessu.xieshi.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

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

    /**
     * 格式化SOAP的数据
     */
    public static <T> List<T> parseSoapObject(String methodName, SoapObject soapObject, Class<T> aClass){
        List<T> beans = new ArrayList<>();
        try {
            SoapObject property = (SoapObject) soapObject.getProperty(methodName + "Result");
            for (int i=0;i<property.getPropertyCount();i++){
                SoapObject property1 = (SoapObject) property.getProperty(i);
                T t = aClass.newInstance();
                for (int j=0;j<property1.getPropertyCount();j++){
                    PropertyInfo info = new PropertyInfo();
                    property1.getPropertyInfo(j,info);
                    soapObjectToBean(info, t);
                }
                beans.add(t);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return beans;
    }

    /**
     * 将soapObject转换为javaBean
     * @param info
     * @param o
     * @param <T>
     */
    private static <T> void  soapObjectToBean(PropertyInfo info,T o){
        try {
            Class<?> aClass = o.getClass();
            Field declaredField = aClass.getDeclaredField(info.getName());
            declaredField.setAccessible(true);
            String typeName = declaredField.getType().toString();
            if(typeName.endsWith("String")){
                declaredField.set(o,String.valueOf(info.getValue()));
            }else if(typeName.endsWith("int")){
                declaredField.set(o,Integer.parseInt(String.valueOf(info.getValue())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
