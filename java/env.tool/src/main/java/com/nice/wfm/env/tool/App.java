package com.nice.wfm.env.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.TreeMap;
/**
 * Hello world!
 *
 */
public class App 
{
    private static final String UNIX_SHELL = "/bin/sh";
    private static PrintWriter pw = new PrintWriter(System.out);
    
    public static void main(String[] args) {
    	pw.println("--------- start ---------------");
    	pw.println(args[0]);
    	pw.println("--------- start ---------------");
    	pw.flush();
    	String base = "/totalview/inst/tv4/wfm_nodes/swxiface/conf";
    	
    	if(args.length == 1) {
    		base = args[0];
    	}
    	
    	pw.println("base is " + base);
    	pw.flush();
    	
    	String fileName = base + File.separatorChar + "catalina.properties";
    	
    	pw.println("--------- fileName ---------------" + fileName);
    	pw.flush();
    	
    	File file = new File(fileName);
    	if(file.exists()) {
        	pw.println("--------- file exist ---------------" + fileName);
        	pw.flush();
    	} else {
        	pw.println("--------- file not found ---------------" + fileName);
        	pw.flush();
    	}
    	
    	fileName = base + "/catalina.properties";
    	file = new File(fileName);
    	if(file.exists()) {
        	pw.println("--------- file exist ---------------" + fileName);
        	pw.flush();
    	} else {
        	pw.println("--------- file not found ---------------" + fileName);
        	pw.flush();
    	}
    }
    
//    public static void main(String[] args)
//    {		
//    	if(args.length != 3) {
//    		System.out.println("Usage: java -jar file shellName pathName fileName");
//    		return;
//    	} else {
//    		String shell = args[0];
//    		
//    		String pathName = args[1];
//    		String fileName = args[2];
//    		
//	    	pw.println("--------- start ---------------");
//	    	pw.flush();
//
//	    	ProcessBuilder procBuilder = new ProcessBuilder(shell, fileName);
//	    	procBuilder.directory(new File(pathName));
//	        procBuilder.redirectErrorStream(true);
//	        
//	        
//	        pw.println("procbuilder.directory = " + procBuilder.directory());
//	        pw.flush();
//	        pw.println("procbuilder.command = " + procBuilder.command());
//	        pw.flush();
//	        
//	        TreeMap<String, String> sortedEnv = new TreeMap<String, String>(procBuilder.environment());
//	        String value = buildDelimitedString(sortedEnv, "\n", null).toString();
//	        pw.println("<=================== procbuilder env ====\n");
//	        pw.flush();
//	        pw.println(value);
//	        pw.flush();
//	        pw.println("==========================================\n");
//	        pw.flush();
//	        
//	        Process process;
//			try {
//				process = procBuilder.start();
//				int status = process.waitFor();
//				
//				BufferedReader stdInput = new BufferedReader(new 
//					     InputStreamReader(process.getInputStream()));
//	
//				BufferedReader stdError = new BufferedReader(new 
//				     InputStreamReader(process.getErrorStream()));
//					
//				pw.println("=====status=====" + status);
//				String s = null;
//				pw.println("=====standard out=====");
//				while ((s = stdInput.readLine()) != null) {
//					 pw.println(s);
//					 pw.flush();
//				}
//				pw.println("=====error out=====");
//				while ((s = stdError.readLine()) != null) {
//					 pw.println(s);
//					 pw.flush();
//				}
//				
//				pw.flush();
//			} catch (Exception e) {
//				pw.println("============== error ===========");
//				e.printStackTrace(pw);
//			}
//	        
//	        pw.println("--------- end ---------------");
//	        pw.flush();
//    	}
//    }
    
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
