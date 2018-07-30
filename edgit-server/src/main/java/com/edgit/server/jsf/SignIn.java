package com.edgit.server.jsf;

import java.io.File;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class SignIn {

	private String username;
	private String password;
	
	@Inject
	private UserManager userManager;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String submit() {
		return userManager.signIn(getUsername(), getPassword());
	}
}