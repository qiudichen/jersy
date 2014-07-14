package com.iex.tv.caching.spi;

public interface Cache<T> {
	/**
	 * Return the cache name. 
	 */
	String getName();
	
	/**
	 * Return the the underlying native cache provider. 
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
	 * remove the mapping for this key from this cache if it is present. 
	 * Parameters:key the key whose mapping is to be removed from the cache
	 */
	void remove(Object key);
	
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
	 *  An inexpensive check to see if the key exists in the cache. 
	 */
	boolean containsKey(Object key);
	
    /**
     * Returns the number of elements in this cache store.
     *
     * @return the number of elements in this store
     */
	int size();
}
