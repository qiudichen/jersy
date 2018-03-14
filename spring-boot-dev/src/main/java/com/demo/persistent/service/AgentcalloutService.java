package com.demo.persistent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.persistent.dao.AgentcalloutDAO;
import com.demo.persistent.domain.Agentcallout;

@Service
public class AgentcalloutService {

	@Autowired
	public AgentcalloutDAO agentcalloutDAO;
	
	public AgentcalloutService() {
		
	}
	
	public void init() {
		System.out.println("---");
	}
	
	@Transactional
	public long addAgentcallout(String agentcalloutid, String servicename) {
		Agentcallout agentcallout = new Agentcallout(agentcalloutid, servicename);
		long id = agentcalloutDAO.save(agentcallout);
		return id;
	}

//	Agentcallout get(long id);
//
//	List<Agentcallout> list();
//
//	Agentcallout update(Agentcallout agentcallout);
//
//	void delete(long id);
//	
//	List<Agentcallout>  findAgentcalloutByServicename(String servicename);
}
