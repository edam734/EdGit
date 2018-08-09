package com.edgit.server.security.authentication;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class Ldap {

	// the suffix (base DN) associated with the partition
	public static final String PARTITION_SUFIX = "ou=edgit,o=ed";

	private DirContext context;

	private LdapPartitionManager partitionManager;

	public Ldap() {
		try {
			this.context = getContext();
		} catch (NamingException e) {
			// context could not be initialized
			e.printStackTrace();
		}
		partitionManager = new LdapPartitionManager(context);
	}

	public LdapPartitionManager getPartitionManager() {
		return partitionManager;
	}

	public DirContext getContext() throws NamingException {
		if (context == null) {
			Properties properties = new Properties();
			properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			properties.put(Context.PROVIDER_URL, "ldap://localhost:10389/");
			properties.put(Context.SECURITY_AUTHENTICATION, "simple");
			// properties.put(Context.SECURITY_PRINCIPAL,
			// "uid=admin,ou=system");
			// properties.put(Context.SECURITY_CREDENTIALS, "secret");

			// Bind anonymously
			return new InitialDirContext(properties);
		}

		return context;
	}

	public boolean authenticate(String uid, String password) {
		String filter = "(&(objectClass=inetOrgPerson)(uid={0}))";

		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(new String[0]);
		controls.setReturningObjFlag(true);
		try {
			NamingEnumeration<SearchResult> enm = context.search(PARTITION_SUFIX, filter, new String[] { uid },
					controls);

			String dn = null;

			if (enm.hasMore()) {
				SearchResult result = enm.next();
				dn = result.getNameInNamespace();
			}

			if (dn == null || enm.hasMore()) {
				// uid not found or not unique
				throw new NamingException("Authentication failed");
			}
			enm.close();

			// Bind with found DN and given password
			@SuppressWarnings("unchecked")
			Hashtable<String, String> environment = (Hashtable<String, String>) context.getEnvironment().clone();
			environment.put(Context.SECURITY_PRINCIPAL, dn);
			environment.put(Context.SECURITY_CREDENTIALS, password);
			new InitialDirContext(environment).close();

			// Perform a lookup in order to force a bind operation with JNDI
			context.lookup(dn);

			return true;

		} catch (NamingException e) {
			return false;
		}
	}
}
