package com.iex.tv.caching.spi;

import java.util.Collection;

public interface CacheManager<T> {
	/**
	 * get cache by name
	 * @Return: cache if it exists, or null
	 */
	public Cache<T> getCache(String name);
	/**
	 * Return a collection of the caches known by this cache manager. 
	 * @Returns:names of caches known by the cache manager.
	 */
	public Collection<String> getCacheNames();
	
	/**
	 * remove object if key exists in the cache
	 */
	public boolean evict(String cacheName, Object key);
	
	/**
	 * remove all object in the cache
	 */
	public void clear(String cacheName);
	
	/**
	 * remove all object in all caches
	 */	
	public void clear();
}
