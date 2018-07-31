package com.edgit.commons.network;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BinamedFile {

	private File file;
	private Path rawPath;
	private Path versionedPath;

	public BinamedFile(File file, Path pathToFile) {
		this.file = file;
		this.rawPath = null;
		this.versionedPath = Paths.get(pathToFile.toString(), file.getName());
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Path getRawPath() {
		return rawPath;
	}

	public void setRawPath(Path rawPath) {
		this.rawPath = rawPath;
	}

	public Path getVersionedPath() {
		return versionedPath;
	}
}
