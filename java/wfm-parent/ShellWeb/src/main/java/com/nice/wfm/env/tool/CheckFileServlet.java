package com.nice.wfm.env.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CheckFileServlet
 */
public class CheckFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckFileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter writer = response.getWriter(); 

		writer.append("Served at: ").append(request.getContextPath());

		MBeanService beanService = MBeanService.getInstance();
		beanService.listObjects(writer);
		
//		checkFile("/totalview/inst/tv4/config/tvconfig" + File.separatorChar + "tv.test.properties", writer);
//		 checkFile("/totalview/inst/tv4/config/tvconfig/tv.test.properties", writer);
//		 checkFile("c:/totalview/inst/tv4/config/tvconfig/tv.test.properties", writer);
//		 checkFile("t:/totalview/inst/tv4/config/tvconfig/tv.test.properties", writer);
//		 checkFile("/c/totalview/inst/tv4/config/tvconfig/tv.test.properties", writer);
		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	
	private void checkFile(String filePath, PrintWriter writer) {
		writer.append("\n ---- start --- check file ---" + filePath);
		File file = new File(filePath);
		if(!file.exists()) {
			writer.append("\n File doesn't exists. filePath:" + filePath);
			return;
		}
		writer.append("\n File exists. filePath:" + filePath);
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace(writer);
			return;
		}
		for (Object key: prop.keySet()) {
			writer.append("\n" + key + ": " + prop.getProperty(key.toString()));
        }
		writer.append("\n ---- end --- check file ---" + filePath);
		return ;
	}
}
