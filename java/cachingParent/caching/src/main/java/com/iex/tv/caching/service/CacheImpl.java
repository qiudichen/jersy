package com.iex.tv.caching.service;

import com.iex.tv.caching.spi.Cache;

public abstract class CacheImpl<T> implements Cache<T> {
	private String name;
	
	protected T cache;
	
	public CacheImpl() {

	}

	public CacheImpl(String name, T cache) {
		assert cache != null : "Cache must be not null.";
		this.name = name;
		this.cache = cache;
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
		if(this.containKey(key)) {
			this.remove(key);
			return true;
		}
		return false;
	}
}
