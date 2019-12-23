package com.lessu.foundation;

public class Tuple<T1,T2> {
	public T1 value1;
	public T2 value2;
	Tuple(T1 value1,T2 value2){
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public static Tuple<String,String> StringTuple(String value1,String value2){
		return new Tuple<String, String>(value1, value2);
	}
}
