package com.edgit.filesystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipOutputStream;

//import javax.ejb.Stateless;

import com.edgit.filesystem.BinamedFile;
import com.edgit.domain.User;
import com.edgit.filesystem.EdGitRepositoryUtils;

//@Stateless
public class FileManager {

	private String rootLocation;
	private User currentUser;

	public FileManager(String rootLocation, User username) {
		super();
		this.rootLocation = rootLocation;
		this.currentUser = username;
	}

	public String getRootLocation() {
		return rootLocation;
	}



	public User getCurrentUser() {
		return currentUser;
	}

	public File getUserRepository() {
		return new File(String.format("%s%s%s", this.getRootLocation(), File.separator, this.getCurrentUser()));
//		return new File(Paths.get(ServerRepository.getRepositoryRootLocation(), currentUser.getUsername());
	}

	public long copyFile(InputStream in, Path target, CopyOption... options) throws IOException {
		EdGitRepositoryUtils.makeDirectory(new File(target.getParent().toString()));
		return Files.copy(in, target, options);
	}

	public void push(InputStream in, Path target, CopyOption... options) throws IOException {
		EdGitRepositoryUtils.receiveFile(in, target, "updateIndex (TODO)", this.currentUser.getUsername(), options);
	}

	public Path getDirectory(Path path) {
		return EdGitRepositoryUtils.getDirectory(path);
	}

	public String getFilename(Path path) {
		return EdGitRepositoryUtils.getFilename(path);
	}

	public List<BinamedFile> getAllSubfiles(User currentUser, String filename) {
		List<BinamedFile> subfiles = EdGitRepositoryUtils
				.getAllSubfiles(new File(addUserRepositoryOnFilename(filename)));
		for (BinamedFile bf : subfiles) {
			bf.setPath(removeUserRepositoryOfFilename(bf.getPath().toString()));
		}
		return subfiles;
	}

	private String addUserRepositoryOnFilename(String filename) {
		return String.format("%s%s%s", getUserRepository(), File.separator, filename);
	}

	private Path removeUserRepositoryOfFilename(String complete) {
		int length = (String.format("%s%s", getUserRepository(), File.separator)).length();
		return Paths.get(complete.substring(length));
	}

	public void pull(ZipOutputStream zos, BinamedFile binamedFile) throws IOException {
		EdGitRepositoryUtils.sendFile(zos, binamedFile);
	}
}
