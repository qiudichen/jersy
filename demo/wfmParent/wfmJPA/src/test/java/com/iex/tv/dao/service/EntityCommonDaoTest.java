package com.iex.tv.dao.service;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import com.iex.tv.dao.core.EntityCommonDao;
import com.iex.tv.dao.core.EntityCommonDaoImpl;
import com.iex.tv.domain.Skill;

public class EntityCommonDaoTest  extends BaseDaoTest {
	
	private EntityCommonDao<Skill, Long> skillDao = new EntityCommonDaoImpl<Skill, Long>() {
		@Override
		protected EntityManager getEntityManager() {
			return EntityCommonDaoTest.this.em;
		}
	};
	
	@Test
	public void persist() {
		String name = "skill one";
		Skill skill = new Skill(name, 100);
		skillDao.persist(skill);
		Assert.assertTrue(skill.getId() > 0);
	}
}
