package com.edgit.server.jsf;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.edgit.server.domain.User;
import com.edgit.server.filesystem.EdGitRepositoryManager;
import com.edgit.server.jpa.GitFile;
import com.edgit.server.jsf.handlers.ServerRepositoryHandler;

@ApplicationScoped
public class UserServiceImpl implements UserService {

	@Inject
	private RepositoryServiceImpl repositoryService;

	private Map<String, User> users = new ConcurrentHashMap<>();

	@Override
	public User getUser(String username) {
		return users.get(username);
	}

	@Override
	public void saveUser(User user) {
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
		users.put(user.getUsername(), user);
	}
}
