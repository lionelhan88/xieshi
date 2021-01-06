package com.lessu.foundation;

import android.content.Context;
import android.content.SharedPreferences;

import com.lessu.ShareableApplication;

/**
 * Created by lessu on 14-7-14.
 */
public class LSUtil {

    private static final String LSUtilKey = "LSUtil";
    protected static String currentUser;

    public static void setCurrentUserId(String user){
        currentUser = user;
    }

    public static String currentUserId(){
        return currentUser;
    }

    public static boolean isLogin(){
        return !( LSUtil.currentUserId() == null || LSUtil.currentUserId().equalsIgnoreCase("0") || LSUtil.currentUserId().equalsIgnoreCase("") );
    }
    public static void setShouldLogin(boolean shouldLogin){
        LSUtil.setValueStatic("ls_util_should_login", String.valueOf(shouldLogin));
    }
    public static boolean shouldLogin(){
        return Boolean.parseBoolean( LSUtil.valueStatic("ls_util_should_login") );
    }


    public static void logout(){
        LSUtil.setCurrentUserId(null);
    }

    public static String valueStatic(String key){
        Context context = ShareableApplication.getInstance();
        SharedPreferences settings = context.getSharedPreferences(LSUtilKey, 0);
        String value = settings.getString(key,null);
        return value;
    }

    public static void setValueStatic(String key,String value){
        Context context = ShareableApplication.getInstance();
        SharedPreferences settings = context.getSharedPreferences(LSUtilKey, 0);
        SharedPreferences.Editor editor = settings.edit();
        if (value == null){
            editor.remove(key);
        }else {
            editor.putString(key,value);
        }
        editor.commit();
    }


    public LSUtil() {
        this.context = ShareableApplication.getInstance();
    }

    protected Context context;
    protected LSUtil sharedUtilInstance = null;

    public LSUtil sharedUtil(Context context){
        if (sharedUtilInstance == null){
            sharedUtilInstance = new LSUtil();
        }
        return sharedUtilInstance;
    }
    public String value(String key){
        SharedPreferences settings = context.getSharedPreferences(LSUtilKey + Validate.stringDefaultIfNot(LSUtil.currentUserId(), "0"), 0);
        String value = settings.getString(key,null);
        return value;
    }

    public void setValue(String key,String value){
        SharedPreferences settings = context.getSharedPreferences(LSUtilKey + Validate.stringDefaultIfNot(LSUtil.currentUserId(),"0"), 0);
        SharedPreferences.Editor editor = settings.edit();
        if (value == null){
            editor.remove(key);
        }else {
            editor.putString(key,value);
        }
        editor.commit();
    }



    public static String loginUserName(){
        return LSUtil.valueStatic("username");
    }

    public static String password(){
        return LSUtil.valueStatic("password");
    }

    public static void setLoginUserName(String userName){
        LSUtil.setValueStatic("username",userName);
    }

    public static void setPassword(String password){
        LSUtil.setValueStatic("password",password);
    }
}
