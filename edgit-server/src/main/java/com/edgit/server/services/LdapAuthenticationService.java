package com.edgit.server.services;

import javax.naming.NamingException;

import com.edgit.server.domain.User;
import com.edgit.server.security.authentication.LdapServer;

public class LdapAuthenticationService implements AuthenticationService {

	private LdapServer ldap;

	public LdapAuthenticationService() {
		ldap = new LdapServer();
	}

	@Override
	public User authenticate(String identification, String password) {
		return ldap.authenticate(identification, password);
	}

	@Override
	public void save(User user) throws NamingException {
		ldap.getPartitionManager().crateEntry(user.getUsername(), user.getPassword(), user.getFirstName(),
				user.getLastName(), user.getEmailAddress(), user.getRootRepository());
	}
}
