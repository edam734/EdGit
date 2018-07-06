package com.edgit.server.jsf.services;

import java.util.List;

import com.edgit.server.jpa.GitFile;

public interface RepositoryService {

	void createRoot(String repositoryName);

	void createRepository(String repositoryName, String description);

	List<GitFile> getSubfiles(String name);
}
