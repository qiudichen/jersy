package com.eem.persistent.dao.jpa;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

import com.demo.persistent.dao.AgentcalloutDAO;
import com.demo.persistent.domain.Agentcallout;

@Repository
public class AgentcalloutDAOJpaImpl implements AgentcalloutDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	public AgentcalloutDAOJpaImpl() {
		System.out.println("---");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("---");
	}

	@Override
	public long save(Agentcallout agentcallout) {
		//SessionFactory sessionFactory = em.unwrap(SessionFactory.class);
		em.persist(agentcallout);
		em.flush();
		return agentcallout.getId();
	}

	@Override
	public Agentcallout get(long id) {
		return em.find(Agentcallout.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agentcallout> list() {
		String hql = "FROM Agentcallout";
		return (List<Agentcallout>) em.createQuery(hql).getResultList();
	}

	@Override
	public Agentcallout update(Agentcallout agentcallout) {
		Agentcallout dbAgentcallout = (Agentcallout)em.merge(agentcallout);
		em.flush();
		return dbAgentcallout;
	}

	@Override
	public void delete(long id) {
		Agentcallout agentcallout = em.getReference(Agentcallout.class, id);
		em.remove(agentcallout);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agentcallout> findAgentcalloutByServicename(String servicename) {
		Query query = em.createNamedQuery("findAgentcalloutByServicename");
		return query.getResultList();
	}
}
