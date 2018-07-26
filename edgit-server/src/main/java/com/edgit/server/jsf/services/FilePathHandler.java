package com.edgit.server.jsf.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.edgit.server.filesystem.FileSystemResolver;
import com.edgit.server.jsf.ServerRepositoryHandler;

@Stateless
public class FilePathHandler {

	@Inject
	private UserManager userManager;

	public File getUserRepository() {
		return new File(ServerRepositoryHandler.REPOSITORY_ROOT_LOCATION + File.separator
				+ userManager.getCurrentUser().getUsername());
	}

	public long copyFile(InputStream in, Path target, CopyOption... options) throws IOException {
		FileSystemResolver.makeDirectory(new File(target.getParent().toString()));
		return Files.copy(in, target, options);
	} 

	public void uploadFile(InputStream in, Path target, CopyOption... options) throws IOException {
		FileSystemResolver.uploadFile(in, target, userManager.getCurrentUser().getUsername(), options);
	}
	
	public Path getDirectory(Path path) {
		return FileSystemResolver.getDirectory(path);
	}

	public String getPureFilename(Path path) {
		return FileSystemResolver.getPureFilename(path);
	}
}
