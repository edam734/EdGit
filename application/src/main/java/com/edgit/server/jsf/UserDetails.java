package com.edgit.server.jsf;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

//import com.edgit.server.domain.User;
import com.edgit.domain.User;

@Named
@ViewScoped
public class UserDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserManager userManager;

	private User user;

	// private String submit;

	public void onload() {
		System.out.println("Detalhes do utilizador!!");
		if (userManager.getCurrentUser() != null) {
			user = userManager.getCurrentUser();
		} else {
			user = new User();
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String submit() {
		System.out.println("Salvou o utilizador numa BD.");
		return userManager.saveUser(user);
	}
}
