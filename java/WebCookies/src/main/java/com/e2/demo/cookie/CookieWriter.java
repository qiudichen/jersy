package com.e2.demo.cookie;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CookieWriter
 */
@WebServlet("/create")
public class CookieWriter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CookieWriter() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String sessionid = generateSessionId();
	      Cookie muleCookie = new Cookie(CookieFilter.MULE_SESSION, sessionid);
	      //muleCookie.setMaxAge(CookieFilter.MAX_EXPIRED_DURATION_INSECOND);
	      muleCookie.setPath("/test/");
	      response.addCookie(muleCookie);
	      
	      response.setContentType("text/html");
	      PrintWriter out = response.getWriter();
	      out.println("<HTML><HEAD><TITLE>Current Shopping Cart Items</TITLE></HEAD>");
	      out.println("<BODY>");
	      out.print("mule Session Id: ");
	      out.println(sessionid);
	      out.println("</BODY></HTML>");
	}

	private static String generateSessionId() {
		String uid = new java.rmi.server.UID().toString(); // guaranteed unique
		return java.net.URLEncoder.encode(uid); // encode any special chars
	}

}
