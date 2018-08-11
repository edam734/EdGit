package com.edgit.server.jsf;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class SignIn {

	private String identification;
	private String password;

	@Inject
	private UserManager userManager;

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String submit() {
		return userManager.signIn(getIdentification(), getPassword());
	}
}