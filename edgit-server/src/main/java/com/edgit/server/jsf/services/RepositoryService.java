package com.edgit.server.jsf.services;

import java.util.List;

import com.edgit.server.jpa.GitFile;

public interface RepositoryService {

	List<Repository> getRepositoryByUsername(String username);

	List<GitFile> getFilesByParent(GitFile folder);

	List<GitFile> getFilesByParentName(String name);
}
