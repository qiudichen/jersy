package com.iex.tv.caching.spi;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public interface JMSCacheMessageBuilder {
	/**
	 * create a Put JMS Message to notify caches in clustered environment to update cache. 
	 * @param session JMS session
	 * @param cacheName Cache Name
	 * @param keyValue	Value key
	 * @param object	New Version of Object
	 * @return JMS Message Object
	 */
	Message createPutMessage(Session session, final String cacheName, final String keyValue, final Serializable object)  throws JMSException;
	
	/**
	 * create a remove JMS Message to notify caches in clustered environment to remove a value from the cache. 
	 * @param session JMS session
	 * @param cacheName Cache Name
	 * @param keyValue	Value key
	 * @return JMS Message Object
	 */	
	Message createRemoveMessage(Session session, final String cacheName, final String keyValue)  throws JMSException;
	
	/**
	 * create a clear JMS Message to notify caches in clustered environment to remove all values from the cache. 
	 * @param session JMS session
	 * @param cacheName Cache Name
	 * @return JMS Message Object
	 */		
	Message createClearMessage(Session session, final String cacheName) throws JMSException;
}
