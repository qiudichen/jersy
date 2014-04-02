package com.e2.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.e2.domain.cust.User;
import com.e2.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/user")
	public ModelAndView getUser(HttpServletRequest request) {
		String name = request.getParameter("name");
		ModelAndView modelAndView = new ModelAndView("user.jsp");
		modelAndView.addObject("userName", name);	
		List<User> users = userService.getUsers();
		if(name != null) {
			String result = userService.addUser(name);
			modelAndView.addObject("result", result);		
		}
		
		return modelAndView;
	}
}
