package com.iex.tv.trianing.web.controller;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.iex.tv.services.AgentService;

@Controller
public class HelloWorldController {
	
	@Resource(name="demoHibernateProperties")
	org.springframework.beans.factory.config.PropertiesFactoryBean testBean;
	
	@Resource(name="agentServiceImpl")
	AgentService agentService;
	
	@RequestMapping(value="/welcome")
	public ModelAndView helloWorld(HttpServletRequest request) {		
		ModelAndView modelAndView = new ModelAndView("HelloWorldPage");
		modelAndView.addObject("msg", "hello world");
		try {
			Properties obj = testBean.getObject();
			System.out.println(obj);
			
			agentService.findAgentByNamedQuery("", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return modelAndView;
	}	
}
