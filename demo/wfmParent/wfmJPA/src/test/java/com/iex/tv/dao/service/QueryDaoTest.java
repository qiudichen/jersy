package com.iex.tv.dao.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import com.iex.tv.dao.core.AgentDao;
import com.iex.tv.domain.AccessControlDetl;
import com.iex.tv.domain.Address;
import com.iex.tv.domain.Agent;

public class QueryDaoTest extends BaseDaoTest {
	@Resource
	AgentDao agentDao;
	
	@Test
	public void queryTest() {		
		Set<String> permissionOids = new HashSet<String>();
		for(int i = 10; i < 16; i++) {
			permissionOids.add("" + i);			
		}

		String parentOid = "1";
		List<AccessControlDetl> result = agentDao.getAcl(permissionOids, parentOid);
		Assert.assertNotNull(result);
	}
	
	@Test
    public void simpleQueryTest() {
    	List<Agent> result = agentDao.simpleQuery();
    	Assert.assertNotNull(result);
    }
    
	@Test
    public void simpleQuerywithOrderTest() {
    	List<Agent> result = agentDao.simpleQuerywithOrder();
    	Assert.assertNotNull(result);
    }

	@Test
    public void simpleSubsetQueryTest() {
    	List<Agent> result = agentDao.simpleSubsetQuery();
    	Assert.assertNotNull(result);
    }
    

	@Test
    public void simpleQueryWithLiteralTest() {
    	String lastName = "";
    	List<Agent> result = agentDao.simpleQueryWithLiteral(lastName);
    	Assert.assertNotNull(result);
    }
    
	@Test
    public void simpleQueryWithSummaryTest() {
    	Number number = agentDao.simpleQueryWithSummary();
    	Assert.assertNotNull(number);
    }
    
	@Test
    public void simpleQueryWithSummaryNonNumberFieldTest() {
    	Date date = agentDao.simpleQueryWithSummaryNonNumberField();
    	Assert.assertNotNull(date);
    }
    
	@Test
	public void simpleQueryCountTest() {
		Number number = agentDao.simpleQueryCount();
    	Assert.assertNotNull(number);
	}
	
	@Test
    public void simpleQueryJoinTest() {
    	String lastName = null;
    	List<Address> result = agentDao.simpleQueryFetchJoin(lastName);
    	Assert.assertNotNull(result);
    }
    
	@Test
    public void simpleQueryFetchJoinTest() {
    	String lastName = null;
    	List<Address> result = agentDao.simpleQueryFetchJoin(lastName);
    	Assert.assertNotNull(result);
    }
    
	@Test
    public void findAgentsHasOnlyOneSkillTest() {
    	Long skillId = 1l;
    	List<Agent> result = agentDao.findAgentsHasOnlyOneSkill(skillId);
    	Assert.assertNotNull(result);
    }
}
