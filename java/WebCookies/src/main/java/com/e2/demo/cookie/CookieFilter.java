package com.e2.demo.cookie;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class CookieFilter
 */
@WebFilter("/")
public class CookieFilter implements Filter {
	public final static String MULE_SESSION = "muleSessionid";
	public final static int MAX_EXPIRED_DURATION_INSECOND = 600;  //10 minutes
	public final static long MAX_EXPIRED_DURATION = 600000;  //10 minutes
	
	private FilterConfig fConfig;
    /**
     * Default constructor. 
     */
    public CookieFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		
		String uri = req.getRequestURI();
		if(uri.endsWith("/login") || isLogin(req, (HttpServletResponse)response)) {
			chain.doFilter(request, response);
		} else {
			((HttpServletResponse)response).sendRedirect("http://www.google.com");
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		this.fConfig = fConfig;
	}

	private boolean isLogin(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		
		MuleSession muleSession = getMuleSessionIdFromSession(session);
		if(muleSession != null) {
			if(!muleSession.isExpired(MAX_EXPIRED_DURATION) || vaildMuleSession(muleSession.sessionId)) {
				muleSession.touch();
				return true;
			} 
		}
		
		String sessionid = getMuleSessionId(request);
		if(sessionid != null && vaildMuleSession(sessionid)) {
			session.setAttribute(MULE_SESSION, new MuleSession(sessionid));
			return true;
		} else {
			session.invalidate();
			return false;
		}
	}
	
	private boolean vaildMuleSession(String sessionid) {
		//TODO - for demo purpose, always return true;
		return true;
	}
	
	private MuleSession getMuleSessionIdFromSession(HttpSession session) {
		if(session.isNew()) {
			return null;
		}
		return (MuleSession)session.getAttribute(MULE_SESSION);
	}
	
	private String getMuleSessionId(HttpServletRequest request) {
		String sessionid = null;
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	      for (Cookie cookie : cookies) {
	        if (MULE_SESSION.equals(cookie.getName())) {
	          sessionid = cookie.getValue();
	          int maxAge = cookie.getMaxAge();
	          String path = cookie.getPath();
	          String domain = cookie.getDomain();
	          boolean isSecure = cookie.getSecure();
	          break;
	        }
	      }
	    }
	    return sessionid;
	}
	
	private class MuleSession {
		String sessionId;
		long lastAccessTime;
		
		MuleSession(String sessionId) {
			this.sessionId = sessionId;
			this.lastAccessTime = System.currentTimeMillis();
		}
		
		void touch() {
			this.lastAccessTime = System.currentTimeMillis();
		}
		
		boolean isExpired(long maxExpiredDuration) {
			long currentTime = System.currentTimeMillis();
			return this.lastAccessTime + maxExpiredDuration < currentTime;
		}
	}
}
