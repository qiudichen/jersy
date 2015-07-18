package com.e2.web.auth;

public interface AuthService {
	
	public boolean valid(AuthSession authSession);

	public void logout(AuthSession authSession);

	public AuthSession login(String userId, String password, String tenantId);
}
