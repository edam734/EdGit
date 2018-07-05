package com.edgit.server.jsf.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
		repositoryService.createRoot(user.getUsername());
		users.put(user.getUsername(), user);
	}

}
