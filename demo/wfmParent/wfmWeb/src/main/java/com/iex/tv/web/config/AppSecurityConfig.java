package com.iex.tv.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	private enum Role {
		USER, ADMIN, SUPERADMIN;
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		addUser(auth, "tom", "123456", Role.USER);
		addUser(auth, "bill", "123456", Role.ADMIN);
		addUser(auth, "james", "123456", Role.SUPERADMIN);
	}

	private void addUser(AuthenticationManagerBuilder auth, String user, String password, Role role) throws Exception {
		auth.inMemoryAuthentication().withUser(user).password(password).roles(role.toString());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/protected/**")
				.access("hasRole('ROLE_ADMIN')")
				.antMatchers("/confidential/**")
				.access("hasRole('ROLE_SUPERADMIN')").and().formLogin();
	}
}
