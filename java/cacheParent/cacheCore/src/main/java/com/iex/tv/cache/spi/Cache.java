package com.iex.tv.cache.spi;

/**
 * Cache interface for Cache Framework.
 * <p/>
 * Cache is the central interface. It wraps the actual implementation. Caches are managed
 * by the {@link CacheManager}. The Cache performs logical actions.
 * <p/>
 * In multiple cache manager environment, Cache Manager Name + Cache Name should be unique.
 * 
 * @author David Chen
 * 
 * @param <T> the underlying native cache provider
 */
public interface Cache<T> {
	
	/**
	 * Return the cache manager name
	 */
	String getCacheManagerName();
	/**
	 * Return the cache name. 
	 */
	String getCacheName();
	
	/**
	 * Return the underlying native cache provider. 
	 */
	T getNativeCache();
	
	/**
	 * Return the value to which this cache maps the specified key. Returns null if the cache contains no mapping for this key. 
	 * 
	 * @Parameters:key key whose associated value is to be returned. 
	 * @Returns:the value to which this cache maps the specified key, or null if the cache contains no mapping for this key
	 */
	Object getValue(Object key);
	
	/**
	 * Associate the specified value with the specified key in this cache. 
	 * If the cache previously contained a mapping for this key, the old value is replaced by the specified value. 
	 * @Parameters:key the key with which the specified value is to be associatedvalue the value to be associated with the specified key
	 */
	void put(Object key, Object value);
	
	/**
	 * Associate the specified value with the specified key in this cache. 
	 * If the cache previously contained a mapping for this key, the old value is replaced by the specified value. 
     * Also notifies the CacheEventListener that:
     * <ul>
     * <li>the element was put, but only if the Element was actually put.
     * <li>if the element exists in the cache, that an update has occurred, even if the element would be expired
     * if it was requested
     * </ul>	  
	 * @Parameters:key the key with which the specified value is to be associatedvalue the value to be associated with the specified key
	 * @param value  An object. If Serializable it can fully participate in replication and the DiskStore.
	 * @param doNotNotifyCacheReplicators whether the put is coming from a doNotNotifyCacheReplicators cache peer, in which case this put should not initiate a
     *                                    further notification to doNotNotifyCacheReplicators cache peers
	 */
	void put(Object key, Object value, boolean doNotNotifyCacheReplicators);
	
	/**
	 * remove the mapping for this key from this cache if it is present. 
	 * Parameters:key the key whose mapping is to be removed from the cache
	 */
	void remove(Object key);

	/**
	 * remove the mapping for this key from this cache if it is present. 
     * <p/>
     * Also notifies the CacheEventListener after the element was removed, but only if an Element
     * with the key actually existed.
     * 	  
	 * Parameters:key the key whose mapping is to be removed from the cache
	 * @param doNotNotifyCacheReplicators whether the put is coming from a doNotNotifyCacheReplicators cache peer, in which case this put should not initiate a
     *                                    further notification to doNotNotifyCacheReplicators cache peers
     **/
	void remove(Object key, boolean doNotNotifyCacheReplicators);
	
	/**
	 * Evict the mapping for this key from this cache if it is present. 
	 * It will check if the key exists. 
	 * Parameters:key the key whose mapping is to be removed from the cache
	 */
	public boolean evict(Object key);
	/**
	 * Remove all mappings from the cache. 
	 */
	void clear();

	/**
	 * Remove all mappings from the cache. 
	 * @param doNotNotifyCacheReplicators whether the put is coming from a doNotNotifyCacheReplicators cache peer,
     *                                    in which case this put should not initiate a further notification to doNotNotifyCacheReplicators cache peers
	 */
	void clear(boolean doNotNotifyCacheReplicators);
	
	/**
	 *  An inexpensive check to see if the key exists in the cache. 
	 */
	boolean containsKey(Object key);
	
    /**
     * Returns the number of elements in this cache store.
     *
     * @return the number of elements in this store
     */
	int size();
	
	public void dispose();
}
