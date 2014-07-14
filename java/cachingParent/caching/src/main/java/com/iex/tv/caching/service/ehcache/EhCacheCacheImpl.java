package com.iex.tv.caching.service.ehcache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

import com.iex.tv.caching.service.CacheImpl;

public class EhCacheCacheImpl extends CacheImpl<Ehcache> {
	protected final Log logger = LogFactory.getLog(getClass());
	
	public EhCacheCacheImpl() {
		super();
	}

	public EhCacheCacheImpl(Ehcache cache) {
		super(cache.getName(), cache);
	}
	
	public EhCacheCacheImpl(String name, Ehcache cache) {
		super(name, cache);
		Status status = cache.getStatus();
		assert Status.STATUS_ALIVE.equals(status): "An 'alive' Ehcache is required - current cache is " + status.toString();
	}

	@Override
	public Object getValue(Object key) {
		Element element = this.cache.get(key);
		return (element != null ? element.getObjectValue() : null);
	}

	@Override
	public void put(Object key, Object value) {
		this.cache.put(new Element(key, value));
	}

	@Override
	public void remove(Object key) {
		this.cache.remove(key);
	}

	@Override
	public void clear() {
		this.cache.removeAll();
	}

	@Override
	public boolean containKey(Object key) {
		return this.cache.isKeyInCache(key);
	}
}
