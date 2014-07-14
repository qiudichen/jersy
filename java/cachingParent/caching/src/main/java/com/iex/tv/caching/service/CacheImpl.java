package com.iex.tv.caching.service;

import com.iex.tv.caching.spi.Cache;

public abstract class CacheImpl<T> implements Cache<T> {
	private String name;
	
	protected T cache;
	
	public CacheImpl() {

	}

	public CacheImpl(String name, T cache) {
		setName(name);
		setCache(cache);
	}

	@Override
	public String getName() {
		return this.name;
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
	
	protected void setName(String name) {
		assert name != null && !name.isEmpty() : "name must be not null or empty.";
		this.name = name;
	}
	
	protected void setCache(T cache) {
		assert cache != null : "Cache must be not null.";
		this.cache = cache;
	}
}
