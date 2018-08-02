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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.edgit.commons.network.BinamedFile;
import com.edgit.server.filesystem.EdGitRepositoryManager.BooleanResult.BooleanMessage;

/**
 * Utilities for manipulating files in the remote repository.
 * 
 * @author Eduardo Amorim
 *
 */
public class EdGitRepositoryManager {

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

		UnfoldPathResolver unfoldPathResolver = new EdGitRepositoryManager().new UnfoldPathResolver(target);
		final Path directory = unfoldPathResolver.getDirectory();
		final Path indexFile = unfoldPathResolver.getIndexFile();

		BooleanResult b = makeDirectory(directory.toFile());
		if (b.getMessage().equals(BooleanMessage.NOT_CREATED)) {
			return false;
		} else if (b.getMessage().equals(BooleanMessage.ALREADY_EXISTS)) {
			version = getMostRecentVersion(indexFile);
		}

		int newVersion = ++version;
		writeAnIndexEntry(indexFile.toFile(), Integer.toString(newVersion), " : ", username);

		target = unfoldPathResolver.getVersionedFilename(newVersion);

		return createFileNewVersion(in, target, options);
	}

	private static boolean createFileNewVersion(final InputStream in, final Path target, CopyOption... options)
			throws IOException {
		return (Files.copy(in, target, options) > 0);
	}

	private static void writeAnIndexEntry(File file, String... strings) {
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
	 * Returns all subfiles of this {@code file} with the name adapted to go to
	 * the user.
	 * <p>
	 * After this method is called, it's necessary to treat the name that goes
	 * within BinamedFile, removing the part that corresponds to the name of the
	 * user's repository.
	 * 
	 * @param file
	 *            The file to search
	 * @return All subfiles of this {@code file}
	 */
	public static List<BinamedFile> getAllSubfiles(File file) {
		List<BinamedFile> binFiles = new ArrayList<>();
		if (file.isDirectory()) {
			binFiles = getSubfiles(file);
		}
		// TODO para o caso de ser só um ficheiro
		return binFiles;
	}

	private static List<BinamedFile> getSubfiles(File file) {
		List<BinamedFile> subfiles = new ArrayList<>();
		return getSubfiles(file, subfiles);
	}

	private static List<BinamedFile> getSubfiles(File file, List<BinamedFile> subfiles) {
		if (file.isDirectory()) {
			if (file.getName().contains(" # ")) {
				Path dirPath = file.toPath();
				Path dirName = Paths.get(dirPath.getFileName().toString().split(" # ")[0]);
				Path indexFile = Paths.get(String.format("%s.index.txt", dirPath.resolve(dirName)));
				int version = getMostRecentVersion(indexFile);

				String extension = "." + dirPath.getFileName().toString().split(" # ")[1].toLowerCase();
				File subfile = new File(String.format("%s-v%d%s", dirPath.resolve(dirName), version, extension));
				Path unversionedPath = foldpath(subfile.toPath());
				subfiles.add(new BinamedFile(subfile, unversionedPath));
			} else {
				for (File subfile : file.listFiles()) {
					getSubfiles(subfile, subfiles);
				}
				return subfiles;
			}
		}
		return subfiles;
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
		if (file.exists()) {
			return new BooleanResult(BooleanMessage.ALREADY_EXISTS);
		} else {
			if (file.mkdirs()) {
				return new BooleanResult(BooleanMessage.CREATED);
			} else {
				return new BooleanResult(BooleanMessage.NOT_CREATED);
			}
		}
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

	// Path Resolver -----------------------------------------------------------

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
	class UnfoldPathResolver {

		private String extension;
		private String pureFilename;
		private Path directory;
		private Path indexFile;

		public Path getDirectory() {
			return directory;
		}

		public Path getIndexFile() {
			return indexFile;
		}

		public Path getVersionedFilename(int newVersion) {
			return filePath(directory, pureFilename, newVersion);
		}

		public UnfoldPathResolver(Path path) {
			resolve(path);
		}

		private void resolve(Path path) {
			extension = getExtension(path.getFileName().toString());
			pureFilename = removeExtension(path.getFileName().toString());
			Path pathWithoutExtension = Paths.get(removeExtension(path.toString()));
			directory = directoryPath(pathWithoutExtension,
					String.format("%s # %s", pureFilename, extension.substring(1).toUpperCase()));
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

	private static Path foldpath(Path path) {
		String compositFilename = path.getFileName().toString();
		String[] parts = compositFilename.split("-v\\d+");
		Path filename = Paths.get(parts[0].concat(parts[1]));
		Path directory = path.getParent();
		if (directory != null) {
			directory = directory.getParent();
			return Paths.get(directory.toString(), parts[0].concat(parts[1]));
		}
		return filename;
	}
}
