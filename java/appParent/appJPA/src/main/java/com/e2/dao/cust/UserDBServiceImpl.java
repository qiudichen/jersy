package com.e2.dao.cust;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.e2.dao.DBException;
import com.e2.dao.GenericDao;
import com.e2.domain.cust.User;

@Transactional(rollbackFor=DBException.class)
@Repository("userDBServiceImpl")
public class UserDBServiceImpl implements UserDBService {
	
	@Autowired
	@Qualifier("userDAO")	
	private GenericDao<User, String> userDAO;
	
	public UserDBServiceImpl() {
		super();
	}

	@Override
	public List<User> getAllUsers() {
		return userDAO.findAll();
	}

	@Override
	public User addUser(User user) throws DBException {
		try {
			userDAO.persist(user);
			return user;
		} catch(Exception e) {
			throw new DBException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteUser(User user) throws DBException {
		try {
			userDAO.remove(user);
		} catch(Exception e) {
			throw new DBException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteUser(String userOid) throws DBException {
	
		try {
			userDAO.remove(userOid);
		} catch(Exception e) {
			throw new DBException(e.getMessage(), e);
		}		
	}
}
