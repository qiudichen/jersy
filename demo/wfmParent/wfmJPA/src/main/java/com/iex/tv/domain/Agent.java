package com.iex.tv.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.REFRESH;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.jpa.QueryHints;

@SuppressWarnings("serial")
@Entity
@Table(name="AGENT")

@NamedQueries({
   @NamedQuery(name = Agent.NamedQuery.QUERY_FIND_BY_NAME, 
		   query="SELECT a FROM Agent a WHERE a.person.firstName like :firstName and a.person.lastName like :lastName and a.skills is NOT Empty",
		   hints = {@QueryHint(name=QueryHints.HINT_COMMENT, value="Named Query Comment"),
		   			@QueryHint(name=QueryHints.HINT_CACHEABLE, value="true")}), 
   @NamedQuery(name = Agent.NamedQuery.QUERY_FIND_BY_JOIN, 
		   query="SELECT new com.iex.tv.domain.Agent(a.id, a.person.firstName, a.person.lastName, d.description) "
		   		+ "FROM Agent a LEFT OUTER JOIN a.agentDetail d WHERE size(a.skills) = 1 and :skill MEMBER OF a.skills"), 
   @NamedQuery(name = Agent.NamedQuery.QUERY_FIND_SUBSET_BY_ID, 
   	query="SELECT new com.iex.tv.domain.Agent(a.id, a.person.firstName, a.person.middleName, a.person.lastName, a.startDate) "
   			+ "FROM Agent a WHERE a.person.firstName like :firstName and a.person.lastName like :lastName ")
}) 

public class Agent extends CreateDateEntity {
	@Id 
	@Column(name="AGENT_ID")
	@SequenceGenerator(name="seqAgentId", sequenceName="SEQ_AGT_ID", initialValue = 100, allocationSize = 5)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seqAgentId")
	private long id;

	private Person person;
	
    @Temporal(TemporalType.DATE)    
	@Column(name="START_DATE", nullable = false)	
	private Date startDate;
    
    @OneToOne(cascade = ALL, orphanRemoval=true)
    @JoinColumn(name="AGENT_ID", referencedColumnName="AGENT_DETAIL_ID")
    private AgentDetail agentDetail;

    @OneToMany(cascade= ALL, fetch = FetchType.EAGER, orphanRemoval=true, mappedBy="agent")
	private Set<Address> addresses;
	
	@OneToMany(cascade = ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="AGENT", referencedColumnName="AGENT_ID")
	private Set<Phone> phones;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { REFRESH, DETACH })
	@JoinTable(name="AGT_SKILL", joinColumns={@JoinColumn(name="AGENT", referencedColumnName="AGENT_ID")},
				inverseJoinColumns=@JoinColumn(name="SKILL", referencedColumnName="SKILL_ID"))
	private Set<Skill> skills; 

	public Agent() {
		super();
	}

	public Agent(long id, Person person, Date startDate, Set<Address> addresses) {
		super();
		this.id = id;
		this.person = person;
		this.startDate = startDate;
		this.addresses = addresses;
	}
	
	public Agent(long id, String firstName, String lastName, String description) {
		super();
		this.id = id;
		this.person = new Person(firstName, lastName);
		this.agentDetail = new AgentDetail(this, description);
	}
	
	public Agent(long id, String firstName, String middleName, String lastName, Date startDate) {
		super();
		this.id = id;
		this.person = new Person(firstName, middleName, lastName);
		this.startDate = startDate;
	}
	
	public Agent(Person person, Date startDate) {
		super();
		this.person = person;
		this.startDate = startDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}
	
	public void addAddress(Address address) {
		if(this.addresses == null) {
			this.addresses = new HashSet<Address>(10);
		}
		this.addresses.add(address);
		address.setAgent(this);
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	public void addPhone(Phone phone) {
		if(this.phones == null) {
			this.phones = new HashSet<Phone>(10);
		};
		this.phones.add(phone);
		phone.setAgentId(this.id);
	}
	
	public Set<Skill> getSkills() {
		return skills;
	}

	public void setSkills(Set<Skill> skills) {
		this.skills = skills;
	}
	
	public void addSkill(Skill skill) {
		if(this.skills == null) {
			this.skills = new HashSet<Skill>(10);
		};
		this.skills.add(skill);
	}

	public AgentDetail getAgentDetail() {
		return agentDetail;
	}

	public void setAgentDetail(AgentDetail agentDetail) {
		this.agentDetail = agentDetail;
		if(this.agentDetail != null) {
			this.agentDetail.setAgent(this);
		}
	}
	
	public interface NamedQuery {
		public static final String QUERY_FIND_BY_NAME = "Agent.findByName";
		
		public static final String QUERY_FIND_BY_JOIN = "Agent.findByJoin";
		
		public static final String QUERY_FIND_SUBSET_BY_ID = "Agent.findSubSetById";
	}	
}