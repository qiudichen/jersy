package com.iex.tv.demo.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iex.tv.demo.dao.AgentDao;
import com.iex.tv.domain.training.Address;
import com.iex.tv.domain.training.Agent;
import com.iex.tv.domain.training.Person;
import com.iex.tv.domain.training.Phone;

@Service("agentServiceImpl")
public class AgentServiceImpl implements AgentService {

	@Autowired
	@Qualifier("agentDaoImpl")	
	private AgentDao agentDao;
	
	@Override
	public Agent getAgent(long id) {
		return this.agentDao.getAgent(id);
	}
	
	@Override
	public List<Agent> getAgents() {
		return agentDao.getAgents();
	}
	
	@Override
	@Transactional(value="demoTransactionManager", propagation=Propagation.REQUIRED, rollbackFor=DBRollback.class)
	public void addAgent(long skillId) {
		Agent agent = new Agent(new Person("firstName", "middleName", "lastName"), new Date()) ;
		agent.addAddress(new Address("1300 main", "Plano", "TX", "US","10001"));
		agent.addPhone(new Phone("1800"));
		agentDao.addAgent(agent, skillId);
	}

}
