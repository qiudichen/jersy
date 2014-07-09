package com.iex.tv.domain;

import static javax.persistence.CascadeType.ALL;

import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="R_ACL")
public class AccessControl extends BaseEntity {
	@Id 
	@Column(name="C_OID")
	private String oid;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="C_PARENT")
    private AccessControl parent;
	
	@Column(name="C_PARENT", insertable=false, updatable=false)
	private String parentOid;
	
    @OneToMany(mappedBy="parent", fetch = FetchType.EAGER)
    private Collection<AccessControl> children;

	//Option
	@OneToMany(cascade= ALL, fetch = FetchType.EAGER, orphanRemoval=true, mappedBy="accessControl")
	private List<AccessControlDetl> accessControlDetls;


	public AccessControl() {
		super();
	}


	public String getOid() {
		return oid;
	}


	public void setOid(String oid) {
		this.oid = oid;
	}


	public AccessControl getParent() {
		return parent;
	}


	public void setParent(AccessControl parent) {
		this.parent = parent;
	}


	public Collection<AccessControl> getChildren() {
		return children;
	}


	public void setChildren(Collection<AccessControl> children) {
		this.children = children;
	}


	public List<AccessControlDetl> getAccessControlDetls() {
		return accessControlDetls;
	}


	public void setAccessControlDetls(List<AccessControlDetl> accessControlDetls) {
		this.accessControlDetls = accessControlDetls;
	}	
	
	
}
