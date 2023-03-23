package com.tomcat.agent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PropertyLoader
 */
public class PropertyLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PropertyLoader() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = request.getParameter("path");
		if(path == null) {
			path = "tvconfig/tv.default.properties";
		}
		
		String p = load(path);
		response.getWriter().append("Served at: ").append(request.getContextPath()).append("\n").append(p);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public String load(String path) {
		
		Properties prop = new Properties();
		StringBuilder builder = new StringBuilder();
		try {
			InputStream in = PropertyLoader.class.getClassLoader().getResourceAsStream(path);
			if(in == null) {
				builder.append("No tvconfig/tv.default.properties file found in classpath.");
			} else {
				prop.load(in);
		       for (Object key: prop.keySet()) {
		    	   builder.append(key + ": " + prop.getProperty(key.toString())).append("\n");
		        }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.toString();
	}
}
