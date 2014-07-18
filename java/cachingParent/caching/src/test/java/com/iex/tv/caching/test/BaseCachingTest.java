package com.iex.tv.caching.test;

import java.util.Collection;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iex.tv.caching.spi.Cache;
import com.iex.tv.caching.spi.CacheManager;
import com.iex.tv.caching.test.data.CacheTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-test-context.xml"})
public abstract class BaseCachingTest {
	//@Resource(name="cacheManager")
	protected CacheManager<?> cacheManager;
	
	//@Resource(name="cacheTestData")
	protected CacheTestData data;
	
	@Test
	public void standardAPIs() {
		Collection<String> names = cacheManager.getCacheNames();
		Assert.assertNotNull(names);
		
		for(String name : names) {
			Cache<?> cache = cacheManager.getCache(name);
			Assert.assertNotNull(cache);
			initCache(cache);
		}		
	}	
	
	protected void initCache(Cache<?> cache) {
		cache.clear();
		//cache.put(key, value);
	}
}
