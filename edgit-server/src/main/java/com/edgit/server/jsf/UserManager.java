package com.edgit.server.jsf;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import com.edgit.server.domain.User;

@Named
@SessionScoped
public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;

	private User currentUser;

	@Inject
	private UserServiceImpl userService;

	public String signIn(String username, String password) {
		User user = null;
		if ((user = userService.authenticate(username, password)) != null) {
			currentUser = user;
			return "homepage"; // go to home page
			
		} else {
			return ""; // stay in the same page
		}
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
		try {
			userService.saveUser(user);
			currentUser = user;
			return "homepage"; // Home Page
		} catch (NamingException e) {
			// couldn't save user...
			e.printStackTrace();
		}
		return "";
	}
}