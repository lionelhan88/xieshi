package com.lessu.foundation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validate {
	public static boolean stringNotEmpty(Object string){
		if(string instanceof CharSequence){
			CharSequence charSequence = (CharSequence) string;
			if(charSequence.length() == 0){
				return false;
			}
			return true;
		}
		return false;
	}
	public static String stringEmptyIfNot(Object string){
		return Validate.stringDefaultIfNot(string,"");
	}
    public static String stringDefaultIfNot(Object string,String defaultString){
        if(Validate.stringNotEmpty(string)){
            return (String) string;
        }
        return defaultString;
    }
	
	public static boolean listNotEmpty(Object list){
		if(list instanceof List<?>){
			List<?> collection = (List<?>) list;
			if(collection.size() == 0){
				return false;
			}
			return true;
		}
		return false;
	}
	
	public static List<?> listEmptyIfNot(Object list){
		if(Validate.listNotEmpty(list)){
			return (List<?>) list;
		}
		return new ArrayList<Object>();
	}
	
	public static boolean mapNotEmpty(Object map){
		if(map instanceof Map<?,?>){
			Map<?,?> collection = (Map<?,?>) map;
			if(collection.size() == 0){
				return false;
			}
			return true;
		}
		return false;
	}
	
	public static Map<?,?> mapEmptyIfNot(Object map){
		if(Validate.mapNotEmpty(map)){
			return (Map<?,?>) map;
		}
		return new HashMap<Object,Object>();
	}

    public static String stringFromInt(int number){
        return String.format("%d",number);
    }
}
