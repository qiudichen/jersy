package com.e2.domain.cust;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.e2.domain.IdEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "m_user")
@NamedQueries({
    @NamedQuery(name = User.NamedQuery.QUERY_FIND_ALL, query="SELECT u FROM User u"),
    @NamedQuery(name = User.NamedQuery.QUERY_FIND_BY_NAME, query="SELECT u FROM User u WHERE u.username = :name"),
}) 

@NamedNativeQueries({
	@NamedNativeQuery(name = User.NamedNativeQuery.QUERY_FIND_ALL, query = "SELECT * FROM R_USER", resultClass = User.class),
	@NamedNativeQuery(name = User.NamedNativeQuery.QUERY_FIND_BY_NAME, query = "SELECT * FROM R_USER WHERE c_username = :name", 
		resultClass = User.class),
})
@Cacheable

public class User extends IdEntity {
	
	public interface NamedQuery {
		public static final String QUERY_FIND_ALL = "User.findAll";
		public static final String QUERY_FIND_BY_NAME = "User.findByName";
	}
	
	public interface NamedNativeQuery {
		public static final String QUERY_FIND_ALL = "User.Native.findAll";
		public static final String QUERY_FIND_BY_NAME = "User.Native.findByName";
	}
	
    @Column(name = "c_password", length = 64, nullable = false, insertable = true, updatable = true)
    private String password;
    
    @Column(name = "c_username", length = 256, nullable = false, insertable = true, updatable = true)
    private String username;

	public User() {
		super();
	}

	public User(String password, String username) {
		super();
		this.password = password;
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
