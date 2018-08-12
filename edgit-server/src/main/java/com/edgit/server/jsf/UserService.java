package com.edgit.server.jsf;

import javax.naming.NamingException;

import com.edgit.server.domain.User;

public interface UserService {

//	public User getUser(String username);
	
	public User authenticateUser(String identification, String password);

	public void saveUser(User user) throws NamingException;
}
