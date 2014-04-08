package com.iex.tv.web.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.iex.tv.demo.services.AgentService;
import com.iex.tv.domain.training.Agent;
import com.iex.tv.domain.training.Phone;

@Controller
public class AgentController {
	@Autowired
	@Qualifier("agentServiceImpl")	
	private AgentService agentService;
	
	@RequestMapping(value="/agent")
	public ModelAndView viewSkill(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView("agent");	
		Agent agent1 = this.agentService.getAgent(35);
		List<Agent> agents = agentService.getAgents();
		for(Agent agent : agents) {
			if(agent.getPhones() != null) {
				for(Phone phone : agent.getPhones()) {
					System.out.print(phone);
				}
			}
		}
		modelAndView.addObject("agents", agents);
		return modelAndView;
	}
	
	@RequestMapping(value="/addAgent", method=RequestMethod.GET)
	public ModelAndView addSkill(HttpServletRequest request, 
			@RequestParam (required=true) long skillId) {

		this.agentService.addAgent(skillId);
		ModelAndView modelAndView = new ModelAndView("forward:/agent.html");
		return modelAndView;         
	}
}
