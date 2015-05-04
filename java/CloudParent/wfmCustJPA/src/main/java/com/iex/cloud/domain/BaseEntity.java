package com.iex.cloud.domain;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	public BaseEntity() {

	}
	
	@PrePersist
	public void doPrePersist() {
		String name = this.getClass().getSimpleName();
		System.out.println("<---- " + name + ".doPrePersist() --- ");
	}	
}
