package com.e2.web.auth.service;

import com.e2.web.auth.AuthService;
import com.e2.web.auth.AuthSession;

public class AuthServiceRestImpl implements AuthService {

	@Override
	public boolean valid(AuthSession authSession) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void logout(AuthSession authSession) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AuthSession login(String userId, String password, String tenantId) {
		// TODO Auto-generated method stub
		return null;
	}

}
