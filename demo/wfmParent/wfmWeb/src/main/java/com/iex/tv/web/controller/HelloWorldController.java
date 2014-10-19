package com.iex.tv.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.iex.tv.domain.Agent;
import com.iex.tv.domain.Employee;
import com.iex.tv.services.AgentService;
import com.iex.tv.services.EmployeeService;

@Controller
public class HelloWorldController {
    
	@Resource(name="demoHibernateProperties")
	Properties testBean;
	
	@Resource(name="agentServiceImpl")
	AgentService agentService;
	
	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;
	
    @RequestMapping(value="/welcome")
    public ModelAndView helloWorld(HttpServletRequest request) {      
        ModelAndView modelAndView = new ModelAndView("HelloWorldPage");
        modelAndView.addObject("msg", "hello world");
        
		try {
			System.out.println(testBean);

			Agent obj = agentService.getAgent(155);
			Agent obj1 = agentService.getAgent(155);
			List<Agent> result = agentService.findAgentByNamedQuery("a", "a");
			List<Agent> result1 = agentService.findAgentByNamedQuery("a", "a");
			
			System.out.println(testBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return modelAndView;
    }   

    @RequestMapping(value="/logout")
    public ModelAndView logout(HttpServletRequest request) {      
    	ModelAndView modelAndView = new ModelAndView("HelloWorldPage");
    	modelAndView.addObject("msg", "hello world");
    	
    	HttpSession session = request.getSession();
    	session.invalidate();
    	
    	return modelAndView;
    }   
}
