package com.iex.tv.web.demo.controller;

import java.util.Date;
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
import com.iex.tv.demo.services.EmployeeService;
import com.iex.tv.domain.training.Employee;

@Controller
public class EmployeeController {
	@Autowired
	@Qualifier("employeeServiceImpl")	
	private EmployeeService employeeService;
	
	@RequestMapping(value="/employee")
	public ModelAndView viewEmployee(HttpServletRequest request) {
		
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

		try {
			this.employeeService.addEmployee(lastName, firstName, "M".equals(gender) ? Employee.Gender.M : Employee.Gender.F);
		} catch (DBRollback e) {
			e.printStackTrace();
		}
		
		ModelAndView modelAndView = new ModelAndView("forward:/employee.html");
		return modelAndView;         
	}
	
	@RequestMapping(value="/deleteProperty")
	public ModelAndView deleteProperty(HttpServletRequest request, @RequestParam (value="empNum", required=true) long empNum) {
		try {
			this.employeeService.deleteEmployee(empNum);
		} catch (DBRollback e) {
			e.printStackTrace();
		}
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
		
		try {
			this.employeeService.updateEmployee(employee, lastName, firstName);
		} catch (DBRollback e) {
			e.printStackTrace();
		}
		
		ModelAndView modelAndView = new ModelAndView("forward:/employee.html");
		return modelAndView;         
	}	
}
