package com.lessu.foundation;

public class Callback {
	public interface NoneParamCallBack{
		void callback();
	}
	
	public interface CompleteCallBack{
		void complete();
	}
	
	public interface CallBackWithParams<E>{
		void onCallback(E params);
	}
	
	public interface CallBackWith2Params<E1,E2>{
		void onCallback(E1 params1,E2 params2);
	}
	
	public interface CallBackWith3Params<E1,E2,E3>{
		void onCallback(E1 params1,E2 params2,E3 params3);
	}
	
}
