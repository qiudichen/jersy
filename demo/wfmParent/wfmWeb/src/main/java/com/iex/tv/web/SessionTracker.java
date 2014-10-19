package com.iex.tv.web;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class SessionTracker
 *
 */
public class SessionTracker implements HttpSessionListener {
	private final static String LOGIN_TOKEN = "login-token";

    /**
     * Default constructor. 
     */
    public SessionTracker() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
		String token = (String)session.getAttribute(LOGIN_TOKEN);
		System.out.println("<--------------------session created -----" + token);
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
		String token = (String)session.getAttribute(LOGIN_TOKEN);
		System.out.println("<--------------------session destoryed -----" + token);
    }
}
