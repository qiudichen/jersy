package com.iex.cloud.dao;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iex.cloud.scope.ScopeContextProvider;
import com.iex.cloud.test.service.CallbackService;
import com.iex.cloud.test.service.TransactionalServiceWraper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/dao-test-context.xml"})
//@TransactionConfiguration(defaultRollback = true)
//@Transactional

@SuppressWarnings("unchecked")
public class BaseDaoTest {
	
	@Resource(name="custDataSource")
	protected DataSource dataSource;
	
	@Resource(name="custSessionFactory")
	protected SessionFactory sessionFactory;
	
	@Resource(name="readOnlyServiceWraperImpl")
	protected TransactionalServiceWraper readService;
	
	@Resource(name="transactionalServiceWraperImpl")
	protected TransactionalServiceWraper updateService;
	
	@Resource(name="customerId")
	protected String customerId;
	
	@Before
	public void switchIn() {
		if(!"default".equalsIgnoreCase(this.customerId)) {
			ScopeContextProvider.switchIn(this.customerId);
		}
	}	
	
	@After
	public void switchOut() {
		if(!"default".equalsIgnoreCase(this.customerId)) {
			ScopeContextProvider.switchOut();
		}
	}	
	
	protected Object executeUdpate(CallbackService action) throws Exception {
		try {
			return this.updateService.doExecute(action);
		} catch(Exception e) {
			throw e;
		}
	}
	
	protected Object execute(CallbackService action) throws Exception {
		try {
			return this.readService.doExecute(action);
		} catch(Exception e) {
			throw e;
		}
	}
}
