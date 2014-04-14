package com.iex.tv.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.dao.core.EntityCommonDao;
import com.iex.tv.dao.core.QueryParameter;
import com.iex.tv.domain.Agent;
import com.iex.tv.domain.Phone;
import com.iex.tv.domain.Skill;

@Service("agentServiceImpl")
@Transactional(value="demoTransactionManager", propagation = Propagation.REQUIRED)
public class AgentServiceImpl implements AgentService {
	@Autowired
	@Qualifier("agentDAO")	
	private EntityCommonDao<Agent, Long> agentDao;
	
	@Autowired
	@Qualifier("skillDAO")	
	private EntityCommonDao<Skill, Long> skillDao;
	
	@Override
	public long addAgent(Agent agent) {
		Set<Phone> phones = agent.getPhones();
		agent.setPhones(null);
		this.agentDao.persist(agent, true);
		for(Phone phone : phones) {
			phone.setAgentId(agent.getId());
			this.agentDao.persist(phone);
		}
		return agent.getId();
	}

	@Override
	public Agent getAgent(long agentId) {
		return this.agentDao.findByPk(agentId);		
	}

	@Override
	public List<Agent> findAgentByNamedQuery(String firstName, String lastName) {
		return this.agentDao.findByNamedQuery(Agent.NamedQuery.QUERY_FIND_BY_NAME, 
				new QueryParameter(firstName + "%", "firstName"), new QueryParameter(lastName + "%", "lastName"));
	}

	@Override
	public List<Agent> findSubsetAgentByNamedQuery(String firstName,
			String lastName) {
		return this.agentDao.findByNamedQuery(Agent.NamedQuery.QUERY_FIND_SUBSET_BY_ID, 
				new QueryParameter(firstName + "%", "firstName"), new QueryParameter(lastName + "%", "lastName"));
	}
}
