package com.iex.tv.caching.service.ehcache;

import java.util.Collection;
import java.util.LinkedHashSet;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;

import com.iex.tv.caching.service.BaseCacheManagerImpl;
import com.iex.tv.caching.spi.Cache;

public class EhCacheCacheManagerImpl extends BaseCacheManagerImpl<Ehcache> {
	private net.sf.ehcache.CacheManager cacheManager;

	public EhCacheCacheManagerImpl() {
		
	}
	
	public void setCacheManager(CacheManager cacheManager) {
		assert cacheManager != null : "Ehcache cacheManager must be not null.";
		this.cacheManager = cacheManager;
	}
	
	@Override
	protected Collection<EhCacheCacheImpl> initCaches() {
		Status status = this.cacheManager.getStatus();
		assert Status.STATUS_ALIVE.equals(status) : "EhCache CacheManager is not alive - current cache is " + status.toString();
		
		String[] names = this.cacheManager.getCacheNames();
		Collection<EhCacheCacheImpl> caches = new LinkedHashSet<EhCacheCacheImpl>(names.length);
		for (String name : names) {
			caches.add(new EhCacheCacheImpl(this.cacheManager.getEhcache(name)));
		}
		return caches;
	}

	@Override
	protected void removeInternal(String cacheName) {
		cacheManager.removeCache(cacheName);
	}

	@Override
	protected void removeAllInternal() {
		cacheManager.removalAll();
	}

	@Override
	protected Cache<Ehcache> addCacheInternal(String cacheName) {
		try {
			cacheManager.addCache(cacheName);
			Ehcache echcache = cacheManager.getEhcache(cacheName);
			return new EhCacheCacheImpl(echcache);
		} catch(Exception e) {
			logger.warn("Unable to create ehcache with name: " + cacheName, e);
		}
		return null;
	}
	
	public void shutdown() {
		this.cacheManager.shutdown();
	}
}
