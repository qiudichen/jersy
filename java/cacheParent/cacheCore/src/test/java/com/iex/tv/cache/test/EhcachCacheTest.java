package com.iex.tv.cache.test;

import java.util.Collection;
import java.util.Set;

import junit.framework.Assert;
import net.sf.ehcache.Element;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.iex.tv.cache.service.ehcache.EhCacheImpl;
import com.iex.tv.cache.service.ehcache.EhCacheManagerImpl;
import com.iex.tv.cache.spi.Cache;

@ContextConfiguration(locations = { "classpath:/ehcache-core-context.xml"})
public class EhcachCacheTest extends BaseCachingTest {

	@Test
	public void addDataRemoveData() {
		EhCacheManagerImpl cacheManager = EhCacheManagerImpl();
		
		Collection<String> names = cacheManager.getCacheNames();

		for(String name : names) {
			Cache<?> cache = cacheManager.getCache(name);
			Assert.assertNotNull(cache);
			cacheData(cache); 
		}		
	}
	
	private void cacheData(Cache<?> cache) {
		Set<String> keys = initCache(cache);
		Assert.assertEquals(cache.size(), keys.size());
		
		int size = keys.size();
		boolean remove = true;
		
		EhCacheImpl cacheMapImpl = (EhCacheImpl)cache;
		Assert.assertEquals(cache.size(), size);
		Assert.assertEquals(cacheMapImpl.getKeys().size(), keys.size());
		
		for(String key : keys) {
			Assert.assertTrue(cache.containsKey(key));
			
			 Element cacheElementBeforeGet = cacheMapImpl.getCacheElement(key);
					 
			long hitsBeforeGet = cacheElementBeforeGet.getHitCount();
			long lastAccessedTimeBeforeGet = cacheElementBeforeGet.getLastAccessTime();
			
			Object value = cacheMapImpl.getValue(key);
			Assert.assertNotNull(value);
			
			Element cacheElementAfterGet = cacheMapImpl.getCacheElement(key);				
			long hitsAfterGet = cacheElementAfterGet.getHitCount();
			long lastAccessedTimeAfterGet = cacheElementAfterGet.getLastAccessTime();
			
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

		for(String key : keys) {
			Assert.assertFalse(cache.containsKey(key));
		}			
	}
	
	private EhCacheManagerImpl EhCacheManagerImpl() {
		return (EhCacheManagerImpl)cacheManager;
	}
}
