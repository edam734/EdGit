package com.edgit.server.jsf.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class UserRepositories implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserManager userManager;
	@Inject
	private RepositoryServiceImpl repositoryService;

	private List<Repository> repositories = new ArrayList<>();

	@PostConstruct
	private void init() {
		repositories = repositoryService.getRepositoryByUsername(userManager.getCurrentUser().getUsername());
	}

	public List<Repository> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<Repository> repositories) {
		this.repositories = repositories;
	}
}
