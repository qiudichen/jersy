package com.demo.persistent.dao;

import java.util.List;

import com.demo.persistent.domain.Agentcallout;

public interface AgentcalloutDAO {
	long save(Agentcallout agentcallout);

	Agentcallout get(long id);

	List<Agentcallout> list();

	Agentcallout update(Agentcallout agentcallout);

	void delete(long id);
	
	List<Agentcallout>  findAgentcalloutByServicename(String servicename);
}
