package com.iex.tv.caching.service.ehcache.hibernate;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import net.sf.ehcache.distribution.jms.Action;
import net.sf.ehcache.distribution.jms.JMSEventMessage;

import com.iex.tv.caching.spi.JMSCacheMessageBuilder;

public class JMSCacheMessageBuilderImpl implements JMSCacheMessageBuilder {

	@Override
	public Message createPutMessage(Session session, String cacheName,
			String keyValue, Serializable object) throws JMSException {
		assert cacheName != null : "cacheName cannot be null.";
		assert keyValue != null : "keyValue cannot be null.";
		assert object != null : "object cannot be null.";
		
    	ObjectMessage message = session.createObjectMessage(object);
    	addMessageHeader(message, cacheName, keyValue, Action.PUT);
        return message;
	}

	@Override
	public Message createRemoveMessage(Session session, String cacheName,
			String keyValue)  throws JMSException {
		assert cacheName != null : "cacheName cannot be null.";
		assert keyValue != null : "keyValue cannot be null.";
    	ObjectMessage message = session.createObjectMessage();
    	addMessageHeader(message, cacheName, keyValue, Action.REMOVE);
    	return message;
	}

	@Override
	public Message createClearMessage(Session session, String cacheName)  throws JMSException {
		assert cacheName != null : "cacheName cannot be null.";
    	ObjectMessage message = session.createObjectMessage();
    	addMessageHeader(message, cacheName, null, Action.REMOVE_ALL);
        return message;
	}

	private void addMessageHeader(Message message, String cacheName, String keyValue, Action action) throws JMSException {
		message.setStringProperty(JMSEventMessage.ACTION_PROPERTY, action.toString());
		message.setStringProperty(JMSEventMessage.CACHE_NAME_PROPERTY, cacheName);
		if(keyValue != null) {
			message.setStringProperty(JMSEventMessage.KEY_PROPERTY, keyValue);
		}
	}	
}
