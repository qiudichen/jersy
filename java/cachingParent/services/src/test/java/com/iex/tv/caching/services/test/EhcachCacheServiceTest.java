package com.iex.tv.caching.services.test;

import java.util.Collection;

import net.sf.ehcache.Ehcache;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.iex.tv.caching.service.ehcache.EhCacheCacheManagerImpl;
import com.iex.tv.caching.spi.Cache;

@ContextConfiguration(locations = { "classpath:/ehcache-core-context.xml"})
public class EhcachCacheServiceTest extends BaseCachingServiceTest {
	private EhCacheCacheManagerImpl getEhCacheCacheManagerImpl() {
		return (EhCacheCacheManagerImpl)cacheManager;
	}
	
	@Test
	public void jmsNotification() {
		EhCacheCacheManagerImpl cacheManager = getEhCacheCacheManagerImpl();
		Collection<String> names = cacheManager.getCacheNames();
		for(String name : names) {
			Cache<Ehcache> cache = cacheManager.getCache(name);
			
			String key = "123";
			if(cache.containsKey(key)) {
				Object value = cache.getValue(key);
			} else {
				cache.put(key, "New Value is added for key 123");
			}
			
			if(cache.containsKey(key)) {
				cache.remove(key);
			} else {
				cache.put(key, "New Value is added for key 123");
			}
						
		}
	}
}
