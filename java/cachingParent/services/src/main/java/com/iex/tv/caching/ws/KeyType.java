package com.iex.tv.caching.ws;


public enum KeyType implements KeyConverter{
	StringType,
	LongType() {
		@Override
		public Object getValue(String value) {
			Long lv = null;
			if(value != null && !value.isEmpty()) {
				try {
					lv = Long.valueOf(value);
				} catch(Exception e) {
					//
				}
			}
			return lv;
		}
	}, ;

	public Object getValue(String value) {
		return value;
	}
	
}
 