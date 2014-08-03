package com.iex.tv.caching.services.test;

import java.util.Collection;

import junit.framework.Assert;
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

			String keyPref = name + "-key-";
			String valuePref = name + "-value-";
			
			//JVM 1 put, JVM 2 get
			for(int i = 0; i < 5; i++) {
				String key = keyPref + i;
				String value = valuePref + i;
				if(cache.containsKey(key)) {
					Object cachedValue = cache.getValue(key);
					Assert.assertEquals(cachedValue, value);
				} else {
					cache.put(key, value);
				}				
			}
			
			try {
				Thread.sleep(1000000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//get updated in JVM 2
			for(int i = 0; i < 5; i++) {
				String key = keyPref + i;
				String value = valuePref + i;
				if(cache.containsKey(key)) {
					Object cachedValue = cache.getValue(key);
					Assert.assertTrue(((String)cachedValue).startsWith(value));
				}			
			}
			
			//update cache in JVM 1
			for(int i = 0; i < 5; i++) {
				String key = keyPref + i;
				String value = valuePref + i + " updated";
				cache.put(key, value);
			}
			
			//remove get cache in JVM 2
			for(int i = 0; i < 5; i++) {
				String key = keyPref + i;
				String value = valuePref + i;
				if(cache.containsKey(key)) {
					Object cachedValue = cache.getValue(key);
					Assert.assertTrue(((String)cachedValue).startsWith(value));
				}			
			}
			
			//remove cache in JVM1
			for(int i = 0; i < 5; i++) {
				String key = keyPref + i;
				cache.remove(key);
			}
		}
	}
}
