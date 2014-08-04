package com.iex.tv.cache.service;

import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iex.tv.cache.spi.Cache;
import com.iex.tv.cache.spi.CacheManager;

public abstract class AbstractCacheManager<T> implements CacheManager<T> {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String cacheManagerName;
	
	private final ConcurrentMap<String, Cache<T>> caches = new ConcurrentHashMap<String, Cache<T>>();

	/**
	 * initialize all native caches base on configuration 
	 * @return a collection of all native caches
	 */
	protected abstract Collection<? extends Cache<T>> initCaches();	
	
	/**
	 * create a native cache dynamically using default configuration
	 * @return a native caches
	 */
	protected abstract Cache<T> createNativeCache(String cacheName);
	
	/**
	 * remove the native cache
	 * @param cacheName
	 */
	protected abstract void removeNativeCache(String cacheName);
	
	/**
	 * remove all native caches attached in this cache manager
	 */
	protected abstract void removeAllNativeCaches();
	
	public AbstractCacheManager() {
		
	}
	
	public AbstractCacheManager(String cacheManagerName) {
		setCacheManagerName(cacheManagerName);
	}

	public void setCacheManagerName(String cacheManagerName) {
		assert cacheManagerName != null : "Cache Manager Name cannot be null";
		this.cacheManagerName = cacheManagerName;
	}

	@Override
	public String getCacheManagerName() {
		return cacheManagerName;
	}
	
	
	@PostConstruct
	public void init() {
		Collection<? extends Cache<T>> caches = initCaches();
		this.caches.clear();

		if(caches != null) {
			for (Cache<T> cache : caches) {
				this.caches.put(cache.getCacheName(), cache);
			}			
		}
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
			
			Cache<T> cache = createNativeCache(name);
			if(cache != null) {
				this.caches.put(cache.getCacheName(), cache);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void remove(String cacheName) {
		synchronized(caches) {
			removeNativeCache(cacheName);
			caches.remove(cacheName);
		}
	}

	@Override
	public void removalAll() {
		synchronized(caches) {
			removeAllNativeCaches();
			caches.clear();
		}		
	}	
	
	protected boolean containsName(String name) {
		return caches.containsKey(name);
	}	
	
	@Override
	public void shutdown() {
		this.caches.clear();
	}	
}
