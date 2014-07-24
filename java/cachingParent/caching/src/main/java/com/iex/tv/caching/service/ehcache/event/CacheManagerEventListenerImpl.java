package com.iex.tv.caching.service.ehcache.event;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Status;
import net.sf.ehcache.event.CacheManagerEventListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CacheManagerEventListenerImpl implements CacheManagerEventListener {
	protected final Log logger = LogFactory.getLog(getClass());
	
	public static final CacheManagerEventListener INSTANCE = new CacheManagerEventListenerImpl();
	
	private Status status;
	
	private CacheManagerEventListenerImpl() {
		status = Status.STATUS_UNINITIALISED;
	}
	
	@Override
	public void dispose() throws CacheException {
		logger.info("CacheManagerEventListenerImpl.dispose()");
		status = Status.STATUS_SHUTDOWN;
	}

	@Override
	public Status getStatus() {
		logger.info("CacheManagerEventListenerImpl.getStatus()");
		return status;
	}

	@Override
	public void init() throws CacheException {
		logger.info("CacheManagerEventListenerImpl.init()");
		status = Status.STATUS_ALIVE;
	}

	@Override
	public void notifyCacheAdded(String cacheName) {
		logger.info("Cache[" + cacheName + "] is added.");
	}

	@Override
	public void notifyCacheRemoved(String cacheName) {
		logger.info("Cache[" + cacheName + "] is removed.");
	}

}
