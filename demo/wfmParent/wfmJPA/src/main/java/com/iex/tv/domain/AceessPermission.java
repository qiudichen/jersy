package com.iex.tv.domain;

import static javax.persistence.CascadeType.ALL;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="R_ACCESSOR")
public class AceessPermission extends BaseEntity {
	@Id 
	@Column(name="C_OID")
	private String oid;
	
	//Option
	@OneToMany(cascade= ALL, fetch = FetchType.LAZY, orphanRemoval=true, mappedBy="permission")
	private List<AccessControlDetl> accessControlDetls;

	public AceessPermission() {
		super();
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public List<AccessControlDetl> getAccessControlDetls() {
		return accessControlDetls;
	}

	public void setAccessControlDetls(List<AccessControlDetl> accessControlDetls) {
		this.accessControlDetls = accessControlDetls;
	}
}
