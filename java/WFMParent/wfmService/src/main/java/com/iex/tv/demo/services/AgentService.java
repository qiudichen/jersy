package com.iex.tv.demo.services;

import java.util.List;

import com.iex.tv.domain.training.Agent;

public interface AgentService {
	public Agent getAgent(long id);
	
	public List<Agent> getAgents();
	
	public void addAgent(long skillId);
}
