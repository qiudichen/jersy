package org.apache.tomact;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class ActiveMQInit
 */
public class ActiveMQInit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ActiveMQInit.class);

	private static final String ACTIVEMQ_URL = "activeMQURL";

	private BrokerService broker;

	/**
	 * Default constructor.
	 */
	public ActiveMQInit() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		String url = config.getInitParameter(ACTIVEMQ_URL);
		if (url == null || url.isEmpty()) {
			url = "tcp://localhost:61616";
		}

		logger.info("Load activeMQ");
		try {
			broker = new BrokerService();
			broker.addConnector("tcp://localhost:61616");
			broker.start();
			logger.info("ActiveMQ loaded succesfully");
		} catch (Exception e) {
			logger.error("Unable to load ActiveMQ!", e);
		}
	}

	public void destroy() {
		if (broker != null) {
			try {
				broker.stop();
			} catch (Exception e) {
				logger.error("Unable to load ActiveMQ!", e);
			}
		}
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Context initCtx = new InitialContext();
			Context envContext = (Context) initCtx.lookup("java:comp/env");
			
			ConnectionFactory connectionFactory = (ConnectionFactory) envContext.lookup("jms/ConnectionFactory");
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic("jms/topic/defaultTopic");
			MessageProducer producer = session.createProducer(destination);
			TextMessage msg = session.createTextMessage();
			msg.setText("Message sent");
			producer.send(msg);
		} catch (Exception e) {
			logger.error("Unable to load ActiveMQ!", e);
		}
	}

}
