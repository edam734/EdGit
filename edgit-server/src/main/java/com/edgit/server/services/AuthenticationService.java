package com.edgit.server.services;

import javax.naming.NamingException;
//import com.edgit.server.domain.User;
import com.edgit.domain.User;

public interface AuthenticationService {

	User authenticate(String identification, String password);

	void save(User user) throws NamingException;
}
