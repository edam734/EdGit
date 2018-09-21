package com.edgit.server.jsf;

import java.nio.file.Path;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.edgit.server.jpa.GitFile;
import com.edgit.server.jsf.handlers.PersistenceHandler;

@ApplicationScoped
public class RepositoryServiceImpl implements RepositoryService { 

	private static final String EMPTY_STRING = "";

	@Inject
	private Logger log;

	@EJB
	private PersistenceHandler persistenceHandler;

	public GitFile createRoot(String repositoryName) {
		log.info("O valor de PersistenceHandler eh {}", persistenceHandler);
		return persistenceHandler.create(null, repositoryName, EMPTY_STRING, false);
	}

	public void createRepository(GitFile parent, String repositoryName, String description) {
		persistenceHandler.create(parent, repositoryName, description, false);
	}

	public List<GitFile> getSubfiles(Long parentId) {
		return persistenceHandler.getSubfiles(parentId);
	}

	public boolean createEntry(Path path, String filename, GitFile repo, String description) {
		return persistenceHandler.createEntry(path, filename, repo, description);
	}

}
