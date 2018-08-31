package com.edgit.server.jsf;

import java.nio.file.Path;
import java.util.List;

import com.edgit.server.jpa.GitFile;

public interface RepositoryService {

	GitFile createRoot(String repositoryName);

	void createRepository(GitFile parent, String repositoryName, String description);

	List<GitFile> getSubfiles(Long parentId);

	boolean createEntry(Path path, String filename, GitFile repo, String description);
}
