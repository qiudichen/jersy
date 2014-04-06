package com.iex.tv.domain.customer;

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


@SuppressWarnings("serial")
@Entity
@Table(name="R_AGT")

@NamedQueries({
    @NamedQuery(name = Agent.NamedQuery.QUERY_FIND_BY_SUBTYPE, query="SELECT a FROM Agent a WHERE a.startDate > :startDate "), 
    @NamedQuery(name = Agent.NamedQuery.QUERY_GET_SECOND, query="SELECT s FROM SystemProperty s WHERE (SELECT count(s1) from SystemProperty s1 where s1.version < s.version) = 1"), 
    @NamedQuery(name = Agent.NamedQuery.QUERY_GET_COUNT, query="SELECT count(s) FROM SystemProperty s WHERE s.subsystem = :type")
}) 

public class Agent extends CreateDateEntity {
	public interface NamedQuery {
		public static final String QUERY_FIND_BY_SUBTYPE = "Agent.findBySubType";
		
		public static final String QUERY_GET_COUNT = "Agent.getCount";
		
		public static final String QUERY_GET_SECOND = "Agent.getSecondEarly";
	}
	
	
	@Id 
	@Column(name="C_ID")
	@SequenceGenerator(name="seqAgentId", sequenceName="SEQ_AGT_ID", allocationSize = 5, initialValue = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seqAgentId")
	private long id;
	
	@Column(name="C_FIRSTNAME", nullable = false, length = 40)
	private String firstName;
	
	@Column(name="C_LASTNAME", nullable = false, length = 40)
	private String lastName;
	
    @Temporal(TemporalType.DATE)    
	@Column(name="C_START_DATE", nullable = false)	
	private Date startDate;
    
	@OneToMany(cascade={ PERSIST, MERGE, REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name="C_AGT")
	private List<Address> addresses;

	@OneToMany(cascade={ PERSIST, MERGE, REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name="C_AGT")
	private List<Phone> Phones;
	
	@ManyToMany(cascade ={CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
	@JoinTable(name="R_AGT_SKILL", joinColumns={@JoinColumn(name="C_AGT", referencedColumnName="C_ID")},
				inverseJoinColumns=@JoinColumn(name="C_SKILL", referencedColumnName="C_ID"))
	private List<Skill> skills; 
	
	public Agent() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
}
