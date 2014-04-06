package com.iex.tv.web.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.iex.tv.domain.system.SubSystemType;
import com.iex.tv.domain.system.SystemProperty;
import com.iex.tv.services.ServiceException;
import com.iex.tv.services.SystemService;
import com.iex.tv.services.SystemValue;

@Controller
public class SystemController {
	
	@Autowired
	@Qualifier("systemServiceImpl")	
	private SystemService service;
	
	@RequestMapping(value="/config")
	public ModelAndView viewProperties(HttpServletRequest request) {
		
		ModelAndView modelAndView = new ModelAndView("properties.jsp");
		
		List<SystemProperty> sysList;
		try {
			sysList = service.getSystemPropertiesByRank();
			sysList = service.getSystemPropertiesBySubType(SubSystemType.WEBSTATION);
			
			List<SystemValue> values = service.getSystemValueBySubType(SubSystemType.WEBSTATION);
			
			if(sysList != null) {
				modelAndView.addObject("sysList", sysList);		
			} else {
				modelAndView.addObject("sysList", Collections.emptyList());
			}
			
			modelAndView.addObject("errorMsg", "success");	
		} catch (ServiceException e) {
			modelAndView.addObject("errorMsg", "Unable to get system properties");	
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/addProperty", method=RequestMethod.POST)
	public String addProperties(HttpServletRequest request, @RequestParam (required=true) String name, @RequestParam (required=true) String value) {
		try {
			SystemProperty newProperty = service.addSystemProperty(name, value, SubSystemType.WEBSTATION);
			
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		return "forward:/config.html";         
	}
	
	@RequestMapping(value="/deleteProperty")
	public String deleteProperty(HttpServletRequest request, @RequestParam (value="oid", required=true) String propOid) {
		
		try {
			service.deleteSystemProperty(propOid);
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "forward:/config.html";         
	}	
}
