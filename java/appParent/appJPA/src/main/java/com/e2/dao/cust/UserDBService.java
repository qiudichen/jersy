package com.e2.dao.cust;

import java.util.List;

import com.e2.dao.DBException;
import com.e2.domain.cust.User;


public interface UserDBService {
	
	public List<User> getAllUsers();
	
	public User addUser(User user) throws DBException;
	
	public void deleteUser(User user) throws DBException;
	
	public void deleteUser(String userOid) throws DBException;
	
}
