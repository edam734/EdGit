package com.edgit.server.jsf;

import com.edgit.server.domain.User;

public interface UserService {

	public User getUser(String username);

	public void saveUser(User user);
}
