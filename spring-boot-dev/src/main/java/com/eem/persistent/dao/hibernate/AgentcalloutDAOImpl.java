package com.eem.persistent.dao.hibernate;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.demo.persistent.dao.AgentcalloutDAO;
import com.demo.persistent.domain.Agentcallout;

@Repository
public class AgentcalloutDAOImpl implements AgentcalloutDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public AgentcalloutDAOImpl() {
		System.out.println("---");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("---");
	}

	@Override
	public long save(Agentcallout agentcallout) {
		getSession().save(agentcallout);
		return agentcallout.getId();
	}

	@Override
	public Agentcallout get(long id) {
		return getSession().get(Agentcallout.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agentcallout> list() {
		return getSession().createQuery("from Agentcallout").list();
	}

	@Override
	public Agentcallout update(Agentcallout agentcallout) {
		Agentcallout dbAgentcallout = (Agentcallout)getSession().merge(agentcallout);
		return dbAgentcallout;
	}

	@Override
	public void delete(long id) {
		Session session = getSession();
		Agentcallout agentcallout = (Agentcallout)session.load(Agentcallout.class, id); 
        session.delete(agentcallout); 
	}

	@Override
	public List<Agentcallout>  findAgentcalloutByServicename(String servicename) {
		Query query = getSession().getNamedQuery("findAgentcalloutByServicename");  
	    query.setString("servicename", servicename);  
	          
	    List<Agentcallout> agentcallouts= query.list(); 
	    return agentcallouts;
	}
	
	private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
