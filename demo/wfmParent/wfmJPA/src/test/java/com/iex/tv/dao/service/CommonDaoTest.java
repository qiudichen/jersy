package com.iex.tv.dao.service;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import com.iex.tv.dao.core.CommonDao;
import com.iex.tv.dao.core.CommonDaoImpl;
import com.iex.tv.domain.Skill;
import com.iex.tv.utils.ExceptionUtils.Error;

public class CommonDaoTest  extends BaseDaoTest {

	CommonDao commonDao = new CommonDaoImpl() {
		@Override
		protected EntityManager getEntityManager() {
			return CommonDaoTest.this.em;
		}
	};
	
	@Test
	public void persistTest() {
		Integer rank = 100;
		
		Skill skill = new Skill("skill one", rank);
		commonDao.persist(skill, true);
		commonDao.flush();
		Assert.assertTrue(skill.getId() > 0);

		skill = new Skill("skill one", rank);
		try {
			commonDao.persist(skill, false);
			commonDao.flush();
		} catch(Exception e) {
			Error error = exceptionUtils.getCausedError(e);
			error.printStackTrace();
		}
		Assert.assertTrue(skill.getId() > 0);		
	}
}
