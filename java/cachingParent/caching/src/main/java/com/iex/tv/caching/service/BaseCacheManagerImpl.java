package com.iex.tv.caching.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import com.iex.tv.caching.spi.Cache;
import com.iex.tv.caching.spi.CacheManager;

public abstract class BaseCacheManagerImpl<T> implements CacheManager<T> {

	private final ConcurrentMap<String, Cache<T>> caches = new ConcurrentHashMap<String, Cache<T>>();
	
	protected abstract Collection<? extends Cache<T>> initCaches();
	
	@PostConstruct
	public void init() {
		Collection<? extends Cache<T>> caches = initCaches();
		this.caches.clear();
		if(caches != null) {
			for (Cache<T> cache : caches) {
				this.caches.put(cache.getName(), cache);
			}			
		}
	}

	protected final void addCache(Cache<T> cache) {
		this.caches.put(cache.getName(), cache);
	}

	@Override
	public Cache<T> getCache(String name) {
		return this.caches.get(name);
	}
	
	@Override
	public Collection<String> getCacheNames() {
		return this.caches.keySet();
	}

	@Override
	public boolean evict(String cacheName, Object key) {
		Cache<T> cache = getCache(cacheName);
		if(cache != null) {
			return cache.evict(key);
		}
		return false;
	}
	
	@Override
	public void clear(String cacheName) {
		Cache<T> cache = getCache(cacheName);
		if(cache != null) {
			cache.clear();
		}		
	}
	
	@Override
	public void clear() {
		Collection<String> names = getCacheNames();
		for(String name : names) {
			clear(name);
		}
	}
}
