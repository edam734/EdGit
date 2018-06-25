package com.edgit.server.jsf.services;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;

	private User currentUser;

	@Inject
	private UserServiceImpl userService;

	public String signIn(String username, String password) {
		User user = userService.getUser(username);
		if (user == null || !password.equals(user.getPassword())) {
			return ""; // stay in the same page
		}
		currentUser = user;
		return "homepage"; // go to home page
	}
	
	public boolean isSignedIn() {
		return currentUser != null;
	}
	

	public String signOut() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "welcome?faces-redirect=true";
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String saveUser(User user) {
		userService.saveUser(user);
		currentUser = user;
		return "homepage"; // Home Page
	}
}
