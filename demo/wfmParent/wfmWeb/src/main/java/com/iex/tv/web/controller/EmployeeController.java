package com.iex.tv.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iex.tv.domain.Employee;
import com.iex.tv.domain.Employee.Gender;
import com.iex.tv.services.EmployeeService;

@Controller
public class EmployeeController {
	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;	
	
	@RequestMapping(value="employee")
	public String viewEmployee(Model model) {	
		List<Employee> employees = this.employeeService.getEmployees();
		model.addAttribute("employees", employees);	
		return "employee";
	}

	@RequestMapping(value="/employee")
	public String addEmployee(@RequestParam (required=true) String firstName, 
			@RequestParam (required=true) String lastName, 
			@RequestParam (required=true) String gender) {
		this.employeeService.addEmployee(lastName, firstName, Gender.findGender(gender));
		return "forward:/employee.html";       
	}
	
	@RequestMapping(value="/updateEmployee", method=RequestMethod.POST)
	public String updateProperties(@RequestParam (required=true) String firstName, 
			@RequestParam (required=true) String lastName, 
			@RequestParam (required=true) long empNum) {
		this.employeeService.updateEmployee(empNum, lastName, firstName);
		return "forward:/employee.html";      
	}
	
	@RequestMapping(value="/{empNum}/deleteEmployee")
	public String deleteEmployee(@PathVariable("empNum") long empNum) {
		this.employeeService.deleteEmployee(empNum);
		return "forward:/employee.html";       
	}		
}
