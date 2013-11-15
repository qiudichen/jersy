package com.e2.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.e2.web.form.CustomerForm;
import com.e2.web.model.Customer;
import com.e2.web.service.CustomerBo;

@Component("/AddCustomer")
public class AddCustomerAction extends Action { // ActionSupport {
	
	@Autowired
	@Qualifier("customerBoImpl")
	private CustomerBo customerBo;

	@Override
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) 
	        throws Exception {
	 
			//CustomerBo customerBo = (CustomerBo) getWebApplicationContext().getBean("customerBoImpl");
			
			CustomerForm customerForm = (CustomerForm)form;
			Customer customer = new Customer();
			
			//copy customerform to model
			BeanUtils.copyProperties(customer, customerForm);
			
			//save it
			customerBo.addCustomer(customer);
		        
			return mapping.findForward("success");
		}
}
