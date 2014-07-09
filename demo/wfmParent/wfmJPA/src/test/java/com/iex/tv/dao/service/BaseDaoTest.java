package com.iex.tv.dao.service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.utils.ExceptionUtils;

@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-test-context.xml"})
@Transactional
public class BaseDaoTest {
	@PersistenceContext(name="emDemo")
	protected EntityManager em;
	
	@Resource (name="exceptionUtils")
	protected ExceptionUtils exceptionUtils;
}
