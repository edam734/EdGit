package com.edgit.server.filesystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import com.edgit.server.filesystem.RepositorySystemManager.BooleanResult.BooleanMessage;

/**
 * Utilities for manipulating files in the remote repository.
 * 
 * @author Eduardo Amorim
 *
 */
public class RepositorySystemManager {

	final static Charset ENCODING = StandardCharsets.UTF_8;

	/**
	 * Place a new file in the appropriate location on the remote server, write
	 * to the ".index.txt" file who uploaded this version, and update the file's
	 * name version
	 * 
	 * @param in
	 *            The input stream that carries the data
	 * @param target
	 *            The path to be modified
	 * @param updateIndex
	 * @param username
	 *            who uploaded this file
	 * @param options
	 *            Some copy options
	 * @return true if was successful
	 * @throws IOException
	 */
	public static boolean uploadFile(final InputStream in, Path target, final String updateIndex, final String username,
			CopyOption... options) throws IOException {

		if (target.toFile().isDirectory()) {
			return makeDirectory(target.toFile()).toBoolean();
		}

		int version = 0;

		UpdateFilePathResolver updateFilePathResolver = new RepositorySystemManager().new UpdateFilePathResolver(target);
		final Path directory = updateFilePathResolver.getDirectory();
		final Path indexFile = updateFilePathResolver.getIndexFile();

		BooleanResult b = makeDirectory(directory.toFile());
		if (b.getMessage().equals(BooleanMessage.NOT_CREATED)) {
			return false;
		} else if (b.getMessage().equals(BooleanMessage.ALREADY_EXISTS)) {
			version = getMostRecentVersion(indexFile);
		}

		int newVersion = ++version;
		writeAnEntry(indexFile.toFile(), Integer.toString(newVersion), " : ", username);

		target = updateFilePathResolver.getVersionedFilename(newVersion);

		return createNewVersion(in, target, options);
	}

	private static boolean createNewVersion(final InputStream in, final Path target, CopyOption... options)
			throws IOException {
		return (Files.copy(in, target, options) > 0);
	}

	private static void writeAnEntry(File file, String... strings) {
		try (FileWriter fw = new FileWriter(file.getAbsolutePath(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			for (String s : strings) {
				out.print(s);
			}
			out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int getMostRecentVersion(final Path path) {
		try (Scanner fileReader = new Scanner(path)) {
			String line = null;
			while (fileReader.hasNextInt()) {
				line = fileReader.nextLine();
			}
			return Integer.parseInt(line.split(" : ")[0]);
		} catch (IOException e) {
			return -1;
		}
	}

	public static Path getDirectory(Path path) {
		if (path.toFile().isDirectory()) {
			return path;
		}
		return path.getParent();
	}

	public static String getFilename(Path path) {
		return path.getFileName().toString();
	}

	/**
	 * Creates a new directory if it does not already exist.
	 * 
	 * @param file
	 *            A directory
	 * @requires file.isDirectory()
	 * @return A BooleanResult with several possibilities
	 */
	public static BooleanResult makeDirectory(File file) {
		if (!file.exists()) {
			if (file.mkdirs()) {
				return new BooleanResult(BooleanMessage.CREATED);
			} else {
				return new BooleanResult(BooleanMessage.NOT_CREATED);
			}
		}
		return new BooleanResult(BooleanMessage.ALREADY_EXISTS);
	}

	static class BooleanResult {
		public static enum BooleanMessage {
			ALREADY_EXISTS, CREATED, NOT_CREATED;
		}

		private BooleanMessage message;

		public BooleanResult(BooleanMessage message) {
			this.message = message;
		}

		public BooleanMessage getMessage() {
			return message;
		}

		public boolean toBoolean() {
			return BooleanMessage.NOT_CREATED.equals(message) ? false : true;
		}
	}

	/**
	 * Class that will apply file manipulation rules in the remote repository of
	 * the client.
	 * <p>
	 * For each file you create a new folder that will contain an ".index.txt"
	 * file and the file itself with the version number in the name. E.g., the
	 * file in the path client "/folder1/.../folderN/filename.pdf" will be
	 * placed on the server in the following format <br/>
	 * "/rootRepository/repo/folder1/.../folderN/filename - PDF/filename-v
	 * <version_number>.pdf
	 * 
	 * @author Eduardo Amorim
	 *
	 */
	class UpdateFilePathResolver {

		private String extension;
		private String pureFilename;
		private Path directory;
		private Path indexFile;

		public UpdateFilePathResolver(final Path path) {
			resolve(path);
		}

		public Path getDirectory() {
			return directory;
		}

		public Path getIndexFile() {
			return indexFile;
		}

		public Path getVersionedFilename(int newVersion) {
			return filePath(directory, pureFilename, newVersion);
		}

		private void resolve(final Path path) {
			extension = getExtension(path.getFileName().toString());
			pureFilename = removeExtension(path.getFileName().toString());
			Path pathWithoutExtension = Paths.get(removeExtension(path.toString()));
			directory = directoryPath(pathWithoutExtension,
					pureFilename + " - " + extension.substring(1).toUpperCase());
			indexFile = indexFilePath(directory, pureFilename);
		}

		private String getExtension(final String filename) {
			int beginIndex = filename.lastIndexOf('.');
			return filename.substring(beginIndex);
		}

		private String removeExtension(final String filename) {
			int endIndex = filename.lastIndexOf('.');
			return filename.substring(0, endIndex);
		}

		private Path directoryPath(final Path path, final String directoryName) {
			return Paths.get(path.getParent().toString(), directoryName);
		}

		private Path indexFilePath(final Path path, String name) {
			return Paths.get(path.toString(), (name + ".index.txt"));
		}

		private Path filePath(final Path path, String name, int version) {
			return Paths.get(path.toString(), name + "-v" + version + extension);
		}
	}
}
