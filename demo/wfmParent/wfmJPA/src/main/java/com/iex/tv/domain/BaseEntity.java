package com.iex.tv.domain;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	public BaseEntity() {

	}
}
