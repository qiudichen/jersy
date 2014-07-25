package com.iex.tv.caching.service.ehcache.hibernate;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.distribution.jms.AcknowledgementMode;
import net.sf.ehcache.distribution.jms.JMSCacheLoader;
import net.sf.ehcache.loader.CacheLoader;
import net.sf.ehcache.loader.CacheLoaderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JMSCacheLoaderFactory extends CacheLoaderFactory {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private String[] contextLocations;
	
	private AcknowledgementMode acknowledgementMode;
	
	private ApplicationContext context = null;
	
	private int timeoutMillis = 0;
	
	private Queue queue = null;
	
	private QueueConnectionFactory queueFactory = null;
	
	private String userName = null;
	private String password = null;
	private String defaultLoaderArgument = null;
	
	@Override
	public CacheLoader createCacheLoader(Ehcache cache, Properties properties) {
		init(properties);
		
		QueueConnection queueConnection;
		try {
			queueConnection = JMSUtil.createQueueConnection(userName, password, this.queueFactory);
			return new JMSCacheLoader(cache, this.defaultLoaderArgument, queueConnection, this.queue, this.acknowledgementMode, this.timeoutMillis);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} catch(BeansException e) {
			logger.error(e);
			throw e;
		}
		
	}

	private void init(Properties properties) {
		if(this.context != null) {
			return;
		}
		
		ApplicationContext context = getContext(properties);
		
		ConnectionFactory factory = null;
		try {
			factory = JMSUtil.getBean(context, JMSUtil.EHCACHE_CONNECTION_FACTORY, ConnectionFactory.class);
		} catch(Exception e) {
			//ignore
		}
		
		if(factory == null) {
			try {
				this.queueFactory = JMSUtil.getBean(context, JMSUtil.EHCACHE_QUEUE_CONNECTION_FACTORY, QueueConnectionFactory.class);
			} catch(Exception e) {
				//ignore
			}
		} else {
			this.queueFactory = (QueueConnectionFactory)factory;
		}
		
		this.queue =  JMSUtil.getBean(context, JMSUtil.EHCACHE_QUEUE, Queue.class);
		this.acknowledgementMode = getAcknowledgementMode(properties);
		this.userName = JMSUtil.getUser(properties);
		if(this.userName != null) {
			this.password = JMSUtil.getPassword(properties);
		}
		this.defaultLoaderArgument = JMSUtil.extractDefaultLoaderArgument(properties);
		this.timeoutMillis = extractTimeoutMillis(properties);
	}
	
    protected int extractTimeoutMillis(Properties properties) {
        if(this.timeoutMillis == 0) {
        	this.timeoutMillis = JMSUtil.extractTimeoutMillis(properties);
        }
        return this.timeoutMillis;
    }	
    
	private ApplicationContext getContext(Properties properties) {
		if(context == null) {
			context = new ClassPathXmlApplicationContext(getSpringConextLocation(properties));
		}
		return context;
	}   
	
	private String[] getSpringConextLocation(Properties properties) {
		if(contextLocations == null) {
			contextLocations = JMSUtil.getSpringConextLocation(properties);
		}
		return contextLocations;
	}
	
	private AcknowledgementMode getAcknowledgementMode(Properties properties) {
		if(acknowledgementMode == null) {
			acknowledgementMode = JMSUtil.getAcknowledgementMode(properties);
		} 
		return acknowledgementMode;
	}	
	

}
