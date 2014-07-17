package com.iex.tv.caching.util;

import com.iex.tv.caching.ws.KeyType;

public interface CacheServiceUtil {
	
	public void removeObject(String cacheName, String keyValue, KeyType keyType);
	
	public void clear(String cacheName);
}
