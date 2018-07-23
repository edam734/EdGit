package com.edgit.server.jsf.services;

import java.io.File;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.edgit.server.jsf.ServerRepositoryHandler;

@Stateless
public class FilePathHandler {

	@Inject
	private UserManager userManager;

	public File getUserRepository() {
		return new File(ServerRepositoryHandler.REPOSITORY_ROOT_LOCATION + File.separator
				+ userManager.getCurrentUser().getUsername());
	}
}
