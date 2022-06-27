package com.edgit.filesystem;


import java.io.File;
import java.nio.file.Path;

public class BinamedFile {

	private File file;
	private Path path;

	public BinamedFile(File file, Path pathToFile) {
		this.file = file;
		this.path = pathToFile;
	}

	public File getFile() {
		return file;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "BinamedFile [file=" + file + ", path=" + path + "]";
	}

}
