package com.iex.tv.caching.service;

import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iex.tv.caching.spi.Cache;
import com.iex.tv.caching.spi.CacheManager;

public abstract class BaseCacheManagerImpl<T> implements CacheManager<T> {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private final ConcurrentMap<String, Cache<T>> caches = new ConcurrentHashMap<String, Cache<T>>();
	
	protected abstract Collection<? extends Cache<T>> initCaches();

	protected abstract void removeInternal(String cacheName);
	protected abstract void removeAllInternal();
	protected abstract Cache<T> addCacheInternal(String cacheName);
	
	
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

	protected boolean containsName(String name) {
		return caches.containsKey(name);
	}
	
	@Override
	public Cache<T> getCache(String name) {
		return this.caches.get(name);
	}
	
	@Override
	public Collection<String> getCacheNames() {
		return new TreeSet<String>(this.caches.keySet());
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
	
	@Override
	public boolean addCache(String name) {
		synchronized(caches) {
			if(containsName(name)) {
				return false;
			}
			
			Cache<T> cache = addCacheInternal(name);
			if(cache != null) {
				this.caches.put(cache.getName(), cache);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void remove(String cacheName) {
		synchronized(caches) {
			removeInternal(cacheName);
			caches.remove(cacheName);
		}
	}

	@Override
	public void removalAll() {
		synchronized(caches) {
			removeAllInternal();
			caches.clear();
		}		
	}
}
