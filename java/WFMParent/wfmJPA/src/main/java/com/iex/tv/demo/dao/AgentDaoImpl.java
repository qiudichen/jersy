package com.iex.tv.demo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.iex.tv.domain.training.Agent;
import com.iex.tv.domain.training.Phone;
import com.iex.tv.domain.training.Skill;

@Repository("agentDaoImpl")
public class AgentDaoImpl implements AgentDao {
	@PersistenceContext(name="emDemo") 
	protected EntityManager em;
	
//	@Autowired
//	@Qualifier("skillDaoImpl")	
//	private SkillDao skillDao;
	
	@Override
	public Agent getAgent(long id) {
		return em.find(Agent.class, id);
	}
	
	@Override
	public List<Agent> getAgents() {
		TypedQuery<Agent> q = em.createQuery("SELECT e, a FROM Agent e LEFT JOIN e.addresses a ORDER BY a.city", Agent.class);
    	List<Agent> result = q.getResultList();
    	return result;
	}	
	
	@Override
	public Agent addAgent(Agent agent, long skillId) {
		Skill skill = em.getReference(Skill.class, skillId);
		agent.addSkill(skill);
		List<Phone> phones = agent.getPhones();
		agent.setPhones(null);
		em.persist(agent);
		if(phones != null && !phones.isEmpty()) {
			em.flush();
			for(Phone phone : phones) {
				phone.setAgentId(agent.getId());
				em.persist(phone);
			}
		}
		em.refresh(agent);
		return agent;
	}	
}
