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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JMSCacheManagerPeerProviderFactoryImpl extends
		CacheManagerPeerProviderFactory {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private final static String DEFAULT_CONTEXT = "ehcache-jms-context.xml";
	
	private final static String JMS_CONTEXT_LOCATION = "contextConfigLocation";
	private final static String JMS_CONTEXT_MODE = "AcknowledgementMode";
	private final static String JMS_CONTEXT_LISTEN_TOPIC = "listenToTopic";

	private final static String EHCACHE_TOPIC = "ehcacheJMSTopic";
	private final static String EHCACHE_QUEUE = "ehcacheJMSQueue";
	private final static String EHCACHE_CONNECTION_FACTORY = "ehcacheJMSConnectionFactory";
	private final static String EHCACHE_QUEUE_CONNECTION_FACTORY = "ehcacheJMSQueueConnectionFactory";
	private final static String EHCACHE_TOPIC_CONNECTION_FACTORY = "ehcacheJMSTopicConnectionFactory";
	
	private String[] contextLocations;
	
	@Override
	public CacheManagerPeerProvider createCachePeerProvider(CacheManager cacheManager, Properties properties) {
		
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext(getSpringConextLocation(properties));
			
			ConnectionFactory factory = this.getBean(context, EHCACHE_CONNECTION_FACTORY, ConnectionFactory.class);
			QueueConnectionFactory queueFactory = this.getBean(context, EHCACHE_QUEUE_CONNECTION_FACTORY, QueueConnectionFactory.class);
			TopicConnectionFactory topicFactory = this.getBean(context, EHCACHE_TOPIC_CONNECTION_FACTORY, TopicConnectionFactory.class);
			
				TopicConnection replicationTopicConnection = createTopicConnection(factory, topicFactory);
			QueueConnection queueConnection = createQueueConnection(factory, queueFactory) ;
	
			Topic replicationTopic =  this.getBean(context, EHCACHE_TOPIC, Topic.class);
			Queue queue =  this.getBean(context, EHCACHE_QUEUE, Queue.class);
			
			AcknowledgementMode acknowledgementMode = getAcknowledgementMode(properties);
			
			boolean listenToTopic = isListToTopic(properties);
			
			return new JMSCacheManagerPeerProvider(cacheManager, replicationTopicConnection, replicationTopic, queueConnection,  queue, acknowledgementMode, listenToTopic);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
	
	private QueueConnection createQueueConnection(ConnectionFactory factory, QueueConnectionFactory queueFactory) throws JMSException {
		if(queueFactory == null) {
			return ((QueueConnectionFactory)factory).createQueueConnection();
		} 
		return queueFactory.createQueueConnection();
	}

	private TopicConnection createTopicConnection(ConnectionFactory factory, TopicConnectionFactory topicFactory) throws JMSException {
		if(topicFactory == null) {
			return ((TopicConnectionFactory)factory).createTopicConnection();
		}
		return topicFactory.createTopicConnection();
	}
	
	private <T> T getBean(ApplicationContext context, String name, Class<T> cls) {
		return (T)context.getBean(name);
	}
	
	private boolean isListToTopic(Properties properties) {
		String value = properties.getProperty(JMS_CONTEXT_LISTEN_TOPIC);
		if(value != null && value.equalsIgnoreCase("false")) {
			return false;
		}
		return true;
	}
	
	private AcknowledgementMode getAcknowledgementMode(Properties properties) {
		String value = properties.getProperty(JMS_CONTEXT_MODE);
		AcknowledgementMode mode = AcknowledgementMode.forString(value);
		return (mode != null) ? mode : AcknowledgementMode.AUTO_ACKNOWLEDGE;		
	}
	
	private String[] getSpringConextLocation(Properties properties) {
		if(contextLocations == null) {
			String value = properties.getProperty(JMS_CONTEXT_LOCATION);
			if(value == null || value.isEmpty()) {
				contextLocations = new String[]{DEFAULT_CONTEXT};
			} else {
				contextLocations = new String[]{value};
			}
		}
		return contextLocations;
	}
}
