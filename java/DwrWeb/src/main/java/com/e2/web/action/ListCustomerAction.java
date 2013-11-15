package com.e2.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.e2.web.model.Customer;
import com.e2.web.service.CustomerBo;

@Component("/ListCustomer")
public class ListCustomerAction extends Action { // ActionSupport {
	@Autowired
	@Qualifier("customerBoImpl")
	private CustomerBo customerBo;
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
	        throws Exception {
	 
			//only extends from ActionSupport, but bean is in different spring contain from autowired bean
			//CustomerBo customerBo1 = (CustomerBo) getWebApplicationContext().getBean("customerBoImpl");
			
			DynaActionForm dynaCustomerListForm = (DynaActionForm)form;
			
			List<Customer> list = customerBo.findAllCustomer();
			
			dynaCustomerListForm.set("customerList", list);
		        
			return mapping.findForward("success");
		  
		}
}
