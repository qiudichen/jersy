package com.e2.web.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * define filter element to web.xml	
 * <filter>
 * 		<filter-name>securityTokenFilter</filter-name>
 *  	<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
 *  </filter>
 *  
 *  Scan package com.e2.web.auth in spring configuration
 *  
 * @author dchen
 *
 */
@Component("securityTokenFilter")
public class SecurityTokenFilter implements Filter {
	private final static Logger logger = Logger.getLogger(SecurityTokenFilter.class);
	
	private final static String LOGIN_URL_PARAM = "LoginURL";
	
	private final static String REDIRECT = "isRedirect";

	private final static String EXCLUDE_URI = "excludeURI";
	
	private final static String DOMAIN_NAME = "domainName";
	
	private final static String TOKEN_MAX_EXPIRED_DURATION = "tokenMaxExpiredSeconds";
	
	@Autowired
	@Qualifier("cookieUtil")
	private CookieUtil cookieUtil;
	
	@Autowired
	@Qualifier("authService")
	private AuthService authService;
	
	private FilterConfig filterConfig;
	
	private String loginURL;
	
	private boolean redirectFlag;
	
	private String excludeURI;
	
	private String domainName;
	
	private long tokenMaxExpiredSeconds;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		loginURL = this.filterConfig.getInitParameter(LOGIN_URL_PARAM);
		redirectFlag = "true".equalsIgnoreCase(this.filterConfig.getInitParameter(REDIRECT));
		excludeURI = this.filterConfig.getInitParameter(EXCLUDE_URI);
		domainName  = this.filterConfig.getInitParameter(DOMAIN_NAME);
		
		String temp  = this.filterConfig.getInitParameter(TOKEN_MAX_EXPIRED_DURATION);
		try {
			tokenMaxExpiredSeconds = Long.parseLong(temp) * 1000;
		} catch(Exception e) {
			tokenMaxExpiredSeconds = 900 * 1000; //15 minutes;
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("loginURL = " + loginURL + ", isRedirect = " + redirectFlag);
			logger.debug("excludeURI = " + excludeURI + ", domainName = " + domainName);
			logger.debug("tokenMaxExpiredSeconds = " + tokenMaxExpiredSeconds);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		String uri = req.getRequestURI();
		if(uri.endsWith(excludeURI) || isLogin(req, res)) {
			chain.doFilter(request, response);
		} else {
			loginAction(req, res);
		}
	}

	@Override
	public void destroy() {
		
	}
	
	private String getDomain(ServletRequest request) {
		if(domainName == null) {
			domainName = request.getServerName().replaceAll(".*\\.(?=.*\\.)", "");	
		}
		return domainName;
	}
	
	private boolean isLogin(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		
		AuthSession authSession = getAuthSessionFromSession(session);
		if(authSession != null) {
			if(!authSession.isExpired(tokenMaxExpiredSeconds) || vaildTokenSession(authSession)) {
				authSession.touch();
				return true;
			} 
		}
		
		authSession = cookieUtil.getAuthSession(request);
		if(authSession != null && vaildTokenSession(authSession)) {
			session.setAttribute(authSession.SESSION_KEY, authSession);
			return true;
		} else {
			session.invalidate();
			return false;
		}
	}

	private boolean vaildTokenSession(AuthSession authSession) {
		if(this.authService != null) {
			return this.authService.valid(authSession);
		} 
		logger.warn("authService is null. Always return ture.");
		return true;
	}
	
	private AuthSession getAuthSessionFromSession(HttpSession session) {
		if(session.isNew()) {
			return null;
		}
		return (AuthSession)session.getAttribute(AuthSession.SESSION_KEY);
	}
	
	private void loginAction(HttpServletRequest request, HttpServletResponse response) {
		if(this.redirectFlag) {
			try {
				response.sendRedirect(loginURL);
			} catch (IOException e) {
				logger.error("Unable to redirect to" + loginURL, e);
			}
		} else {
			try {
				request.getRequestDispatcher(loginURL).forward(request, response);
			} catch (ServletException | IOException e) {
				logger.error("Unable to forward to" + loginURL, e);
			}
		}
	}
}
