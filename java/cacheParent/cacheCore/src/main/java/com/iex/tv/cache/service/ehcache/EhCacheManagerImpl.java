package com.iex.tv.cache.service.ehcache;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.management.MBeanServer;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import net.sf.ehcache.management.ManagementService;

import com.iex.tv.cache.service.AbstractCacheManager;
import com.iex.tv.cache.spi.Cache;

public class EhCacheManagerImpl extends AbstractCacheManager<Ehcache> {
	private net.sf.ehcache.CacheManager cacheManager;

	private boolean registerMBeans;

	public EhCacheManagerImpl() {
		
	}
	
	public EhCacheManagerImpl(String cacheManagerName) {
		super(cacheManagerName);
	}
	
	public EhCacheManagerImpl(String cacheManagerName, net.sf.ehcache.CacheManager cacheManager) {
		super(cacheManagerName);
		this.cacheManager = cacheManager;
	}

	public boolean isRegisterMBeans() {
		return registerMBeans;
	}

	public void setRegisterMBeans(boolean registerMBeans) {
		this.registerMBeans = registerMBeans;
	}

	public void setCacheManager(net.sf.ehcache.CacheManager cacheManager) {
		assert cacheManager != null : "Ehcache cacheManager must be not null.";
		this.cacheManager = cacheManager;
	}
	
	private void registerMBeans(net.sf.ehcache.CacheManager cacheManager) {
		if(this.registerMBeans) {
			try {
				MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
				ManagementService.registerMBeans(cacheManager, mBeanServer, false, false, false, true);
			} catch (Exception e) {
				logger.warn("Unable to register cacheManager.", e);
			}
		}
	}

	@Override
	public void shutdown() {
		this.cacheManager.shutdown();
		super.shutdown();
	}

	@Override
	protected Collection<? extends Cache<Ehcache>> initCaches() {
		registerMBeans(this.cacheManager);
		Status status = this.cacheManager.getStatus();
		assert Status.STATUS_ALIVE.equals(status) : "EhCache CacheManager is not alive - current cache is " + status.toString();
		
		String[] names = this.cacheManager.getCacheNames();
		Collection<EhCacheImpl> caches = new LinkedHashSet<EhCacheImpl>(names.length);
		for (String cacheName : names) {
			Ehcache ehcache = this.cacheManager.getEhcache(cacheName);
			caches.add(new EhCacheImpl(this.getCacheManagerName(), cacheName, ehcache));
		}
		return caches;
	}

	@Override
	protected Cache<Ehcache> createNativeCache(String cacheName) {
		try {
			cacheManager.addCache(cacheName);
			Ehcache echcache = cacheManager.getEhcache(cacheName);
			return new EhCacheImpl(this.getCacheManagerName(), cacheName, echcache);
		} catch(Exception e) {
			logger.warn("Unable to create ehcache with name: " + cacheName, e);
		}
		return null;
	}

	@Override
	protected void removeNativeCache(String cacheName) {
		cacheManager.removeCache(cacheName);
	}

	@Override
	protected void removeAllNativeCaches() {
		cacheManager.removalAll();
	}
}
