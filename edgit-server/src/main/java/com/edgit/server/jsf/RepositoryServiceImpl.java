package com.edgit.server.jsf;

import java.nio.file.Path;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.edgit.server.jpa.GitFile;
import com.edgit.server.jsf.handlers.PersistenceService;

@ApplicationScoped
public class RepositoryServiceImpl implements RepositoryService { 

	private static final String EMPTY_STRING = "";

	@Inject
	private Logger log;

	@EJB
	private PersistenceService persistenceService;

	public GitFile createRoot(String repositoryName) {
		log.info("O valor de PersistenceHandler eh {}", persistenceService);
		return persistenceService.create(null, repositoryName, EMPTY_STRING, false);
	}

	public void createRepository(GitFile parent, String repositoryName, String description) {
		persistenceService.create(parent, repositoryName, description, false);
	}

	public List<GitFile> getSubfiles(Long parentId) {
		return persistenceService.getSubfiles(parentId);
	}

	public boolean createEntry(Path path, String filename, GitFile repo, String description) {
		return persistenceService.createEntry(path, filename, repo, description);
	}

}