package com.iex.tv.caching.service.ehcache.event;

import java.util.Properties;

import net.sf.ehcache.event.CacheManagerEventListener;
import net.sf.ehcache.event.CacheManagerEventListenerFactory;

public class CacheManagerEventListenerFactoryImpl extends
		CacheManagerEventListenerFactory {

	@Override
	public CacheManagerEventListener createCacheManagerEventListener(
			Properties properties) {
		return CacheManagerEventListenerImpl.INSTANCE;
	}

}
