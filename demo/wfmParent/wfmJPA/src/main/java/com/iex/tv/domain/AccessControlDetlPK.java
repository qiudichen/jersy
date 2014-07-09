package com.iex.tv.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class AccessControlDetlPK implements Serializable {

	@Column(name="C_ACL")
	private String acl;
	
	@Column(name="C_ACCESSOR")
	private String accessor;

	public AccessControlDetlPK() {
		super();
	}

	public AccessControlDetlPK(String acl, String accessor) {
		super();
		this.acl = acl;
		this.accessor = accessor;
	}

	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acl == null) ? 0 : acl.hashCode());
		result = prime * result
				+ ((accessor == null) ? 0 : accessor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccessControlDetlPK other = (AccessControlDetlPK) obj;
		if (acl == null) {
			if (other.acl != null)
				return false;
		} else if (!acl.equals(other.acl))
			return false;
		if (accessor == null) {
			if (other.accessor != null)
				return false;
		} else if (!accessor.equals(other.accessor))
			return false;
		return true;
	}
}
