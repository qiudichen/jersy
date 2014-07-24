package com.iex.tv.caching.jms;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.iex.tv.caching.spi.CacheNotifier;
import com.iex.tv.caching.spi.JMSCacheMessageBuilder;

public class JMSCacheNotifierImpl implements CacheNotifier {

	@Resource(name="jmsEhcacheTemplate")
    private JmsTemplate template;
	
	private JMSCacheMessageBuilder messageBuilder;
	
	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public void setMessageBuilder(JMSCacheMessageBuilder messageBuilder) {
		this.messageBuilder = messageBuilder;
	}

	@Override
	public void put(final String cacheName, final String keyValue, final Serializable object) {
		template.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return messageBuilder.createPutMessage(session, cacheName, keyValue, object);
            }
        });		
	}

	@Override
	public void removeObject(final String cacheName, final String keyValue) {
		template.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return messageBuilder.createRemoveMessage(session, cacheName, keyValue);
            }
        });	
	}

	@Override
	public void clear(final String cacheName) {
		template.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return messageBuilder.createClearMessage(session, cacheName);
            }
        });		
	}
}
