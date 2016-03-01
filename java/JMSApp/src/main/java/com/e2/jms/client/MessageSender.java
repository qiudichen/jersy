package com.e2.jms.client;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.logging.log4j.LogManager;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.e2.web.jms.SimpleObject;

public class MessageSender implements Runnable {
	private static Logger logger = LogManager.getLogger(MessageSender.class);
	
	private static String buf = "AAAAAAAAAABBBBBBBBBBCCCCCCCCCCDDDDDDDDDDEEEEEEEEEE" +
			"AAAAAAAAAABBBBBBBBBBCCCCCCCCCCDDDDDDDDDDEEEEEEEEEE" +
			"AAAAAAAAAABBBBBBBBBBCCCCCCCCCCDDDDDDDDDDEEEEEEEEEE" +
			"AAAAAAAAAABBBBBBBBBBCCCCCCCCCCDDDDDDDDDDEEEEEEEEEE" +
			"AAAAAAAAAABBBBBBBBBBCCCCCCCCCCDDDDDDDDDDEEEEEEEEEE" +
			"AAAAAAAAAABBBBBBBBBBCCCCCCCCCCDDDDDDDDDDEEEEEEEEEE";
	private Object lockObject;
	
	private JmsTemplate template;
	
	private int maxMessage;
	
	private String prefix;
	
	public MessageSender(JmsTemplate template, int maxMessage, String prefix, Object lockObject) {
		this.template = template;
		this.maxMessage = maxMessage;
		this.prefix = prefix;
		this.lockObject = lockObject;
	}
	
	public void run() {
		for(int i = 0; i < maxMessage; i++) {
			process(this.prefix);
			//System.out.println("Sender " + prefix + " send message " + i);
		}
		
		System.out.println("Sender " + prefix + " completed. ");
		try {
			synchronized( lockObject ) 
			{ 
				lockObject.notify();
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void process(final String msg) {
		try {
			template.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					long start = System.currentTimeMillis();
					SimpleObject platformSession = new SimpleObject(start, buf);
					ObjectMessage objMsg = session.createObjectMessage();
					objMsg.setObject(platformSession);
					return objMsg;
				}
			});			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	public static JmsTemplate getJmsTemplate() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL("tcp://localhost:61616");
		factory.setTrustAllPackages(true);
		CachingConnectionFactory cachedFactory = new CachingConnectionFactory();
		cachedFactory.setTargetConnectionFactory(factory);
		cachedFactory.setSessionCacheSize(10);
		cachedFactory.setReconnectOnException(true);
		
		ActiveMQQueue destination = new ActiveMQQueue("nice.rest.provider");
		JmsTemplate jmsTemplate = new JmsTemplate();
		
		jmsTemplate.setConnectionFactory(cachedFactory);
		jmsTemplate.setDefaultDestination(destination);
		return jmsTemplate;
	}
	
	private static boolean isRunning(Thread[] threads) {
		for(Thread thread : threads) {
			if(thread.isAlive()) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String args[]) {
		logger.info("Entering application.");
		logger.warn("Entering application.");
		logger.debug("Entering application.");
		logger.error("Entering application.");

		Object lockObject = new Object();
		
		int threadSize = 10;
		int messageSize = 1000;
		String prefix = "msg";
		
		AtomicInteger count = new AtomicInteger(threadSize);
		
		Thread[] threads = new Thread[threadSize];
		for(int i = 0; i < threadSize; i++) {
			threads[i] = new Thread(new MessageSender(getJmsTemplate(), messageSize, prefix + i, lockObject));
		}

		long start = System.currentTimeMillis();
		for(int i = 0; i < threadSize; i++) {
			threads[i].start();;
		}
		
		synchronized( lockObject )
		{ 
		    while(true)
		    { 
		        try {
					lockObject.wait(10000);
					if(!isRunning(threads)) {
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		}
		start = System.currentTimeMillis() - start;
		System.out.println("================= Durating ===================== " + start);
		System.exit(0);
    }
	
}
