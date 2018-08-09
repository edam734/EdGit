package com.edgit.server.security.authentication;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;

public class LdapPartitionManager {

	private DirContext context;

	public LdapPartitionManager(DirContext context) {
		this.context = context;
	}

	public void crateEntry(String username, String password, String commonName, String surname) throws NamingException {

		Attributes attributes = new BasicAttributes();

		Attribute attribute = new BasicAttribute("objectClass");
		attribute.add("inetOrgPerson");
		attributes.put(attribute);

		Attribute uid = new BasicAttribute("uid");
		uid.add(username);
		attributes.put(uid);

		Attribute userPassword = new BasicAttribute("userPassword");
		userPassword.add(password); // in plain text for now...
		attributes.put(userPassword);

		Attribute cn = new BasicAttribute("cn");
		cn.add(commonName);
		attributes.put(cn);

		Attribute sn = new BasicAttribute("sn");
		sn.add(surname);
		attributes.put(sn);

		// context.createSubcontext(SUBCONTEXT.concat(String.valueOf(ServerRepositoryHandler.numberOfUsers)),
		// attributes);
		context.createSubcontext("uid=" + username + "," + Ldap.PARTITION_SUFIX, attributes);
	}
}
