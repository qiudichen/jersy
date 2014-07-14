package com.iex.tv.caching.web.controller;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iex.tv.caching.ws.CachingWSService;

@Controller
@RequestMapping("/caching")
public class CachingChontroller {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Resource(name="cachingWSServiceImpl")
	private CachingWSService cachingWSService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String listCaches(Model model) {
		Collection<String> names = cachingWSService.getCachingNames();
		model.addAttribute("cahces", names);	
		return "caches";
	}
	
	@RequestMapping(value="/remove", method = RequestMethod.POST)
	public String remove(@RequestParam (required=true) String cacheName, 
			@RequestParam (required=true) String key) {
		return "forward:/caching.html";       
	}
	
	@RequestMapping(value="/clear", method = RequestMethod.POST)
	public String removeAll(@RequestParam (required=true) String cacheName) {
		return "forward:/caching.html";       
	}
	
	@RequestMapping(value="/check")
	public String check(@RequestParam (required=true) String cacheName, 
			@RequestParam (required=true) String key) {
		return "check";       
	}	
}
