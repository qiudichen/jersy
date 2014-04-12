package com.iex.tv.dao.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.dao.core.EntityCommonDao;
import com.iex.tv.dao.core.EntityCommonDaoImpl;
import com.iex.tv.domain.Skill;

@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/app-test-context.xml"})
@Transactional

public class EntityCommonDaoTest {
	
	@PersistenceContext(name="emDemo")
	protected EntityManager em;
	
	private EntityCommonDao<Skill, Long> shiftDao = new EntityCommonDaoImpl<Skill, Long>() {
		@Override
		protected EntityManager getEntityManager() {
			return EntityCommonDaoTest.this.em;
		}
	};
	

	@Test
	public void persist() {
		System.out.println("");
	}
}
