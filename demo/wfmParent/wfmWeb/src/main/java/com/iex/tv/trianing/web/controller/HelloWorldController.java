package com.iex.tv.trianing.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {
	
	@RequestMapping(value="/welcome")
	public ModelAndView helloWorld(HttpServletRequest request) {		
		ModelAndView modelAndView = new ModelAndView("HelloWorldPage");
		modelAndView.addObject("msg", "hello world");
		return modelAndView;
	}	
}
