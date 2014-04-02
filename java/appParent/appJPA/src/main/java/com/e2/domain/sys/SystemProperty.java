package com.e2.domain.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.e2.domain.IdEntity;

@Entity
@Table(name = "R_SYSPROPS", 
	uniqueConstraints = @UniqueConstraint(columnNames = {"C_SUBSYSTEM", "C_NAME" }))

@NamedQueries({
    @NamedQuery(name = SystemProperty.NamedQuery.QUERY_FIND_BY_SUBTYPE, query="SELECT s FROM SystemProperty s WHERE s.subsystem = :type")
}) 

@SuppressWarnings("serial")
public class SystemProperty extends IdEntity {
	
	public interface NamedQuery {
		public static final String QUERY_FIND_BY_SUBTYPE = "SystemProperty.findBySubType";
	}
	
    @Column(name = "C_SUBSYSTEM", length = 32, nullable = false)
    @Enumerated(EnumType.STRING)
    private SubSystemType subsystem;

    @Column(name = "C_NAME", length = 255, nullable = false)
    private String name;

    @Column(name =  "C_VAL", length = 255, nullable = true)
    private String value;
    
	public SystemProperty() {
		super();
	}
}
