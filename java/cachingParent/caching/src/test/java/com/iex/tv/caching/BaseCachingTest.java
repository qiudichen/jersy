package com.iex.tv.caching;

import java.util.Collection;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iex.tv.caching.spi.Cache;
import com.iex.tv.caching.spi.CacheManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-test-context.xml"})
public abstract class BaseCachingTest {
	@Resource(name="cacheManager")
	protected CacheManager<?> cacheManager;
	
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
	
	private void initCache(Cache<?> cache) {
		cache.clear();
		//cache.put(key, value);
	}
}
