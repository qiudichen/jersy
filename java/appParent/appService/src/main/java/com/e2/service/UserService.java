package com.e2.service;

import java.util.List;

import com.e2.domain.cust.User;

public interface UserService {
	public List<User> getUsers();
	
	public String addUser(String userName);
}
