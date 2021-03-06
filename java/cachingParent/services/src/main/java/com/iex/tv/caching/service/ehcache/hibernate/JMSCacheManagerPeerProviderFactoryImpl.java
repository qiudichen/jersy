package com.iex.tv.caching.service.ehcache.hibernate;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CacheManagerPeerProviderFactory;
import net.sf.ehcache.distribution.jms.AcknowledgementMode;
import net.sf.ehcache.distribution.jms.JMSCacheManagerPeerProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JMSCacheManagerPeerProviderFactoryImpl extends
		CacheManagerPeerProviderFactory {
	protected final Log logger = LogFactory.getLog(getClass());

	private String[] contextLocations;
	
	private AcknowledgementMode acknowledgementMode;
	
	private ApplicationContext context = null;

	private QueueConnectionFactory queueFactory = null;
	
	private TopicConnectionFactory topicFactory = null;
	
	private Topic replicationTopic = null;
	
	private Queue queue = null;

	private String userName = null;
	
	private String password = null;
	
	@Override
	public CacheManagerPeerProvider createCachePeerProvider(CacheManager cacheManager, Properties properties) {
		
		try {
			init(properties);

			TopicConnection replicationTopicConnection = JMSUtil.createTopicConnection(this.userName, this.password, this.topicFactory);
			QueueConnection queueConnection = JMSUtil.createQueueConnection(this.userName, this.password, this.queueFactory) ;

			boolean listenToTopic = JMSUtil.isListToTopic(properties);
			return new JMSCacheManagerPeerProvider(cacheManager, replicationTopicConnection, this.replicationTopic, queueConnection,  this.queue, this.acknowledgementMode, listenToTopic);
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

			try {
				this.topicFactory = JMSUtil.getBean(context, JMSUtil.EHCACHE_TOPIC_CONNECTION_FACTORY, TopicConnectionFactory.class);
			} catch(Exception e) {
				//ignore
			}
		} else {
			this.queueFactory = (QueueConnectionFactory)factory;
			this.topicFactory = (TopicConnectionFactory)factory;
		}
		
		this.replicationTopic =  JMSUtil.getBean(context, JMSUtil.EHCACHE_TOPIC, Topic.class);
		this.queue =  JMSUtil.getBean(context, JMSUtil.EHCACHE_QUEUE, Queue.class);
		this.acknowledgementMode = getAcknowledgementMode(properties);
		
		this.userName = JMSUtil.getUser(properties);
		if(this.userName != null) {
			this.password = JMSUtil.getPassword(properties);
		}		
	}
	
	private AcknowledgementMode getAcknowledgementMode(Properties properties) {
		if(acknowledgementMode == null) {
			acknowledgementMode = JMSUtil.getAcknowledgementMode(properties);
		} 
		return acknowledgementMode;
	}
	
	private String[] getSpringConextLocation(Properties properties) {
		if(contextLocations == null) {
			contextLocations = JMSUtil.getSpringConextLocation(properties);
		}
		return contextLocations;
	}
	
	private ApplicationContext getContext(Properties properties) {
		if(context == null) {
			context = new ClassPathXmlApplicationContext(getSpringConextLocation(properties));
		}
		return context;
	}
}
