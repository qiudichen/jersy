package com.iex.tv.caching.test;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iex.tv.caching.spi.Cache;
import com.iex.tv.caching.spi.CacheManager;
import com.iex.tv.caching.test.data.CacheData;
import com.iex.tv.caching.test.data.CacheTestData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-test-context.xml"})
public abstract class BaseCachingTest {
	@Resource(name="cacheManager")
	protected CacheManager<?> cacheManager;
	
	@Resource(name="cacheTestData")
	protected CacheTestData cacheTestData;

	protected Set<String> initCache(Cache<?> cache) {
		//cache.clear();
		CacheData cacheData = cacheTestData.getCacheData(cache.getName());
		if(cacheData == null) {
			return Collections.emptySet();
		}
		
		Set<String> keys = cacheData.getKes();
		for(String key : keys) {
			Object value = cacheData.getValue(key);
			cache.put(key, value);
		}
		return keys;
	}
}
