package com.iex.tv.caching.service.ehcache;

import java.util.Collection;
import java.util.LinkedHashSet;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;

import com.iex.tv.caching.service.BaseCacheManagerImpl;

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

}
