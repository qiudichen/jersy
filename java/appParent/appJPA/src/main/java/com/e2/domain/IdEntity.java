package com.e2.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.Instant;

@MappedSuperclass
@SuppressWarnings("serial")
public class IdEntity extends BaseEntity {
    @Id
    @GeneratedValue(generator = "ObjectIdGenerator")
    @GenericGenerator(name = "ObjectIdGenerator", strategy = "com.e2.domain.schema.ObjectIdGenerator")
    @Column(name = "c_oid", length = 32, nullable = false, insertable = false, updatable = false)
    private String oid;
    
    @Version
    @Type(type="org.joda.time.contrib.hibernate.PersistentInstant")
    @Column(name = "c_mod", length = 20, nullable = false)
    private Instant version;
    
    public IdEntity() {
    	
    }

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}
}
