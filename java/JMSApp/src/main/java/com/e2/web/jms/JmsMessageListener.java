package com.e2.web.jms;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

@Component("jmsMessageListener")
public class JmsMessageListener implements MessageListener {

	private static AtomicInteger count = new AtomicInteger(0);
	
	private static AtomicLong delay = new AtomicLong();
	private static long maxDelay = 0;
	
	public void onMessage(Message message) {
        try {   
        	long receive = System.currentTimeMillis();
        	long totalDelay = 0;
        	long myDelay = 0;
            if(message instanceof ObjectMessage) {
            	ObjectMessage objMsg = (ObjectMessage)message;
            	Object obj = objMsg.getObject();
            	if(obj instanceof SimpleObject) {
            		SimpleObject simpleObject = (SimpleObject)obj;
            		myDelay = receive - simpleObject.getSendTime();
            		if(maxDelay < myDelay) {
            			maxDelay = myDelay;
            		}
            		totalDelay = delay.addAndGet(myDelay);
            	}
            }
            
            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage)message;
                String msg = tm.getText();
            }
            
            int num = count.incrementAndGet();
            double everyDelay = totalDelay / num;
            
            System.out.println("Message totalDelay" + totalDelay + "/ totalCount " + num + ", this message Delay" + myDelay + ", average: " + everyDelay + ", maxDelay " + maxDelay);
        } catch (JMSException e) {
            e.printStackTrace();;
        }

	}

}
