package com.iex.tv.domain.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.iex.tv.domain.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="R_PHONE")

@NamedQueries({
    @NamedQuery(name = Phone.NamedQuery.QUERY_FIND_BY_NAME, query="SELECT p FROM Phone p, Agent a "
    		+ "WHERE p.agentId = a.id AND a.lastName like :lastName")
}) 
public class Phone extends BaseEntity {
	public interface NamedQuery {
		public static final String QUERY_FIND_BY_NAME = "Phone.findByOwnerName";
	}
	
	@Id 
	@Column(name="C_ID")
	@SequenceGenerator(name="seqPhoneId", sequenceName="SEQ_PHONE_ID", allocationSize = 5, initialValue = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seqPhoneId")
	private long id;
	
	@Column(name="C_PHONE", nullable = false, length = 40)
	private String phone;
	
	@Column(name="C_AGT", nullable = false, unique=true)
	private long agentId;
}
