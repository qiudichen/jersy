package com.iex.tv.cache.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iex.tv.cache.spi.Cache;

public abstract class AbstractCache<T> implements Cache<T> {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String cacheManagerName;
	
	private String cacheName;
	
	protected T cache;
	
	public AbstractCache() {
		
	}
	
	public AbstractCache(String cacheManagerName, String cacheName, T cache) {
		super();
		this.cacheManagerName = cacheManagerName;
		this.cacheName = cacheName;
		this.cache = cache;
	}

	@Override
	public T getNativeCache() {
		return cache;
	}
	
	@Override
	public boolean evict(Object key) {
		if(this.containsKey(key)) {
			this.remove(key);
			return true;
		}
		return false;
	}

	@Override
	public String getCacheManagerName() {
		return cacheManagerName;
	}

	@Override
	public void put(Object key, Object value) {
		put(key, value, false);
	}
	
	@Override
	public void remove(Object key) {
		remove(key, false);
	}
	
	@Override
	public void clear() {
		clear(false);
	}
	
	public void setCacheManagerName(String cacheManagerName) {
		assert cacheManagerName != null && !cacheManagerName.isEmpty() : "cacheManagerName must be not null or empty.";
		this.cacheManagerName = cacheManagerName;
	}

	@Override
	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		assert cacheName != null && !cacheName.isEmpty() : "name must be not null or empty.";
		this.cacheName = cacheName;
	}

	public void setCache(T cache) {
		assert cache != null : "Cache must be not null.";
		this.cache = cache;
	}	
	
	
}
