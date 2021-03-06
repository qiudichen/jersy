package com.iex.cloud.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@MappedSuperclass
public class CreateDateEntity extends BaseEntity implements Serializable {
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", updatable = false, insertable = false, nullable=false, 
			columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	protected Date createDate;
	
	public CreateDateEntity() {

	}

	public Date getCreateDate() {
		return createDate;
	}

	@PrePersist
	public void doPrePersist() {
		System.out.println("<---- CreateDateEntity.doPrePersist() --- ");
	}	
}
