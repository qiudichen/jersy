package com.iex.tv.domain.training;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@SuppressWarnings("serial")
@Entity
@Table(name="AGENT")

@NamedQueries({
    @NamedQuery(name = Agent.NamedQuery.QUERY_FIND_BY_SUBTYPE, query="SELECT a FROM Agent a WHERE a.startDate > :startDate "), 
    //@NamedQuery(name = Agent.NamedQuery.QUERY_GET_SECOND, query="SELECT s FROM SystemProperty s WHERE (SELECT count(s1) from SystemProperty s1 where s1.version < s.version) = 1"), 
    @NamedQuery(name = Agent.NamedQuery.QUERY_GET_COUNT, query="SELECT count(s) FROM SystemProperty s WHERE s.subsystem = :type")
}) 

public class Agent extends CreateDateEntity {
	public interface NamedQuery {
		public static final String QUERY_FIND_BY_SUBTYPE = "Agent.findBySubType";
		
		public static final String QUERY_GET_COUNT = "Agent.getCount";
		
		public static final String QUERY_GET_SECOND = "Agent.getSecondEarly";
	}
	
	
	@Id 
	@Column(name="AGENT_ID")
	@SequenceGenerator(name="seqAgentId", sequenceName="SEQ_AGT_ID", allocationSize = 5, initialValue = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seqAgentId")
	private long id;

	private Person person;
	
    @Temporal(TemporalType.DATE)    
	@Column(name="START_DATE", nullable = false)	
	private Date startDate;
    
	@OneToMany(cascade={ PERSIST, MERGE, REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name="AGENT")
	private List<Address> addresses;

	@OneToMany(cascade={ PERSIST, MERGE, REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name="AGENT")
	private List<Phone> Phones;
	
	@ManyToMany(cascade ={CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
	@JoinTable(name="AGT_SKILL", joinColumns={@JoinColumn(name="AGENT", referencedColumnName="AGENT_ID")},
				inverseJoinColumns=@JoinColumn(name="SKILL", referencedColumnName="SKILL_ID"))
	private List<Skill> skills; 
	
	
	static int transient1; // not persistent because of static
	transient int transient3; // not persistent because of transient	
	@Transient
	int transient4; // not persistent because of @Transient
	
	public Agent() {
		super();
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

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
	public void addAddress(Address address) {
		if(this.addresses == null) {
			this.addresses = new ArrayList<Address>(10);
		}
		this.addresses.add(address);
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Phone> getPhones() {
		return Phones;
	}

	public void setPhones(List<Phone> phones) {
		Phones = phones;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
}
