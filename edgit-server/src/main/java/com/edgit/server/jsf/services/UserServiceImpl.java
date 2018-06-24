package com.edgit.server.jsf.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserServiceImpl implements UserService {

	private Map<String, User> users = new ConcurrentHashMap<>();

	@Override
	public User getUser(String username) {
		return users.get(username);
	}

	@Override
	public void saveUser(User user) {
		users.put(user.getUsername(), user);
	}

}
