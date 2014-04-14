package com.iex.tv.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.iex.tv.domain.Address;
import com.iex.tv.domain.Agent;
import com.iex.tv.domain.AgentDetail;
import com.iex.tv.domain.Person;
import com.iex.tv.domain.Phone;
import com.iex.tv.domain.Skill;

public class AgentServiceTest extends BaseServiceTest {
	@Resource (name="skillServiceImpl")
	private SkillService skillService;
	
	@Resource (name="agentServiceImpl")
	private AgentService agentService;
	
	@Test
	public void fullCycleTest() {
		String name = "Agent Skill Test";
		List<Agent> agents = agentService.findAgentByNamedQuery("D", "C");
		
		agents = agentService.findSubsetAgentByNamedQuery("D", "C");
//		for(int i = 0; i < 3; i++) {
//			long skillid = skillService.addSkill(new Skill(name + i));
//			Assert.assertTrue(skillid > 0);
//		}
		
		List<Skill> skills = skillService.findSkillByName(name);
		
		Agent agent = new Agent(new Person("David", null, "Chen"), new Date());
		
		agent.setSkills(new HashSet<Skill>(skills));
		
		agent.setAgentDetail(new AgentDetail("Agent Description"));
		
		agent.addAddress(new Address("1701 main", "Allen", "TX", "US", "75013"));
		agent.addAddress(new Address("1702 main", "Allen", "TX", "US", "75013"));

		agent.addPhone(new Phone("1800343232"));
		agent.addPhone(new Phone("1999999999"));

		long agentId = agentService.addAgent(agent);
		
		Agent agentDB = agentService.getAgent(agentId);
		Assert.assertNotNull(agentDB);
		
	}
}
