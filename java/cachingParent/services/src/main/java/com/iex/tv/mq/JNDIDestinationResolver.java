package com.iex.tv.mq;

import java.util.Hashtable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.cxf.transport.jms.util.DestinationResolver;
import org.apache.cxf.transport.jms.util.ResourceCloser;

public class JNDIDestinationResolver implements DestinationResolver {

	@Override
	public Destination resolveDestinationName(Session session,
			String destinationName, boolean pubSubDomain) throws JMSException {
		Destination destination = null;
		try {
			destination = lookup(destinationName, Destination.class);
			return destination;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        if (pubSubDomain) {
        	destination = session.createTopic(destinationName);
        } else {
        	destination = session.createQueue(destinationName);
        }
		return destination;
	}

    public <T> T lookup(final String name, Class<T> requiredType) throws NamingException {
        Context ctx = createInitialContext();
        try {
            Object located = ctx.lookup(name);
            if (located == null) {
                throw new NameNotFoundException("JNDI object with [" + name + "] not found");
            }
            return (T)located;
        } finally {
            ResourceCloser.close(ctx);
        }
    }	
    
    protected Context createInitialContext() throws NamingException {
        //CHECKSTYLE:OFF
        Hashtable<Object, Object> icEnv = new Hashtable<Object, Object>(2);
        icEnv.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        icEnv.put("java.naming.provider.url", "tcp://localhost:61616");
        return new InitialContext(icEnv);
}    
}