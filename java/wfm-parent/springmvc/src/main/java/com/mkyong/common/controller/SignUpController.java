package com.mkyong.common.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mkyong.common.model.Customer;
import com.tomcat.serviceImpl.UserService;

@Controller
@RequestMapping("/customer")
public class SignUpController {
    private static final String UNIX_SHELL = "/bin/sh";
    
    
	@Autowired
	private UserService userService;
	
	public SignUpController() {
		System.out.println("==========11===============");
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String addCustomer(
			Customer customer,
			BindingResult result) {

		/*for (Object object : result.getAllErrors()) {
			if (object instanceof FieldError) {
				FieldError fieldError = (FieldError) object;

				System.out.println(fieldError.getField() + ":"
						+ fieldError.getCode());

			}

			if (object instanceof ObjectError) {
				ObjectError objectError = (ObjectError) object;

			}
		}*/

		if (result.hasErrors()) {
			return "SignUpForm";
		} else {
			return "Done";
		}

	}

	@RequestMapping(method = RequestMethod.GET)
	public String displayCustomerForm(ModelMap model) {
		String msg = "change - 3";
		System.out.println(msg);
		
		userService.foo();
		model.addAttribute("customer", new Customer());
		shellRun();
		return "SignUpForm";

	}
	
	public void shellRun() {
		PrintWriter pw = new PrintWriter(System.out);
    	pw.println("--------- start ---------------");
    	pw.flush();
    	File scriptFile = new File("/totalview/inst/tv4/test.sh");
    	File path = new File("/totalview/inst/tv4");
    	
    	ProcessBuilder procBuilder = new ProcessBuilder(UNIX_SHELL, scriptFile.getPath());
    	procBuilder.directory(path);
        procBuilder.redirectErrorStream(true);
        
        pw.println("procbuilder.directory = " + procBuilder.directory());
        pw.flush();
        pw.println("procbuilder.command = " + procBuilder.command());
        pw.flush();
        
        TreeMap<String, String> sortedEnv = new TreeMap<String, String>(procBuilder.environment());
        String value = buildDelimitedString(sortedEnv, "\n", null).toString();
        
        pw.println("==== procbuilder env ====\n");
        pw.flush();

        pw.println(value);
        pw.flush();
        
        Process process;
		try {
			process = procBuilder.start();
			int status = process.waitFor();
			
			BufferedReader stdInput = new BufferedReader(new 
				     InputStreamReader(process.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
			     InputStreamReader(process.getErrorStream()));
				
			pw.println("=====status=====" + status);
			String s = null;
			pw.println("=====standard out=====");
			while ((s = stdInput.readLine()) != null) {
				 pw.println(s);
				 pw.flush();
			}
			pw.println("=====error out=====");
			while ((s = stdError.readLine()) != null) {
				 pw.println(s);
				 pw.flush();
			}
			
			pw.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        pw.println("--------- end ---------------");
        pw.flush();
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