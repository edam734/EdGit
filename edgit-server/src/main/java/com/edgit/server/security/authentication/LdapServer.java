package com.edgit.server.security.authentication;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import com.edgit.server.domain.User;

public class LdapServer {

	// the suffix (base DN) associated with the partition
	public static final String PARTITION_SUFIX = "o=ed";

	public static final String USERS = String.format("ou=edgit,%s", PARTITION_SUFIX);

	private DirContext context;

	private LdapPartitionManager partitionManager;

	public LdapServer() {
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
			properties.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
			properties.put(Context.SECURITY_CREDENTIALS, "secret");

			return new InitialDirContext(properties);
		}
		return context;
	}

	public User authenticate(String uid, String email, String password) {
		try {
			String dn = findDistinctNameOfAccount(context, USERS, uid, email);

			// throws NamingException if not verified
			verifyPassword(context, dn, password);

			User user = retrieveUser(context, dn, "");
			return user;

		} catch (NamingException e) {
			return null;
		}
	}

	private String findDistinctNameOfAccount(DirContext ctx, String ldapSearchBase, String uid, String email)
			throws NamingException {
		String filter = "(&(objectClass=inetOrgPerson)(|(uid={0})(mail={1})))";

		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(new String[0]);
		controls.setReturningObjFlag(true);

		NamingEnumeration<SearchResult> enm = ctx.search(ldapSearchBase, filter, new String[] { uid, email }, controls);
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

		return dn;
	}

	/**
	 * A cloning of the context environment is done so the original environment
	 * it's not modified.
	 * <p>
	 * Try to initialize the cloned context environment. If the user credentials
	 * are different from the originals, it throws an exception.
	 * 
	 * @param ctx
	 *            The original context
	 * @param dn
	 *            The distinct name to user
	 * @param password
	 *            The password to be checked
	 * @throws NamingException
	 *             if a naming exception is encountered
	 */
	private void verifyPassword(DirContext ctx, String dn, String password) throws NamingException {
		// Bind with a DN and given password
		@SuppressWarnings("unchecked")
		Hashtable<String, String> environment = (Hashtable<String, String>) ctx.getEnvironment().clone();
		environment.put(Context.SECURITY_PRINCIPAL, dn);
		environment.put(Context.SECURITY_CREDENTIALS, password);
		new InitialDirContext(environment).close();
	}

	private User retrieveUser(DirContext cxt, String dn, String subcontext) throws NamingException {
		User user = new User();

		// Perform a lookup of the object itself using JNDI
		LdapContext obj = (LdapContext) cxt.lookup(dn);
		Attributes attributes = obj.getAttributes(subcontext);
		Attribute username = attributes.get("uid");
		Attribute firstName = attributes.get("cn");
		Attribute lastName = attributes.get("sn");
		user.setUsername(username.get().toString());
		user.setFirstName(firstName.get().toString());
		user.setLastName(lastName.get().toString());

		return user;
	}
}
