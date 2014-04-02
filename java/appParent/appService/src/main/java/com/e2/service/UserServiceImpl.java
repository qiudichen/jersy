package com.e2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e2.dao.DBException;
import com.e2.dao.cust.UserDBService;
import com.e2.dao.system.SystemDBService;
import com.e2.domain.cust.User;

@Service("userServiceImpl")
@Transactional(rollbackFor=DBException.class)
public class UserServiceImpl implements UserService {
	@Autowired
	@Qualifier("userDBServiceImpl")	
    private UserDBService userDBService;

	@Autowired
	@Qualifier("systemDBServiceImpl")	
    private SystemDBService systemDBService;
	
	@Override
	public List<User> getUsers() {
		List<User> result = userDBService.getAllUsers();
		return result;
	}

	@Override
	@Transactional
	public String addUser(String userName) {
		return userName;
	}

}
