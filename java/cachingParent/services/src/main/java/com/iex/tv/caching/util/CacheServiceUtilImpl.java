package com.iex.tv.caching.util;

import com.iex.tv.caching.ws.CachingWSService;
import com.iex.tv.caching.ws.KeyType;

public class CacheServiceUtilImpl implements CacheServiceUtil {

	protected CachingWSService cachingWSService;
	
	@Override
	public void removeObject(String cacheName, String keyValue, KeyType keyType) {
		cachingWSService.removeObject(cacheName, keyValue, keyType);
	}
	

	@Override
	public void clear(String cacheName) {
		cachingWSService.clear(cacheName);
	}


	public void setCachingWSService(CachingWSService cachingWSService) {
		this.cachingWSService = cachingWSService;
	}
}
