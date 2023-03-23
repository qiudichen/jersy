package com.nice.wfm.env.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProcessServlet
 */
public class ProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UNIX_SHELL = "/bin/sh";
    /**
     * Default constructor. 
     */
    public ProcessServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter writer = response.getWriter(); //.append("Served at: ").append(request.getContextPath());
		
		writer.append("Served at: ").append(request.getContextPath());
		
		String path = request.getParameter("path");
		if(path == null) {
			path = "/totalview/inst/tv4";
		}
		String script = request.getParameter("script");
		if(script == null) {
			script = "test.sh";
		}
		
		try {
			ProcessBuilder procBuilder = new ProcessBuilder(UNIX_SHELL, script);
	    	procBuilder.directory(new File(path));
	        procBuilder.redirectErrorStream(true);
	        
	        writer.append("\nprocbuilder.directory = " + procBuilder.directory());
	        writer.append("\nprocbuilder.command = " + procBuilder.command());
	        
	        TreeMap<String, String> sortedEnv = new TreeMap<String, String>(procBuilder.environment());
	        String value = buildDelimitedString(sortedEnv, "\n", null).toString();
	        
	        writer.append("\n==== procbuilder env ====\n");
	        writer.append(value);
	        
	        Process process = procBuilder.start();
			int status = process.waitFor();
			
			BufferedReader stdInput = new BufferedReader(new 
				     InputStreamReader(process.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
			     InputStreamReader(process.getErrorStream()));
				
			writer.append("\n=====status=====" + status);
			String s = null;
			writer.append("\n=====standard out=====");
			while ((s = stdInput.readLine()) != null) {
				writer.append("\n");
				writer.append(s);
			}
			writer.append("\n=====error out=====");
			while ((s = stdError.readLine()) != null) {
				writer.append("\n");
				writer.append(s);
			}

		} catch(Exception e) {
			e.printStackTrace(writer);
		}
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
    public static final StringBuilder buildDelimitedString(Map<String, ?> map, String delimiter, StringBuilder buf)
    {
        if (buf == null)
        {
            buf = new StringBuilder();
        }

        if (map != null && !map.isEmpty() && (delimiter != null))
        {
            for (Map.Entry<String, ?> entry : map.entrySet())
            {
                buf.append(entry.getKey());

                Object value = entry.getValue();
                if (value != null)
                {
                    buf.append('=');

                    // Make sure we get "readable" array entries
                    if (value.getClass().isArray())
                    {
                        buildDelimitedString(value, ",", buf);
                    }
                    else
                    {
                        buf.append(value);
                    }
                }

                buf.append(delimiter);
            }

            // Remove trailing delimiter
            if (buf.length() > 0)
            {
                buf.setLength(buf.length() - delimiter.length());
            }
        }

        return buf;
    }
    
    public static final StringBuilder buildDelimitedString(Object array, String delimiter, StringBuilder buf)
    {
        if (buf == null)
        {
            buf = new StringBuilder();
        }

        if (array != null && delimiter != null && array.getClass().isArray() && Array.getLength(array) > 0)
        {
            for (int n = 0; n < Array.getLength(array); ++n)
            {
                Object obj = Array.get(array, n);

                // Make sure we get "readable" array entries
                if (obj != null && obj.getClass().isArray())
                {
                    buf.append('[');
                    buildDelimitedString(obj, ",", buf);
                    buf.append(']');
                }
                else
                {
                    buf.append(obj);
                }
                buf.append(delimiter);
            }

            // Remove trailing delimiter
            if (buf.length() > 0)
            {
                buf.setLength(buf.length() - delimiter.length());
            }
        }

        return buf;
    }    
}
