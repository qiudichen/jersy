package com.iex.tv.caching;

public interface BaseTestData {
	public abstract class BaseData<T> {
		public String[] getKeys() {
			return null;
		}
		public abstract T getObject();
	}
	
	public class StringData extends BaseData<String>{
		public static String[] keys = {};
		//public 

		@Override
		public String getObject() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
