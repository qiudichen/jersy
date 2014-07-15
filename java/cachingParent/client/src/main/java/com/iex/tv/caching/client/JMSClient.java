package com.iex.tv.caching.client;

import java.util.Collection;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicSession;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.iex.tv.caching.ws.CachingWSService;

public class JMSClient {
	public static final String MESSAGE_COUNT = "messageCount";
	
	public static void main(String[] args) {

//		try {
//			javax.naming.Context ctx = new javax.naming.InitialContext();
//			// lookup the connection factory
//			javax.jms.QueueConnectionFactory factory = (javax.jms.QueueConnectionFactory)ctx.lookup("ConnectionFactory");
//			// create a new TopicConnection for pub/sub messaging
//			javax.jms.QueueConnection conn = factory.createQueueConnection();
//			// lookup an existing topic
//			javax.jms.Queue mytopic = (javax.jms.Queue)ctx.lookup("MyQueue");
//			// create a new TopicSession for the client
//			javax.jms.QueueSession session = conn.createQueueSession(false,QueueSession.AUTO_ACKNOWLEDGE);
//			// create a new subscriber to receive messages
//			QueueSender sender = session.createSender(mytopic);
//			
//			TextMessage message = session.createTextMessage("sssssss");
//            message.setIntProperty(MESSAGE_COUNT, 1);
//            
//			sender.send(message);
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
		
		
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("cxf-jmsclient-context.xml");
//		JmsTemplate template = (JmsTemplate)appContext.getBean("jmsProducerTemplate");
//
//		template.send(new MessageCreator() {
//            public Message createMessage(Session session) throws JMSException {
//                TextMessage message = session.createTextMessage("ggggggggggggggggggggggggggggggggg");
//                message.setIntProperty(MESSAGE_COUNT, 1);
//                return message;
//            }
//        });		
//		
		CachingWSService service = (CachingWSService)appContext.getBean("cachingServiceClient");
		Collection<String> names = service.getCachingNames();		
		for(String name : names) {
			System.out.println(name);
		}
		appContext.close();
	}
}
