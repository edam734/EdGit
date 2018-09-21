package com.edgit.server.jsf.handlers;

import java.nio.file.Path;
import java.util.List;

import javax.persistence.EntityManager;

import com.edgit.server.jpa.GitFile;

public interface PersistenceHandler {
	
	EntityManager getEntityManager();
	
	GitFile create(GitFile parent, String filename, String description, boolean isFile);
	
	Long getCurrentFolderId(Long parentId, String filename);
	
	List<GitFile> getSubfiles(Long parentId);
	
	boolean createEntry(Path path, String filename, GitFile repo, String description);

}
