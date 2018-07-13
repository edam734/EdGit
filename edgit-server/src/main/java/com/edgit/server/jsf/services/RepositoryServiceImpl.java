package com.edgit.server.jsf.services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.edgit.server.jpa.GitFile;

@ApplicationScoped
public class RepositoryServiceImpl implements RepositoryService {

	private static final String EMPTY_STRING = "";

	PersistenceHandler persistenceHandler;

	private GitFile root;

	public RepositoryServiceImpl() {
		root = new GitFile();
		persistenceHandler = new PersistenceHandler();
	}

	public GitFile getRoot() {
		return root;
	}

	public void createRoot(String repositoryName) {
		persistenceHandler.create(null, getRoot(), repositoryName, EMPTY_STRING);
	}

	public void createRepository(String repositoryName, String description) {
		GitFile repository = new GitFile();
		persistenceHandler.create(getRoot(), repository, repositoryName, description);
	}

	public List<GitFile> getSubfiles(String name) {
		return persistenceHandler.getSubfiles(name);
	}
}
