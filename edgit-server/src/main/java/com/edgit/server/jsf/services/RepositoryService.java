package com.edgit.server.jsf.services;

import java.util.List;

public interface RepositoryService {

	List<Repository> getRepositoryByUsername(String username);
}
