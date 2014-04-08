package com.iex.tv.demo.dao;

import java.util.List;

import com.iex.tv.domain.training.Agent;

public interface AgentDao {
	public Agent getAgent(long id);
	
	public List<Agent> getAgents();
	
	public Agent addAgent(Agent agent, long skillId);
}
