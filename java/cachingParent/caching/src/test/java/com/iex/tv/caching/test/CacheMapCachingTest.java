package com.iex.tv.caching.test;

import org.springframework.test.context.ContextConfiguration;

import com.iex.tv.caching.service.hashmap.CacheMapCacheManagerImpl;

//@ContextConfiguration(locations = { "classpath:/cachemap-core-context.xml"})
public class CacheMapCachingTest extends BaseCachingTest {
	private CacheMapCacheManagerImpl getCacheMapCacheManagerImpl() {
		return (CacheMapCacheManagerImpl)cacheManager;
	}
}
