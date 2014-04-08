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

import com.iex.tv.demo.services.DBRollback;
import com.iex.tv.demo.services.SkillService;
import com.iex.tv.domain.training.Skill;

@Controller
public class SkillController {
	@Autowired
	@Qualifier("skillServiceImpl")	
	private SkillService skillService;
	
	@RequestMapping(value="/skill")
	public ModelAndView viewSkill(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView("skill");
		List<Skill> skills = this.skillService.getSkills();
		modelAndView.addObject("skills", skills);			
		return modelAndView;
	}
	
	@RequestMapping(value="/addSkill", method=RequestMethod.POST)
	public ModelAndView addSkill(HttpServletRequest request, 
			@RequestParam (required=true) String skillName) {

		try {
			this.skillService.addSkill(skillName);
		} catch (DBRollback e) {
			e.printStackTrace();
		}
		ModelAndView modelAndView = new ModelAndView("forward:/skill.html");
		return modelAndView;         
	}
	
	@RequestMapping(value="/deleteSkill")
	public ModelAndView deleteSkill(HttpServletRequest request, @RequestParam (value="skillId", required=true) long skillId) {
		try {
			//Skill skill = this.skillService.getSkill(skillId);
			//this.skillService.deleteSkill(skill);
			this.skillService.deleteSkill(skillId);
		} catch (DBRollback e) {
			e.printStackTrace();
		}
		ModelAndView modelAndView = new ModelAndView("forward:/skill.html");
		return modelAndView;              
	}	
	
	@RequestMapping(value="/updateSkill", method=RequestMethod.POST)
	public ModelAndView updateSkill(HttpServletRequest request, 
			@RequestParam (required=true) String skillName, 
			@RequestParam (required=true) long skillId) {
		
		try {
			Skill skill = this.skillService.updateSkill(skillId, skillName);
			
			skill = this.skillService.getSkill(skillId);
			if(skill != null) {
				this.skillService.updateSkill(skill, skillName);
			}
		} catch (DBRollback e) {
			e.printStackTrace();
		}
		
		ModelAndView modelAndView = new ModelAndView("forward:/skill.html");
		return modelAndView;         
	}		
}
