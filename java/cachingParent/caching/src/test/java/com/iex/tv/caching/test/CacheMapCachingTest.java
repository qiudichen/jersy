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
	public void addDataRemoveData() {
		CacheMapCacheManagerImpl cacheManager = getCacheMapCacheManagerImpl();
		
		Collection<String> names = cacheManager.getCacheNames();

		for(String name : names) {
			Cache<?> cache = cacheManager.getCache(name);
			Assert.assertNotNull(cache);
			cacheData(cache); 
		}		
	}
	
	private void cacheData(Cache<?> cache) {
		Set<String> keys = initCache(cache);
		int size = keys.size();
		Assert.assertEquals(cache.size(), keys.size());
		
		CacheMapImpl cacheMapImpl = (CacheMapImpl)cache;
		Assert.assertEquals(cacheMapImpl.getKeys().size(), keys.size());
		
		if(cacheMapImpl.getCacheMapConfig().isForceExpiration()) {
			Assert.assertTrue(cacheMapImpl.isScheduleRunning());				
		} else {
			Assert.assertFalse(cacheMapImpl.isScheduleRunning());				
		}
		
		Assert.assertEquals(cache.size(), size);

		boolean remove = true;		
		for(String key : keys) {
			Assert.assertTrue(cache.containsKey(key));
			
			//check hit and last access
			CacheMapImpl.CacheElement cacheElementBeforeGet = cacheMapImpl.getCacheElement(key);				
			long hitsBeforeGet = cacheElementBeforeGet.getHits();
			long lastAccessedTimeBeforeGet = cacheElementBeforeGet.getLastAccessedTime();
			
			Object value = cacheMapImpl.getValue(key);
			Assert.assertNotNull(value);
			
			CacheMapImpl.CacheElement cacheElementAfterGet = cacheMapImpl.getCacheElement(key);				
			long hitsAfterGet = cacheElementAfterGet.getHits();
			long lastAccessedTimeAfterGet = cacheElementAfterGet.getLastAccessedTime();
			
			if(cacheMapImpl.getCacheMapConfig().isForceExpiration()) {
				Assert.assertEquals(hitsBeforeGet + 1, hitsAfterGet);
				Assert.assertTrue(lastAccessedTimeAfterGet >= lastAccessedTimeBeforeGet);
			} else {
				Assert.assertEquals(hitsBeforeGet, hitsAfterGet);
				Assert.assertEquals(lastAccessedTimeAfterGet, lastAccessedTimeBeforeGet);
			}
			
			if(remove) {
				cache.remove(key);
				Assert.assertEquals(cache.size(), size-1);
				size = cache.size();
			}
			remove = !remove;
		}
		
		if(cacheMapImpl.getCacheMapConfig().isForceExpiration()) {
			long expiredMillis = cacheMapImpl.getCacheMapConfig().getExpireMillis();
			long pollSecond = cacheMapImpl.getCacheMapConfig().getPollSeconds();
			try {
				Thread.sleep(expiredMillis + pollSecond * 1000);
			} catch (InterruptedException e) {
			}
			Assert.assertEquals(cache.size(), 0);
		} else {
			cache.clear();
			Assert.assertEquals(cache.size(), 0);
		}
		Assert.assertFalse(cacheMapImpl.isScheduleRunning());
		
		for(String key : keys) {
			Assert.assertFalse(cache.containsKey(key));
		}		
	}
	
	private CacheMapCacheManagerImpl getCacheMapCacheManagerImpl() {
		return (CacheMapCacheManagerImpl)cacheManager;
	}
}
