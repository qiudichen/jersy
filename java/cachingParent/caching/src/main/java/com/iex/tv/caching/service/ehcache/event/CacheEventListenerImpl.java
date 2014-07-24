package com.iex.tv.caching.service.ehcache.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

public class CacheEventListenerImpl implements CacheEventListener {
	protected final Log logger = LogFactory.getLog(getClass());
	
	public static final CacheEventListener INSTANCE = new CacheEventListenerImpl();
	
	private CacheEventListenerImpl() {
		
	}
	
	@Override
	public void notifyElementRemoved(final Ehcache cache, final Element element)
			throws CacheException {
		logger.info("Element[" + element.getKey() + "] is removed from cache[" + cache.getName() + "]");
	}

	@Override
	public void notifyElementPut(final Ehcache cache, final Element element)
			throws CacheException {
		logger.info("Element[" + element.getKey() + "] is put to cache[" + cache.getName() + "]");
	}

	@Override
	public void notifyElementUpdated(final Ehcache cache, final Element element)
			throws CacheException {
		logger.info("Element[" + element.getKey() + "] is update in cache[" + cache.getName() + "]");
	}

	@Override
	public void notifyElementExpired(final Ehcache cache, final Element element) {
		logger.info("Element[" + element.getKey() + "] is expired in cache[" + cache.getName() + "]");
	}

	@Override
	public void notifyElementEvicted(final Ehcache cache, final Element element) {
		logger.info("Element[" + element.getKey() + "] is removed from cache[" + cache.getName() + "]");
	}

	@Override
	public void notifyRemoveAll(final Ehcache cache) {
		logger.info("Cache[" + cache.getName() + "] is clear");
	}

	@Override
	public void dispose() {
		logger.info("CacheEventListenerImpl.dispose()");
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Singleton instance");
	}	
}
