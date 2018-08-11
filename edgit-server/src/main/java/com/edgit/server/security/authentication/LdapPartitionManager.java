package com.edgit.server.security.authentication;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;

import com.edgit.server.jpa.GitFile;

public class LdapPartitionManager {

	private DirContext context;

	public LdapPartitionManager(DirContext context) {
		this.context = context;
	}

	public void crateEntry(String username, String password, String commonName, String surname, String emailAddress,
			GitFile rootRepository) throws NamingException {

		Attributes attributes = new BasicAttributes();

		Attribute attribute = new BasicAttribute("objectClass");
		attribute.add("edgitClient");
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

		Attribute email = new BasicAttribute("mail");
		email.add(emailAddress);
		attributes.put(email);

		// A string representation of a GitFile (folder must be null)

		String id = String.valueOf(rootRepository.getFileId());
		String filename = rootRepository.getFilename();
		String description = rootRepository.getDescription();
		String isFile = String.valueOf(rootRepository.getIsFile());

		String[] idKV = { "id:", id };
		String[] folderKV = { "folder:", "null" };
		String[] filenameKV = { "filename:", filename };
		String[] descriptionKV = { "description:", description };
		String[] isFileKV = { "isFile:", isFile };

		String rootRepositoryStr = String.format("%s;%s;%s;%s;%s",
				flatten(idKV),
				flatten(folderKV),
				flatten(filenameKV),
				flatten(descriptionKV),
				flatten(isFileKV));

		Attribute repository = new BasicAttribute("rootRepository");
		repository.add(rootRepositoryStr);
		attributes.put(repository);

		String objectName = "uid=" + username + "," + LdapServer.USERS;
		context.createSubcontext(objectName, attributes);
	}

	private String flatten(String[] arr) {
		String result = "";
		for (int i = 0; i < arr.length; i++) {
			result = result.concat(arr[i]);
		}
		return result;
	}
}
