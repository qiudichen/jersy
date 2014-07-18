package com.iex.tv.caching.test;

import java.util.Collection;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.iex.tv.caching.service.hashmap.CacheMapCacheManagerImpl;
import com.iex.tv.caching.service.hashmap.CacheMapImpl;
import com.iex.tv.caching.spi.Cache;

@ContextConfiguration(locations = { "classpath:/cachemap-core-context.xml"})
public class CacheMapCachingTest extends BaseCachingTest {
	@Test
	public void standardAPIs() {
		CacheMapCacheManagerImpl cacheManager = getCacheMapCacheManagerImpl();
		
		Collection<String> names = cacheManager.getCacheNames();
		Assert.assertNotNull(names);
		
		for(String name : names) {
			Cache<?> cache = cacheManager.getCache(name);
			Assert.assertNotNull(cache);
			Set<String> keys = initCache(cache);
			Assert.assertEquals(cache.size(), keys.size());
			
			int size = keys.size();
			boolean remove = true;
			CacheMapImpl cacheMapImpl = (CacheMapImpl)cache;
			Assert.assertTrue(cacheMapImpl.isScheduleRunning());
			Assert.assertEquals(cache.size(), size);
			for(String key : keys) {
				Assert.assertTrue(cache.containsKey(key));
				
				CacheMapImpl.CacheElement cacheElementBeforeGet = cacheMapImpl.getCacheElement(key);				
				long hitsBeforeGet = cacheElementBeforeGet.getHits();
				long lastAccessedTimeBeforeGet = cacheElementBeforeGet.getLastAccessedTime();
				
				Object value = cacheMapImpl.getValue(key);
				Assert.assertNotNull(value);
				
				CacheMapImpl.CacheElement cacheElementAfterGet = cacheMapImpl.getCacheElement(key);				
				long hitsAfterGet = cacheElementAfterGet.getHits();
				long lastAccessedTimeAfterGet = cacheElementAfterGet.getLastAccessedTime();
				Assert.assertEquals(hitsBeforeGet + 1, hitsAfterGet);
				Assert.assertTrue(lastAccessedTimeAfterGet > lastAccessedTimeBeforeGet);
				
				if(remove) {
					cache.remove(key);
					Assert.assertEquals(cache.size(), size-1);
					size = cache.size();
				}
				remove = !remove;
			}
			
			cache.clear();
			Assert.assertEquals(cache.size(), 0);
			Assert.assertFalse(cacheMapImpl.isScheduleRunning());
			
			for(String key : keys) {
				Assert.assertFalse(cache.containsKey(key));
			}
		}		
	}
	
	private CacheMapCacheManagerImpl getCacheMapCacheManagerImpl() {
		return (CacheMapCacheManagerImpl)cacheManager;
	}
}
