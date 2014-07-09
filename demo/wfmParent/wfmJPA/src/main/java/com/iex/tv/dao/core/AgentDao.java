package com.iex.tv.dao.core;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.iex.tv.domain.AccessControlDetl;
import com.iex.tv.domain.Address;
import com.iex.tv.domain.Agent;
import com.iex.tv.domain.Skill;

public interface AgentDao extends EntityCommonDao<Agent, Long> {
	public List<AccessControlDetl> getAcl(Collection<String> permissionOids, String parentOid);
    public List<Agent> simpleQuery();
    public List<Agent> simpleQuerywithOrder();
    public List<Agent> simpleSubsetQuery();
    public List<Agent> simpleQuery(String firstName, String lastName);
    public List<Agent> simpleQueryWithLiteral(String lastName);
    public Number simpleQueryWithSummary();
    public Date simpleQueryWithSummaryNonNumberField();
    public Number simpleQueryCount();
    public List<Address> simpleQueryJoin(String lastName);
    public List<Address> simpleQueryFetchJoin(String lastName);
    public List<Agent> findAgentsHasOnlyOneSkill(Long skillId);	
}
