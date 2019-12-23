package com.lessu.uikit.easy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class EasyCollection{
	public interface OnListEnumlateInterface<T extends Object>{
		boolean onEmulate(T item,long index);
	}
	public static void enumlateList(List<?> list,OnListEnumlateInterface onEmulate){
		Iterator<?> itr = list.iterator();
		long index = 0;
		while (itr.hasNext()) {
			Object item = itr.next();
			boolean stop = !onEmulate.onEmulate(item, index);
			if(stop){
				return ;
			}
			index ++;
		}
	}
	
	public interface OnMapEnumlateInterface<T>{
		boolean onEmulate(T key,Object value,long index);
	}
	
	public static void enumlateMap(Map<?,?> map,OnMapEnumlateInterface onEmulate){
		long index = 0;
		for (Map.Entry<?, ?> entry : map.entrySet()) {       
		    Object key = entry.getKey();
		    Object value=entry.getValue();
		    
		    boolean stop = !onEmulate.onEmulate(key , value , index);
		    if(stop){
				return ;
			}
		    index ++;
		}    
	}
}
