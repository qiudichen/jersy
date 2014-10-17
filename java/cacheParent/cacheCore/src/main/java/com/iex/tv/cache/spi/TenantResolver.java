package com.iex.tv.cache.spi;

import java.util.Properties;

/**
 * Strategy interface used by a CacheManagerFactory representing a target Tenant to hold 
 * tenant Id and tenant's properties for CacheManager.
 * 
 * <p><code>TenantResolver</code> implementations are expected to be thread-safe.
 * One <code>TenantResolver</code> instance can be used with multiple bean factories
 * at the same time, if desired (unless it explicitly wants to be aware of
 * the containing BeanFactory), with any number of threads accessing
 * the <code>TenantResolver</code> concurrently from any number of factories.
 * 
 * In multiple cache manager environment, Cache Manager Name + Cache Name should be unique.
 * @author dchen
 *
 */
public interface TenantResolver {
	
	/**
	 * Return the <em>conversation ID</em> for the current underlying Tenant.
	 * 
	 */	
	String getConversationId();
	
	/**
	 * Return the <em>tenant's properties</em> for the current underlying Tenant.
	 * 
	 */		
	Properties getCacheProperties();
}
