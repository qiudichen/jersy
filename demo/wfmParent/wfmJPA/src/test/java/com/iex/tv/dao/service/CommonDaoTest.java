package com.iex.tv.dao.service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.dao.core.CommonDao;
import com.iex.tv.dao.core.CommonDaoImpl;
import com.iex.tv.domain.BaseEntity;
import com.iex.tv.domain.Skill;
import com.iex.tv.utils.ExceptionUtils;
import com.iex.tv.utils.ExceptionUtils.Error;

@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-test-context.xml"})
@Transactional
public class CommonDaoTest {
	@PersistenceContext(name="emDemo")
	protected EntityManager em;
	
	@Resource (name="exceptionUtils")
	private ExceptionUtils exceptionUtils;
	
	CommonDao commonDao = new CommonDaoImpl() {
		@Override
		protected EntityManager getEntityManager() {
			return CommonDaoTest.this.em;
		}
	};
	
	@Test
	public void persistTest() {
		Skill skill = new Skill("skill one");
		commonDao.persist(skill, true);
		commonDao.flush();
		Assert.assertTrue(skill.getId() > 0);

		skill = new Skill("skill one");
		try {
			commonDao.persist(new BaseEntity(){}, false);
			commonDao.flush();
		} catch(Exception e) {
			Error error = exceptionUtils.getCausedError(e);
			error.printStackTrace();
		}
		Assert.assertTrue(skill.getId() > 0);		
	}
}
