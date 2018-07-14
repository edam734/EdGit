package com.edgit.server.jsf.services;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.edgit.server.jpa.GitFile;

@ApplicationScoped
public class RepositoryServiceImpl implements RepositoryService {

	private static final String EMPTY_STRING = "";

	PersistenceHandler persistenceHandler;

	public RepositoryServiceImpl() {
		persistenceHandler = new PersistenceHandler();
	}

	public GitFile createRoot(String repositoryName) {
		return persistenceHandler.create(null, repositoryName, EMPTY_STRING);
	}

	public void createRepository(GitFile parent, String repositoryName, String description) {
		persistenceHandler.create(parent, repositoryName, description);
	}

	public List<GitFile> getSubfiles(String name) {
		return persistenceHandler.getSubfiles(name);
	}

}
