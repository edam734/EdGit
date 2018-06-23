package com.edgit.server.jsf.services;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class UserDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private User user;

	public void onload() {
		user = new User();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String submit() {
		// TO DO
		System.out.println("Salvou o utilizador numa BD.");
		return "";
	}
}
