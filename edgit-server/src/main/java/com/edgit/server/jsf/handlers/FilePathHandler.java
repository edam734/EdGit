package com.edgit.server.jsf.handlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.ejb.Stateless;

import com.edgit.server.domain.User;
import com.edgit.server.filesystem.RepositorySystemManager;

@Stateless
public class FilePathHandler {

	public File getUserRepository(User currentUser) {
		return new File(ServerRepositoryHandler.REPOSITORY_ROOT_LOCATION + File.separator
				+ currentUser.getUsername());
	}

	public long copyFile(InputStream in, Path target, CopyOption... options) throws IOException {
		RepositorySystemManager.makeDirectory(new File(target.getParent().toString()));
		return Files.copy(in, target, options);
	} 

	public void uploadFile(User currentUser, InputStream in, Path target, CopyOption... options) throws IOException {
		RepositorySystemManager.uploadFile(in, target, "updateIndex (TODO)", currentUser.getUsername(), options);
	}
	
	public Path getDirectory(Path path) {
		return RepositorySystemManager.getDirectory(path);
	}

	public String getFilename(Path path) {
		return RepositorySystemManager.getFilename(path);
	}
}