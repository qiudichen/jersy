package com.iex.tv.domain.customer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.iex.tv.domain.BaseEntity;

@SuppressWarnings("serial")
@MappedSuperclass
public class CreateDateEntity extends BaseEntity {
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "C_CREATE_DATE", updatable = false, insertable = false, nullable=false)
	protected Date createDate;

	public CreateDateEntity() {

	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
