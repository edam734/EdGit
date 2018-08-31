package com.edgit.server.jsf;

import java.nio.file.Path;
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

	public List<GitFile> search(Long parentId) {
		return searchRepositories(parentId);
	}

	private List<GitFile> searchRepositories(Long parentId) {
		List<GitFile> repositories = repositoryService.getSubfiles(parentId);
		return repositories == null ? new ArrayList<GitFile>() : repositories;
	}

	public void createRepository(String repositoryName, String description) {
		GitFile userRoot = userManager.getCurrentUser().getRootRepository();
		repositoryService.createRepository(userRoot, repositoryName, description);
	}

	public boolean createEntry(Path path, String filename, String description) {
		GitFile repo = userManager.getCurrentUser().getRootRepository();
		return repositoryService.createEntry(path, filename, repo, description);
	}
}
