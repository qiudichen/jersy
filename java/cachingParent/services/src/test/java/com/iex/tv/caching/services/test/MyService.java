package com.iex.tv.caching.services.test;

import javax.annotation.Resource;

import com.iex.tv.caching.spi.Cache;
import com.iex.tv.caching.spi.CacheManager;

public class MyService {
	private final String CACHE_NAME = "WFM";
	@Resource(name="cacheManager")
	protected CacheManager<?> cacheManager;
	
	public Object readService(Object key) {
		Cache<?> cache = getCache();
		Object value = null;
		if((value = cache.getValue(key)) != null) {
			return value;
		}
		
		if((value = readDataFromDao(key)) != null) {
			cache.put(key, value);
		}
		
		return value;
	}
	
	public void writeService(Object key, Object value) {
		Cache<?> cache = getCache();
	     writeDataToDataStore(key, value);
	     cache.put(key, value);
	}
	
	private Object readDataFromDao(Object key) {
		//get value from data source;
		return null;
	}
	
	
	private void writeDataToDataStore(Object key, Object value) {
		//write value to data source;
	}
	
	
	private Cache<?> getCache() {
		Cache<?> cache = cacheManager.getCache(CACHE_NAME);
		assert cache != null: "unable to find cache with name" + CACHE_NAME;
		return cache;
	}
}
