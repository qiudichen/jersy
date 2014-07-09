package com.iex.tv.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="R_ACLDETL")
public class AccessControlDetl extends BaseEntity {
	@EmbeddedId
	private AccessControlDetlPK pk;
	
	@Column(name="C_MODE")
	private Integer level;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name = "C_ACL", insertable = false, updatable = false)
    private AccessControl accessControl;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name = "C_ACCESSOR", insertable = false, updatable = false)
    private AceessPermission permission;

	public AccessControlDetl() {
		super();
	}

	public AccessControlDetlPK getPk() {
		return pk;
	}

	public void setPk(AccessControlDetlPK pk) {
		this.pk = pk;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public AccessControl getAccessControl() {
		return accessControl;
	}

	public void setAccessControl(AccessControl accessControl) {
		this.accessControl = accessControl;
	}

	public AceessPermission getPermission() {
		return permission;
	}

	public void setPermission(AceessPermission permission) {
		this.permission = permission;
	}
	
	
}
