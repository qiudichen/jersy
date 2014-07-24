package com.iex.tv.caching.spi;

import java.io.Serializable;


public interface CacheNotifier {
	public void put(String cacheName, String keyValue, Serializable object);

	public void removeObject(String cacheName, String keyValue);
	
	public void clear(String cacheName);
}
