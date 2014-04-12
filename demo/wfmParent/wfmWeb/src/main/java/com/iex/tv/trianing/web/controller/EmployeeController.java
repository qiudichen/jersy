package com.iex.tv.trianing.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.iex.tv.domain.Employee;
import com.iex.tv.services.EmployeeService;

@Controller
public class EmployeeController {
	
	Logger logger = Logger.getLogger(EmployeeController.class);
	
	@Autowired
	@Qualifier("employeeServiceImpl")		
	private EmployeeService employeeService;

	@RequestMapping(value="/employee")
	public ModelAndView viewEmployee(HttpServletRequest request) {
		logger.debug("<------------ viewEmployee --------------------");
		
		ModelAndView modelAndView = new ModelAndView("employee");
		String now = (new Date()).toString();
		modelAndView.addObject("now", now);
		List<Employee> employees = this.employeeService.getEmployees();
		modelAndView.addObject("employees", employees);			
		return modelAndView;
	}
	
	@RequestMapping(value="/addEmployee", method=RequestMethod.POST)
	public ModelAndView addEmployee(HttpServletRequest request, 
			@RequestParam (required=true) String firstName, 
			@RequestParam (required=true) String lastName, 
			@RequestParam (required=true) String gender) {

		this.employeeService.addEmployee(lastName, firstName, "M".equals(gender) ? Employee.Gender.M : Employee.Gender.F);		
		ModelAndView modelAndView = new ModelAndView("forward:/employee.html");
		return modelAndView;         
	}
	
	@RequestMapping(value="/deleteEmployee")
	public ModelAndView deleteEmployee(HttpServletRequest request, @RequestParam (value="empNum", required=true) long empNum) {
		this.employeeService.deleteEmployee(empNum);
		ModelAndView modelAndView = new ModelAndView("forward:/employee.html");
		return modelAndView;              
	}	
	
	@RequestMapping(value="/updateEmployee", method=RequestMethod.POST)
	public ModelAndView updateProperties(HttpServletRequest request, 
			@RequestParam (required=true) String firstName, 
			@RequestParam (required=true) String lastName, 
			@RequestParam (required=true) long empNum) {
		
		//this.employeeService.updateEmployee(empNum, lastName, firstName);
		
		Employee employee = this.employeeService.getEmployee(empNum);
		this.employeeService.updateEmployee(employee, lastName, firstName);		
		ModelAndView modelAndView = new ModelAndView("forward:/employee.html");
		return modelAndView;         
	}		
}
