package com.iex.tv.web.demo.controller;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

public class EmployeeControllerTest extends TestCase {
	@Test
	public void testHandleRequestView() throws Exception{
		EmployeeController controller = new EmployeeController();
        ModelAndView modelAndView = controller.viewEmployee(null);
        assertEquals("employee", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel());
        String nowValue = (String) modelAndView.getModel().get("now");
        assertNotNull(nowValue);
    }
}
