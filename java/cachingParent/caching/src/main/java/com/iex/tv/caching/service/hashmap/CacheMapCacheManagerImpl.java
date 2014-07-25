package com.iex.tv.caching.service.hashmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iex.tv.caching.service.BaseCacheManagerImpl;
import com.iex.tv.caching.service.hashmap.CacheMapImpl.CacheElement;
import com.iex.tv.caching.spi.Cache;

public class CacheMapCacheManagerImpl extends BaseCacheManagerImpl<Map<Object, CacheMapImpl.CacheElement>> {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String name;
	
	private int threadPoolSize;
	
	private ScheduledExecutorService scheduledThreadPool;
	
	private CacheMapConfigs configs; 
	
	public CacheMapCacheManagerImpl() {
		
	}

	public CacheMapCacheManagerImpl(CacheMapConfigs configs) {
		this.threadPoolSize = configs.getThreadPoolSize();
		this.configs = configs;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	
	public int getThreadPoolSize() {
		if(this.threadPoolSize <= 0) {
			return 1;
		}
		return this.threadPoolSize;
	}
	
	public ScheduledExecutorService getScheduledThreadPool() {
		if(this.scheduledThreadPool == null) {
			scheduledThreadPool = Executors.newScheduledThreadPool(getThreadPoolSize());
		}
		return scheduledThreadPool;
	}

	@Override
	protected Collection<? extends Cache<Map<Object, CacheElement>>> initCaches() {
		if(this.configs == null || this.configs.isEmpty()) {
			return Collections.emptyList();
		}
		
		Set<String> names = new HashSet<String>(this.configs.size());
		
		List<CacheMapImpl> result = new ArrayList<CacheMapImpl>(this.configs.size());
		for(CacheMapConfig config : this.configs.getConfigs()) {
			if(names.add(config.getName())) {
				result.add(new CacheMapImpl(config, this));
			} else {
				//duplicate name
				logger.warn("duplicated CacheMap name: " + config.getName());
			}
		}
		return result;
	}

	@PreDestroy
	public void shutdown() {
		logger.info("Shutting down CacheMap CacheManager");
		Collection<String> names = this.getCacheNames();
		for(String name : names) {
			Cache<Map<Object, CacheElement>> cache = this.getCache(name);
			cache.dispose();
		}
		
		try {
			if(scheduledThreadPool != null) {
				scheduledThreadPool.shutdownNow();
				scheduledThreadPool = null;
			}
		} catch(Exception e) {
			logger.warn("Shutting down CacheMap CacheManager error.", e);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected void removeInternal(String cacheName) {
		super.clear(cacheName);
	}

	@Override
	protected void removeAllInternal() {
		super.clear();
	}

	@Override
	protected Cache<Map<Object, CacheElement>> addCacheInternal(String cacheName) {
		CacheMapConfig config = this.configs.getDefaultCacheMapConfig(cacheName);
		return new CacheMapImpl(config, this);
	}
}
