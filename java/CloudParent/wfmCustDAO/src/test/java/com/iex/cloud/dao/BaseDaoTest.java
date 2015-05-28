package com.iex.cloud.dao;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/dao-test-context.xml"})
public class BaseDaoTest {
	@Resource(name = "transactionServiceImpl")
	protected TransactionService service;
}
