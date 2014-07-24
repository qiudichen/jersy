package com.iex.tv.caching.service.ehcache.hibernate;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import org.springframework.context.ApplicationContext;

import net.sf.ehcache.distribution.jms.AcknowledgementMode;
import net.sf.ehcache.util.PropertyUtil;

public class JMSUtil {
	public final static String DEFAULT_CONTEXT = "ehcache-jms-context.xml";
	
	public final static String JMS_CONTEXT_LOCATION = "contextConfigLocation";
	public final static String JMS_CONTEXT_MODE = "AcknowledgementMode";
	public final static String JMS_CONTEXT_LISTEN_TOPIC = "listenToTopic";

	public final static String EHCACHE_USER = "ehcacheJMSUser";
	public final static String EHCACHE_PASSWORD = "ehcacheJMSPassword";

	public final static String EHCACHE_TOPIC = "ehcacheJMSTopic";
	public final static String EHCACHE_QUEUE = "ehcacheJMSQueue";
	public final static String EHCACHE_CONNECTION_FACTORY = "ehcacheJMSConnectionFactory";
	public final static String EHCACHE_QUEUE_CONNECTION_FACTORY = "ehcacheJMSQueueConnectionFactory";
	public final static String EHCACHE_TOPIC_CONNECTION_FACTORY = "ehcacheJMSTopicConnectionFactory";
	
	public static final String DEFAULT_LOADER_ARGUMENT = "defaultLoaderArgument";
	public static final String TIMEOUT_MILLIS = "timeoutMillis";  
    protected static final int DEFAULT_TIMEOUT_INTERVAL_MILLIS = 30000;
    
	public static AcknowledgementMode getAcknowledgementMode(Properties properties) {
		String value = extractProperties(JMS_CONTEXT_MODE, properties); 
		
		AcknowledgementMode mode = AcknowledgementMode.forString(value);
		return (mode != null) ? mode : AcknowledgementMode.AUTO_ACKNOWLEDGE;		
	}
	
	public static String[] getSpringConextLocation(Properties properties) {
		String[] contextLocations;
		String value = extractProperties(JMS_CONTEXT_LOCATION, properties);
		if(value == null || value.isEmpty()) {
			contextLocations = new String[]{DEFAULT_CONTEXT};
		} else {
			if(value.contains(" ")) {
				contextLocations = value.split(" ");
			} else {
				contextLocations = new String[]{value};
			}
		}
		return contextLocations;
	}	
	
	public static boolean isListToTopic(Properties properties) {
		String value =  extractProperties(JMS_CONTEXT_LISTEN_TOPIC, properties);
		if(value != null && value.equalsIgnoreCase("false")) {
			return false;
		}
		return true;
	}	
	
	public static String getUser(Properties properties) {
		return extractProperties(EHCACHE_USER, properties);
	}	
	
	public static String getPassword(Properties properties) {
		return extractProperties(EHCACHE_PASSWORD, properties);
	}	
	
	 /**
     * Extracts the value of timeoutMillis. Sets it to 30000ms if
     * either not set or there is a problem parsing the number
     *
     * @param properties
     */
	public static int extractTimeoutMillis(Properties properties) {
        int timeoutMillis = 0;
        String timeoutMillisString =
                PropertyUtil.extractAndLogProperty(TIMEOUT_MILLIS, properties);
        if (timeoutMillisString != null) {
            try {
                timeoutMillis = Integer.parseInt(timeoutMillisString);
            } catch (NumberFormatException e) {
                timeoutMillis = DEFAULT_TIMEOUT_INTERVAL_MILLIS;
            }
        } else {
            timeoutMillis = DEFAULT_TIMEOUT_INTERVAL_MILLIS;
        }
        return timeoutMillis;
    }	
	
	public static <T> T getBean(ApplicationContext context, String name, Class<T> cls) {
		return (T)context.getBean(name);
	}
		
	public static TopicConnection createTopicConnection(String userName,
			String password, TopicConnectionFactory topicFactory) throws JMSException {
		TopicConnection topicConnection;
		if (userName != null) {
			topicConnection = topicFactory.createTopicConnection(userName, password);
		} else {
			topicConnection = topicFactory.createTopicConnection();
		}
		return topicConnection;
	}
	
	public static QueueConnection createQueueConnection(String userName,
			String password, QueueConnectionFactory queueConnectionFactory)
			throws JMSException {
		QueueConnection queueConnection;
		if (userName != null) {
			queueConnection = queueConnectionFactory.createQueueConnection(
					userName, password);
		} else {
			queueConnection = queueConnectionFactory.createQueueConnection();
		}
		return queueConnection;
	}	
	
    public static String extractDefaultLoaderArgument(Properties properties) {
        return extractProperties(DEFAULT_LOADER_ARGUMENT, properties);
    }	

    public static String extractProperties(String name, Properties properties) {
    	if (properties == null || properties.size() == 0) {
    		return null;
    	}
    	
    	String foundValue = (String) properties.get(name);
    	if (foundValue != null) {
    		foundValue = foundValue.trim();
    	}
    	return foundValue;
    }	
}
