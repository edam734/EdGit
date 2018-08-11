package com.edgit.server.jsf;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.NamingException;

import com.edgit.server.domain.User;
import com.edgit.server.filesystem.EdGitRepositoryManager;
import com.edgit.server.jpa.GitFile;
import com.edgit.server.jsf.handlers.ServerRepositoryHandler;
import com.edgit.server.security.authentication.LdapPartitionManager;
import com.edgit.server.security.authentication.LdapServer;

@ApplicationScoped
public class UserServiceImpl implements UserService {

	@Inject
	private RepositoryServiceImpl repositoryService;

	// private Map<String, User> users = new ConcurrentHashMap<>();

	private LdapServer ldap;

	@PostConstruct
	private void init() {
		ldap = new LdapServer();
	}

	// @Override
	// public User getUser(String username) {
	// return users.get(username);
	// }

	public User authenticate(String identification, String password) {
		return ldap.authenticate(identification, password);
	}

	@Override
	public void saveUser(User user) throws NamingException {
		// save user
		LdapPartitionManager partitionManager = ldap.getPartitionManager();
		partitionManager.crateEntry(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
				user.getEmailAddress());

		// Creates the root folder that will serve to contain all the projects
		// of this user.
		GitFile root = repositoryService.createRoot(user.getUsername());
		// When the transaction ends, the flush will happen, and the entity
		// outside of the transaction will appear with the generated ID.
		user.setRootRepository(root);
		// Create the remote repository for this user
		EdGitRepositoryManager.makeDirectory(
				new File(ServerRepositoryHandler.REPOSITORY_ROOT_LOCATION + File.separator + user.getUsername()));
		// Saves user in DB
//		users.put(user.getUsername(), user);

	}
}
