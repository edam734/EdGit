package com.edgit.server.jsf.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.edgit.server.jpa.GitFile;

@Stateless
public class RepositoryManager {

	@Inject
	private UserManager userManager;

	@Inject
	private RepositoryServiceImpl repositoryService;

	public List<GitFile> search(String name) {
		return searchRepositories(name);
	}

	private List<GitFile> searchRepositories(String username) {
		List<GitFile> repositories = repositoryService.getSubfiles(username);
		return repositories == null ? new ArrayList<GitFile>() : repositories;
	}

	public void createRepository(String repositoryName, String description) {
		GitFile userRoot = userManager.getCurrentUser().getRootRepository();
		repositoryService.createRepository(userRoot, repositoryName, description);
	}
}
