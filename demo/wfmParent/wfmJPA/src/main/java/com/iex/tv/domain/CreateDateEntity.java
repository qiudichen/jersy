package com.iex.tv.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@SuppressWarnings("serial")
@MappedSuperclass
public class CreateDateEntity extends BaseEntity {
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", updatable = false, insertable = false, nullable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	protected Date createDate;
	
    @Version
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE", updatable = true, insertable = false, nullable=false,  columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date modifiedDate;
    
	public CreateDateEntity() {

	}

	public Date getCreateDate() {
		return createDate;
	}

	@PrePersist
	public void doPrePersist() {
		//TODO
	}	
}
