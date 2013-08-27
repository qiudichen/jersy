package com.e2.web.jms;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

@Component("simpleListener")
public class SimpleMessageListener implements MessageListener {

	public void onMessage(Message msg) {
		System.out.println(msg);
	}

}
