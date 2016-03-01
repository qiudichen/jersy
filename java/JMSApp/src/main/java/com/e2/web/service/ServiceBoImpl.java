package com.e2.web.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.demo.domain.data.PlatformSession;

@Service("serviceBoImpl")
public class ServiceBoImpl implements ServiceBo {
	@Qualifier("jmsProducerTemplate")
	@Autowired
    private JmsTemplate template = null;
	
	public void process(final String msg) {
		System.out.println("<---- start to process ------");
		template.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
            	
            	PlatformSession platformSession = new PlatformSession(msg);
            	ObjectMessage objMsg = session.createObjectMessage();
            	objMsg.setObject(platformSession);
                return objMsg;
            }
        });
		System.out.println("<---- end to process ------");
	}
}
