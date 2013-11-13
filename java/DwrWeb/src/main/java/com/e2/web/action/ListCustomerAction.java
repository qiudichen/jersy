package com.e2.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.springframework.web.struts.ActionSupport;

import com.e2.web.model.Customer;
import com.e2.web.service.CustomerBo;

public class ListCustomerAction extends ActionSupport {
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
	        throws Exception {
	 
			CustomerBo customerBo = (CustomerBo) getWebApplicationContext().getBean("customerBoImpl");
			
			DynaActionForm dynaCustomerListForm = (DynaActionForm)form;
			
			List<Customer> list = customerBo.findAllCustomer();
			
			dynaCustomerListForm.set("customerList", list);
		        
			return mapping.findForward("success");
		  
		}
}