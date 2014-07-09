package com.iex.tv.dao.core;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;

import org.springframework.stereotype.Repository;

import com.iex.tv.domain.AccessControl;
import com.iex.tv.domain.AccessControlDetl;
import com.iex.tv.domain.Address;
import com.iex.tv.domain.Agent;
import com.iex.tv.domain.Skill;

@Repository("agentQueryDaoImpl")
public class AgentQueryDaoImpl extends DemoDaoImpl<Agent, Long> implements AgentDao {
	
	@Override
	public List<AccessControlDetl> getAcl(Collection<String> permissionOids, String parentOid) {

		TypedQuery<AccessControlDetl> query = this.getEntityManager().createQuery(
				"from AccessControlDetl a " +
						"join fetch a.accessControl " + 
				"where a.permission.oid in :permissionOids " + 
				"and a.accessControl.oid in (select b.oid from AccessControl b where b.parent.oid = :parentOid) ", AccessControlDetl.class);
		
		query.setParameter("permissionOids", permissionOids);
		query.setParameter("parentOid", parentOid);
		List<AccessControlDetl> list = query.getResultList();
		
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<AccessControlDetl> criteriaQuery = criteriaBuilder.createQuery(AccessControlDetl.class);
		Root<AccessControlDetl> from = criteriaQuery.from(AccessControlDetl.class);
		
		Path<String> persissionOidPath = from.join("permission").get("oid");
		Predicate persissionOidExp = persissionOidPath.in(permissionOids);
		
		Path<Object> path = from.join("accessControl").get("oid"); 

		CriteriaQuery<AccessControlDetl> select = criteriaQuery.select(from);
		
		Subquery<String> subquery = criteriaQuery.subquery(String.class);
		Root<AccessControl> fromAccessControl = subquery.from(AccessControl.class);
		
		Path<String> subSelectPath = fromAccessControl.get("oid");
		subquery.select(subSelectPath);
		subquery.where(criteriaBuilder.equal(fromAccessControl.get("parent.oid"), parentOid));
		
		In<Object> inClause = criteriaBuilder.in(path).value(subquery);
		select.where(criteriaBuilder.and(persissionOidExp, inClause));
		
		TypedQuery<AccessControlDetl> typedQuery = createQuery(select);
		List<AccessControlDetl> resultList = typedQuery.getResultList();		
		return resultList;
	}
	
	//simple query
	@Override
	public List<Agent> simpleQuery() {
		//JPSQL
		TypedQuery<Agent> query = this.getEntityManager().createQuery("from Agent a", Agent.class);
		List<Agent> list = query.getResultList();
		
		//CriteriaBuilder
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<Agent> criteriaQuery = criteriaBuilder.createQuery(Agent.class);
		Root<Agent> from = criteriaQuery.from(Agent.class);
		CriteriaQuery<Agent> select = criteriaQuery.select(from);
		
		TypedQuery<Agent> typedQuery = createQuery(select);
		List<Agent> resultList = typedQuery.getResultList();
		return resultList;
	}

	//simple query with Order
	@Override
	public List<Agent> simpleQuerywithOrder() {
		//JPSQL
		TypedQuery<Agent> query = this.getEntityManager().createQuery("from Agent a order by a.person.lastName asc, a.person.firstName desc", Agent.class);
		List<Agent> list = query.getResultList();
		
		//CriteriaBuilder
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<Agent> criteriaQuery = criteriaBuilder.createQuery(Agent.class);
		Root<Agent> from = criteriaQuery.from(Agent.class);
		CriteriaQuery<Agent> select = criteriaQuery.select(from);
		
		//set order
		select.orderBy(criteriaBuilder.asc(from.get("person.lastName")), criteriaBuilder.desc(from.get("person.firstName")));
		
		TypedQuery<Agent> typedQuery = createQuery(select);
		List<Agent> resultList = typedQuery.getResultList();
		return resultList;
	}
	
	//simple query with selected fields
	@Override
	public List<Agent> simpleSubsetQuery() {
		//JPSQL
		TypedQuery<Agent> query = this.getEntityManager().createQuery("select a.id, a.person.lastName, a.person.firstName from Agent a", Agent.class);
		List<Agent> list = query.getResultList();
		
		//CriteriaBuilder
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<Agent> criteriaQuery = criteriaBuilder.createQuery(Agent.class);
		Root<Agent> from = criteriaQuery.from(Agent.class);
		CriteriaQuery<Agent> select = criteriaQuery.multiselect(from.get("id"), from.get("person.lastName"), from.get("person.firstName")); 
		
		TypedQuery<Agent> typedQuery = createQuery(select);
		List<Agent> resultList = typedQuery.getResultList();
		return resultList;
	}

	//simple query with simple criteria
	@Override
	public List<Agent> simpleQuery(String firstName, String lastName) {
		//JPSQL
		TypedQuery<Agent> query = this.getEntityManager().createQuery("from Agent a where a.person.firstName like :firstName and a.person.lastName like :lastName", Agent.class);
		query.setParameter("firstName", firstName + "%"); 
		query.setParameter("lastName", lastName + "%"); 
		List<Agent> list = query.getResultList();
		
		//CriteriaBuilder
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<Agent> criteriaQuery = criteriaBuilder.createQuery(Agent.class);
		Root<Agent> from = criteriaQuery.from(Agent.class);
		
		CriteriaQuery<Agent> select = criteriaQuery.select(from);
		
		Path<String> firstExp = from.get("person.firstName");
		Predicate firstPredicate = criteriaBuilder.like(firstExp, firstName);

		Path<String> lastExp = from.get("person.lastName");
		Predicate lastPredicate = criteriaBuilder.like(lastExp, lastName);
		
		CriteriaQuery<Agent> where = criteriaQuery.where(criteriaBuilder.and(firstPredicate, lastPredicate)); 
		
		TypedQuery<Agent> typedQuery = createQuery(select);
		List<Agent> resultList = typedQuery.getResultList();
		return resultList;
	}

	
	//simple query with simple criteria
	@Override
	public List<Agent> simpleQueryWithLiteral(String lastName) {
		//JPSQL
		TypedQuery<Agent> query = getEntityManager().createQuery("from Agent a where upper(a.person.lastName) like upper(:lastName)", Agent.class);
		query.setParameter("lastName", lastName);
		List<Agent> list = query.getResultList();
		
		//CriteriaBuilder
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		
		CriteriaQuery<Agent> criteriaQuery = criteriaBuilder.createQuery(Agent.class);
		Root<Agent> from = criteriaQuery.from(Agent.class);
		
		CriteriaQuery<Agent> select = criteriaQuery.select(from);
		
		Expression<String> literal = criteriaBuilder.upper(criteriaBuilder.literal(lastName + "%"));
		Path<String> lastExp = from.get("person.lastName");
		Predicate predicate = criteriaBuilder.like(criteriaBuilder.upper(lastExp), literal);
		
		criteriaQuery.where(predicate);
		
		TypedQuery<Agent> typedQuery = createQuery(select);
		List<Agent> resultList = typedQuery.getResultList();
		return resultList;
	}

	//query with summary (min, max, agv)
	@Override
	public Number simpleQueryWithSummary() {
		//JPSQL
		Query query = getEntityManager().createQuery("select max(a.id) from Agent a");
		Number count = (Number)query.getSingleResult();
		
		//CriteriaBuilder
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<Number> criteriaQuery = criteriaBuilder.createQuery(Number.class);
		Root<Agent> from = criteriaQuery.from(Agent.class);
		
		Path<Number> maxPath = from.get("id");
		Expression<Number> maxExp = criteriaBuilder.max(maxPath);
		
		criteriaQuery.select(maxExp);
		
		TypedQuery<Number> typedQuery = createQuery(criteriaQuery);
		count =  (Number)typedQuery.getSingleResult();		
		return count;
	}

	//query with summary (min, max)
	@Override
	public Date simpleQueryWithSummaryNonNumberField() {
		//JPSQL
		Query query = getEntityManager().createQuery("select max(a.startDate) from Agent a");
		Date date = (Date)query.getSingleResult();
		
		//CriteriaBuilder
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<Date> criteriaQuery = criteriaBuilder.createQuery(Date.class);
		Root<Agent> from = criteriaQuery.from(Agent.class);
		
		Path<Date> maxPath = from.get("startDate");
		Expression<Date> maxExp = criteriaBuilder.greatest(maxPath);
		criteriaQuery.select(maxExp);
		
		TypedQuery<Date> typedQuery = createQuery(criteriaQuery);
		date =  (Date)typedQuery.getSingleResult();		
		return date;
	}
	
	//query count
	@Override
	public Number simpleQueryCount() {
		//JPSQL
		Query query = getEntityManager().createQuery("select count(*) from Agent a");
		Number count = (Number)query.getSingleResult();
		
		//CriteriaBuilder
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Agent> from = criteriaQuery.from(Agent.class);
		criteriaQuery.select(criteriaBuilder.count(from));
		
		//if don't want to count rows that are double
		//criteriaQuery.select(criteriaBuilder.countDistinct(from)); 
		TypedQuery<Long> typedQuery = createQuery(criteriaQuery);
		count =  (Number)typedQuery.getSingleResult();		
		return count;
	}

	//Simple Join query
	@Override
	public List<Address> simpleQueryJoin(String lastName) {
		//JPSQL
		TypedQuery<Address> query = this.getEntityManager().createQuery("select a from Address a where a.agent.person.lastName like :lastName ", Address.class);
		query.setParameter("lastName", lastName);
		List<Address> list = query.getResultList();
		
		//CriteriaBuilder
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<Address> criteriaQuery = criteriaBuilder.createQuery(Address.class);
		
		Root<Address> from = criteriaQuery.from(Address.class);
		Path<String> path = from.join("agent").get("person.lastName");
		CriteriaQuery<Address> select = criteriaQuery.select(from);
		
		select.where(criteriaBuilder.equal(path, lastName));
		TypedQuery<Address> typedQuery = createQuery(select);
		List<Address> resultList = typedQuery.getResultList();  
		return resultList;
	}
	
	//Simple Join query
	@Override
	public List<Address> simpleQueryFetchJoin(String lastName) {
		//JPSQL
		TypedQuery<Address> query = this.getEntityManager().createQuery("select a from Address a join fetch a.agent where a.agent.person.lastName like :lastName ", Address.class);
		query.setParameter("lastName", lastName);
		List<Address> list = query.getResultList();
		
		//CriteriaBuilder
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<Address> criteriaQuery = criteriaBuilder.createQuery(Address.class);
		
		Root<Address> from = criteriaQuery.from(Address.class);
		from.fetch("agent");

		Path<String> path = from.join("agent").get("person.lastName");
		CriteriaQuery<Address> select = criteriaQuery.select(from);
		select.where(criteriaBuilder.equal(path, lastName));
		
		TypedQuery<Address> typedQuery = createQuery(select);
		List<Address> resultList = typedQuery.getResultList();  
		return resultList;
	}
	
	@Override
	public List<Agent> findAgentsHasOnlyOneSkill(Long skillId) {
		Skill skill = this.getReference(Skill.class, skillId);
		
		CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
		CriteriaQuery<Agent> criteriaQuery = criteriaBuilder.createQuery(Agent.class);
		
		Root<Agent> from = criteriaQuery.from(Agent.class);
		
		EntityType<Agent> agentType = from.getModel();

		Expression<Collection<Skill>> Skills = from.get("skills");
		Predicate containsFavoritedProduct = criteriaBuilder.isMember(skill, Skills);

		criteriaQuery.where(containsFavoritedProduct);
		
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("person.lastName")), criteriaBuilder.desc(from.get("person.firstName"))); 
		//criteriaQuery.groupBy(...);
		
		List<Agent> agents = em.createQuery(criteriaQuery).getResultList();
		return agents;
	}
}