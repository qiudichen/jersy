package com.iex.tv.services;

import java.util.List;

import com.iex.tv.domain.Agent;

public interface AgentService {
	public long addAgent(Agent agent);
	
	public Agent update(Agent agent);
	
	public void remove(Agent agent);

	public void remove(long agentId);
	
	public Agent getAgent(long agentId);
	
	public List<Agent> findSubsetAgentByNamedQuery(String firstName, String lastName);
	
	public List<Agent> findAgentByNamedQuery(String firstName, String lastName);
}
