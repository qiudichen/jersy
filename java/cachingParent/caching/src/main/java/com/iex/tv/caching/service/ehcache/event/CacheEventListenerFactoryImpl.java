package com.iex.tv.caching.service.ehcache.event;

import java.util.Properties;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

public class CacheEventListenerFactoryImpl extends CacheEventListenerFactory {

	@Override
	public CacheEventListener createCacheEventListener(Properties properties) {
		return CacheEventListenerImpl.INSTANCE;
	}

}
