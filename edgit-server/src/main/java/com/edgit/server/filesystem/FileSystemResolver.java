package com.edgit.server.filesystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Path;

public class FileSystemResolver {

	public static void makeDirectory(File file) {
		FileSystemUtils.makeDirectory(file);
	}

	public static void uploadFile(InputStream in, Path target, String username, CopyOption... options)
			throws IOException {
		FileSystemUtils.uploadFile(in, target, "updateIndex (TODO)", username, options);
	}
}