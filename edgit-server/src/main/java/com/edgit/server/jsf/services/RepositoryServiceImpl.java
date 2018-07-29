package com.edgit.server.jsf.services;

import java.nio.file.Path;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.edgit.server.jpa.GitFile;

@ApplicationScoped
public class RepositoryServiceImpl implements RepositoryService {

	private static final String EMPTY_STRING = "";

	private PersistenceHandler persistenceHandler;

	public RepositoryServiceImpl() {
		persistenceHandler = new PersistenceHandler();
	}

	public GitFile createRoot(String repositoryName) {
		return persistenceHandler.create(null, repositoryName, EMPTY_STRING, false);
	}

	public void createRepository(GitFile parent, String repositoryName, String description) {
		persistenceHandler.create(parent, repositoryName, description, false);
	}

	public List<GitFile> getSubfiles(String name) {
		return persistenceHandler.getSubfiles(name);
	}

	public boolean createEntry(Path path, String filename, GitFile repo, String description) {
		return persistenceHandler.createEntry(path, filename, repo, description);
	}
}
