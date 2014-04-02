package com.e2.web.jms;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.e2.web.service.ServiceBo;

@Component("jmsMessageListener")
public class JmsMessageListener implements MessageListener {

	@Autowired
    private AtomicInteger counter = null;

	public void onMessage(Message message) {
        try {   
            int messageCount = message.getIntProperty(ServiceBo.MESSAGE_COUNT);
            
            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage)message;
                String msg = tm.getText();
                System.out.println("Processed message '{" + msg + "}'.  value={" + messageCount + "}");
                counter.incrementAndGet();
            }
        } catch (JMSException e) {
            e.printStackTrace();;
        }

	}

}
