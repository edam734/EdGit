package com.edgit.server.jsf.services;

public interface UserService {

	public User getUser(String username);

	public void saveUser(User user);
}
