package com.edgit.server.jsf;

import java.io.Serializable;

import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;

import com.edgit.server.domain.User;

@Named
@SessionScoped
public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	private User currentUser;

	@Inject
	private UserServiceImpl userService;

	public String signIn(String identification, String password) {
		User user = null;
		if ((user = userService.authenticateUser(identification, password)) != null) {
			currentUser = user;
			// Redirect is just to URL reflect the current page
			return "homepage?faces-redirect=true"; // go to home page

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
			// I now use the redirect for the URL to reflect the current page
			// and not the previous page
			return "homepage?faces-redirect=true"; // Home Page
		} catch (NamingException e) {
			// TODO execute rollback
			log.error("Couldn't save user {}", user.toString(), e);
		}
		return "";
	}
}