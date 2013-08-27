package com.e2.web.jms;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

@Component("activeMQSender")
public class ActiveMQSender implements HttpRequestHandler  {
	private static final long serialVersionUID = 1L;

	@Autowired
	private org.springframework.jms.core.JmsTemplate template;
    /**
     * Default constructor. 
     */
    public ActiveMQSender() {
        // TODO Auto-generated constructor stub
    }

	public void handleRequest(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		template.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage("ddddddddddddddddddddd");
				return message;
			}
		});
	}

}
