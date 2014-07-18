package com.iex.tv.caching.test;

import java.util.Collection;

import junit.framework.Assert;
import net.sf.ehcache.Ehcache;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.iex.tv.caching.service.ehcache.EhCacheCacheManagerImpl;
import com.iex.tv.caching.spi.Cache;

@ContextConfiguration(locations = { "classpath:/ehcache-core-context.xml"})
public class EhcachCacheTest extends BaseCachingTest {

	private EhCacheCacheManagerImpl getEhCacheCacheManagerImpl() {
		return (EhCacheCacheManagerImpl)cacheManager;
	}
}
