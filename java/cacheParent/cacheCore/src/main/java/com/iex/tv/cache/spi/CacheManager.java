package com.iex.tv.cache.spi;

import java.util.Collection;

/**
 * A CacheManager provides and maintains the lifecycles of Cache instances.
 * 
 * @author David Chen
 *
 * @param <T> the underlying native cache provider
 */
public interface CacheManager<T> {
	/**
	 * Return the cache manager name
	 */
	public String getCacheManagerName();
	/**
	 * create new cache with default configuration
	 * @Return: true if cache is created, 
	 * 			false if the cache already exists 
	 */
	public boolean addCache(String name);

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
	 * remove the cache
	 */
	public void remove(String cacheName);
	
	/**
	 * remove all object in all caches
	 */	
	public void clear();
	
	/**
	 * remove the cache
	 */
	public void removalAll();
	
	public void shutdown();
}
