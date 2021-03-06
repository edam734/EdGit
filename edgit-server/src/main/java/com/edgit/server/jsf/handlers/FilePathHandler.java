package com.edgit.server.jsf.handlers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.ejb.Stateless;

import com.edgit.commons.network.BinamedFile;
import com.edgit.server.domain.User;
import com.edgit.server.filesystem.EdGitRepositoryManager;

@Stateless
public class FilePathHandler {

	public File getUserRepository(User currentUser) {
		return new File(ServerRepositoryHandler.REPOSITORY_ROOT_LOCATION + File.separator + currentUser.getUsername());
	}

	public long copyFile(InputStream in, Path target, CopyOption... options) throws IOException {
		EdGitRepositoryManager.makeDirectory(new File(target.getParent().toString()));
		return Files.copy(in, target, options);
	}

	public void push(User currentUser, InputStream in, Path target, CopyOption... options) throws IOException {
		EdGitRepositoryManager.receiveFile(in, target, "updateIndex (TODO)", currentUser.getUsername(), options);
	}

	public Path getDirectory(Path path) {
		return EdGitRepositoryManager.getDirectory(path);
	}

	public String getFilename(Path path) {
		return EdGitRepositoryManager.getFilename(path);
	}

	public List<BinamedFile> getAllSubfiles(User currentUser, String filename) {
		List<BinamedFile> subfiles = EdGitRepositoryManager
				.getAllSubfiles(new File(addUserRepositoryOnFilename(currentUser, filename)));
		for (BinamedFile bf : subfiles) {
			bf.setPath(removeUserRepositoryOfFilename(currentUser, bf.getPath().toString()));
		}
		return subfiles;
	}

	private String addUserRepositoryOnFilename(User currentUser, String filename) {
		return getUserRepository(currentUser) + File.separator + filename;
	}

	private Path removeUserRepositoryOfFilename(User currentUser, String complete) {
		int strLength = (getUserRepository(currentUser) + File.separator).length();
		return Paths.get(complete.substring(strLength));
	}

	public void pull(ZipOutputStream zos, BinamedFile binamedFile) throws IOException {
		EdGitRepositoryManager.sendFile(zos, binamedFile);
	}
}
