package com.iex.tv.services;

import java.util.Date;
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

		List<Agent> agents1 = agentService.findAgentByNamedQuery("a", "a");
		List<Skill> skills = skillService.findSkillByName(name);

		if(skills.isEmpty()) {
			for(int i = 0; i < 3; i++) {
				long skillid = skillService.addSkill(new Skill(name + i, 30 + i*10));
				Assert.assertTrue(skillid > 0);
			}
			
			skills = skillService.findSkillByName(name);
		} 
		
		Skill newSkill = skills.remove(skills.size() -1);
		
		Agent agent = createAgent(skills, 0);

		long agentId = agentService.addAgent(agent);
		
		Agent agentDB = agentService.getAgent(agentId);
		Assert.assertNotNull(agentDB);
		
		List<Agent> agents = agentService.findAgentByNamedQuery("a", "a");
		Assert.assertTrue(agents.size() > 0);
		
		agents = agentService.findSubsetAgentByNamedQuery("a", "a");
		Assert.assertTrue(agents.size() > 0);
		
		updateAgent(agentDB, newSkill);
		agentService.update(agentDB);
		Assert.assertNotNull(agentDB);
		
		agentDB = agentService.getAgent(agentId);
		agentService.remove(agentDB);
		//agentService.remove(agentId);
		agentDB = agentService.getAgent(agentId);
		
		for(int i = 0; i < 10; i++) {
			agent = createAgent(skills, 0);
			agentId = agentService.addAgent(agent);
		}
		
		agents = agentService.findSubsetAgentByNamedQuery("a", "a");
		Assert.assertTrue(agents.size() > 0);
	}
	
	private Agent createAgent(List<Skill> skills, int i) {
		Agent agent = new Agent(new Person("agtFirst" + i, null, "agtLast" + i), new Date());
		agent.setAgentDetail(new AgentDetail("Agent Description"));
		
		for(Skill skill : skills) {
			if(!skill.getName().endsWith("2")) {
				agent.addSkill(skill);
			}
		}
		
		agent.addAddress(new Address("1701 main", "Allen", "TX", "US", "75013"));
		agent.addAddress(new Address("1702 main", "Allen", "TX", "US", "75013"));

		agent.addPhone(new Phone("180011111111"));
		agent.addPhone(new Phone("180011111112"));

		return agent;
	}
	
	private void updateAgent(Agent agentDB, Skill newSkill) {
		agentDB.getPerson().setFirstName("changed");
		agentDB.getAgentDetail().setDescription("changed");
		agentDB.setAgentDetail(null);
		
		for(Address address : agentDB.getAddresses()) {
			agentDB.getAddresses().remove(address);
			break;
		}

		for(Address address : agentDB.getAddresses()) {
			address.setStreet("Changed ");
			break;
		}		
		
		agentDB.addAddress(new Address("1703 main", "Allen", "TX", "US", "75013"));
		
//		for(Phone phone : agentDB.getPhones()) {
//			agentDB.getPhones().remove( phone);
//			break;
//		}
		
//		for(Phone phone : agentDB.getPhones()) {
//			phone.setPhone("Changed");
//		}
//		agentDB.addPhone(new Phone("180011111113"));
		
		for(Skill skill : agentDB.getSkills()) {
			agentDB.getSkills().remove(skill);
			break;
		}
		
		agentDB.addSkill(newSkill);
	}
}
