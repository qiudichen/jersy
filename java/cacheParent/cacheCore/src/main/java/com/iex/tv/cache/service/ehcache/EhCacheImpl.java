package com.iex.tv.cache.service.ehcache;

import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

import com.iex.tv.cache.service.AbstractCache;

public class EhCacheImpl extends AbstractCache<Ehcache> {

	public EhCacheImpl() {
		super();
	}

	public EhCacheImpl(String cacheManagerName, String cacheName, Ehcache cache) {
		super(cacheManagerName, cacheName, cache);
		Status status = cache.getStatus();
		assert Status.STATUS_ALIVE.equals(status): "An 'alive' Ehcache is required - current cache is " + status.toString();
	}

	@Override
	public Object getValue(Object key) {
		Element element = this.cache.get(key);
		return (element != null ? element.getObjectValue() : null);
	}

	@Override
	public void put(Object key, Object value,
			boolean doNotNotifyCacheReplicators) {
		assert key != null : "key must be not null";
		assert value != null : "value must be not null";		
		this.cache.put(new Element(key, value), doNotNotifyCacheReplicators);
	}

	@Override
	public void remove(Object key, boolean doNotNotifyCacheReplicators) {
		this.cache.remove(key, doNotNotifyCacheReplicators);
	}

	@Override
	public void clear(boolean doNotNotifyCacheReplicators) {
		this.cache.removeAll(doNotNotifyCacheReplicators);
	}

	@Override
	public boolean containsKey(Object key) {
		return this.cache.isKeyInCache(key);
	}

	@Override
	public int size() {
		return this.cache.getSize();
	}

	@Override
	public void dispose() {
		this.cache.dispose();
	}
	
	public List<?> getKeys() {
		return this.cache.getKeys();
	}
	
	public Element getCacheElement(Object key) {
        Element cacheElement = cache.getQuiet(key);
        return cacheElement;
	}	
}
