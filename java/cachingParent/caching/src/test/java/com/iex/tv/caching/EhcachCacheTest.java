package com.iex.tv.caching;

import java.util.Collection;

import junit.framework.Assert;
import net.sf.ehcache.Ehcache;

import org.junit.Test;

import com.iex.tv.caching.service.ehcache.EhCacheCacheManagerImpl;
import com.iex.tv.caching.spi.Cache;

public class EhcachCacheTest extends BaseCachingTest {
	@Test
	public void standardAPIs() {
		EhCacheCacheManagerImpl cacheManager = getEhCacheCacheManagerImpl();
		Collection<String> names = cacheManager.getCacheNames();
		Assert.assertNotNull(names);
		
		for(String name : names) {
			Cache<Ehcache> cache = cacheManager.getCache(name);
			Assert.assertNotNull(cache);
			
		}		
	}
	
	private EhCacheCacheManagerImpl getEhCacheCacheManagerImpl() {
		return (EhCacheCacheManagerImpl)cacheManager;
	}
}
