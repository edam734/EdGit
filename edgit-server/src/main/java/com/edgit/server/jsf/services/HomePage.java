package com.edgit.server.jsf.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.edgit.server.jpa.GitFile;

@Named
@ViewScoped
public class HomePage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserManager userManager;

	@Inject
	private RepositoryManager repositoryManager;

	private List<GitFile> repositories = new ArrayList<>();

	@PostConstruct
	private void loadUserRepositories() {
		repositories = repositoryManager.search(userManager.getCurrentUser().getUsername());
	}

	public List<GitFile> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<GitFile> repositories) {
		this.repositories = repositories;
	}
}
