package com.e2.web.auth;

import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component("cookieUtil")
public class CookieUtil {
	private final static Logger logger = Logger.getLogger(CookieUtil.class);
	
	public final static String SECURITY_TOKEN_COOKIE_NAME = "security_token";
	public final static String SECURITY_ENDPOINT_COOKIE_NAME = "security_endpoint";
	
	public AuthSession getAuthSession(HttpServletRequest request) {
		String token = getSecurityToken(request);
		if(token == null) {
			if(logger.isDebugEnabled()) {
				logger.debug("getAuthSession() return null.");
			}			
			return null;
		}
		
		String endPoint = getSecurityToken(request);
		AuthSession authSession = new AuthSession(token, endPoint);
		if(logger.isDebugEnabled()) {
			logger.debug("getAuthSession() = " + authSession.toString());
		}
		return authSession;
	}
	
	public String getSecurityToken(HttpServletRequest request) {
		String token = getCookieValue(request, SECURITY_TOKEN_COOKIE_NAME);
    	if(logger.isDebugEnabled()) {
    		logger.debug("Security Token : " + token);
    	}
		return token;
	}
	
	public String getSecurityEndPoint(HttpServletRequest request) {
		String value = getCookieValue(request, SECURITY_ENDPOINT_COOKIE_NAME);
		if(logger.isDebugEnabled()) {
			logger.debug("Security EndPoint : " + value);
		}
		return value;
	}
	
	public String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie cookie = getCookie(request, cookieName);
		String value = null;
		if(cookie != null) {
			value = cookie.getValue();				
		}
		return value;
	}
	
	public Cookie getCookie(HttpServletRequest request, String cookieName) {
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	      for (Cookie cookie : cookies) {
	        if (cookieName.equals(cookie.getName())) {
	        	if(logger.isDebugEnabled()) {
	        		logger.debug("found cookie " + cookieName + ": " + cookie.toString());
	        	}
	        	return cookie;
	        }
	      }
	    }
    	if(logger.isDebugEnabled()) {
    		logger.debug("No cookie found for name = " + cookieName);
    	}	    
	    return null;
	}
	
	public void createSecurityCookies(HttpServletResponse response, String token, String endPoint, String domain) {
		createCookie(response, SECURITY_TOKEN_COOKIE_NAME, token, domain);
		createCookie(response, SECURITY_ENDPOINT_COOKIE_NAME, endPoint, domain);
	}
	
	public void createCookie(HttpServletResponse response, String cookieName, String value, String domain) {
		createCookie(response, cookieName, value, domain, -1, "/", true, false);
	}
	
	public void removeSecurityCookies(HttpServletResponse response) {
		deleteCookie(response, SECURITY_TOKEN_COOKIE_NAME);
		deleteCookie(response, SECURITY_ENDPOINT_COOKIE_NAME);
	}
	
	public void deleteCookie(HttpServletResponse response, String cookieName) {
		createCookie(response, cookieName, null, null, 0, null, true, false);
	}
	
	public void createCookie(HttpServletResponse response, String cookieName,
			String value, String domain, int expiry, String path,
			boolean isHttpOnly, boolean secure) {
	      Cookie cookie = new Cookie(cookieName, value);
	      if(domain != null) {
	    	  cookie.setDomain(domain);
	      }
	      
	      cookie.setMaxAge(expiry); // zero - delete, -1 - delete when exist, 
	      
	      if(path != null) {
	    	  cookie.setPath(path);
	      }
	      
	      cookie.setHttpOnly(isHttpOnly);
	      cookie.setSecure(secure);
	      
	      response.addCookie(cookie);		
	}
}
