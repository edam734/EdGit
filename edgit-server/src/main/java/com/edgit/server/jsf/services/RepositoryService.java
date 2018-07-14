package com.edgit.server.jsf.services;

import java.util.List;

import com.edgit.server.jpa.GitFile;

public interface RepositoryService {

	GitFile createRoot(String repositoryName);

	void createRepository(GitFile parent, String repositoryName, String description);

	List<GitFile> getSubfiles(String name); 
}
