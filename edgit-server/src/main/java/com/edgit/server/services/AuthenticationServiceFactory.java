package com.edgit.server.services;

public class AuthenticationServiceFactory {

	public static AuthenticationService getAuthenticationService(AuthenticationType type) {
		switch (type) {
		case SIMPLE:
			return null; // TODO
		case LDAP:
			return new LdapAuthenticationService();
		}
		return null;
	}
}
