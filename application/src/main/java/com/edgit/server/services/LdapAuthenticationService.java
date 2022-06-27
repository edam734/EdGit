package com.edgit.server.services;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.naming.NamingException;

//import com.edgit.server.domain.User;
import com.edgit.domain.User;
import com.edgit.server.jsf.util.Authentication;
import com.edgit.server.security.authentication.LdapServer;

@Stateful
@Authentication(AuthenticationType.LDAP)
public class LdapAuthenticationService implements Serializable, AuthenticationService {

	private static final long serialVersionUID = 1l;
	
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
