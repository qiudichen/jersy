package com.demo.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.persistent.service.AgentcalloutService;

@Controller
public class WelcomeController {
	private static int id = 0;
	private final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
	
	@Autowired
	AgentcalloutService service;
	
	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";

	public WelcomeController() {
		System.out.println(message);
	}
	
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		id++;
		service.addAgentcallout("c" + id, "s" + id);
		logger.debug("This is debug.");
		logger.info("This is info.");
		logger.warn("This is warn.");
		logger.error("This is error.");
		model.put("message", this.message);
		return "welcome";
	}
}
