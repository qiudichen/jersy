package com.iex.tv.cache.spi;
/**
 * Factory interface that exposes the underlying CacheManager instance (independent or shared), configured from a specified config location.
 * 
 * In multiple cache manager instances environment, Factory is responsable for maintaining unique cache manager name for each instance.
 * 
 * @author David Chen
 *
 * @param <T>
 */
public interface CacheManagerFactory<T> {
	public CacheManager<T> createCacheManager();
	
	public boolean remove(String cacheManagerName);
}
