package com.edgit.server.filesystem;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.edgit.filesystem.BinamedFile;
import static com.edgit.server.filesystem.EdGitRepositoryManager.BooleanResult.BooleanMessage.*;

/**
 * Utilities for manipulating files in the remote repository.
 * 
 * @author Eduardo Amorim
 *
 */
public class EdGitRepositoryManager {

	final static Charset ENCODING = StandardCharsets.UTF_8;

	/**
	 * An utility class should not have public constructors
	 */
	private EdGitRepositoryManager() {
	}

	/**
	 * Place a new file in the appropriate location on the remote server, write to
	 * the ".index.txt" file who uploaded this version, and update the file's name
	 * version
	 * 
	 * @param in          The input stream that carries the data
	 * @param target      The path to be modified
	 * @param updateIndex
	 * @param username    who uploaded this file
	 * @param options     Some copy options
	 * @return true if was successful
	 * @throws IOException
	 */
	public static boolean receiveFile(final InputStream in, Path target, final String updateIndex,
			final String username, CopyOption... options) throws IOException {
		if (target.toFile().isDirectory()) {
			return makeDirectory(target.toFile()).toBoolean();
		}

		int version = 0;

		UnfoldPathResolver unfoldPathResolver = new UnfoldPathResolver(target);
		final Path directory = unfoldPathResolver.getDirectory();
		final Path indexFile = unfoldPathResolver.getIndexFile();

		BooleanResult b = makeDirectory(directory.toFile());
		if (b.getMessage().equals(NOT_CREATED)) {
			return false;
		} else if (b.getMessage().equals(ALREADY_EXISTS)) {
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
	 * Write a file in a zip with the appropriate name that is already contained in
	 * the field "path" of the method's argument binamedFile
	 * 
	 * @param binamedFile An object containing the file and the file's name
	 * @param zos         A zip to this file
	 * @throws IOException
	 */
	public static void sendFile(ZipOutputStream zos, BinamedFile binamedFile) throws IOException {

		File file = binamedFile.getFile();
		String filename = binamedFile.getPath().toString();

		zos.putNextEntry(new ZipEntry(filename));

		try (FileInputStream fis = new FileInputStream(file); BufferedInputStream fif = new BufferedInputStream(fis);) {
			// Write the contents of the file
			int data = 0;
			while ((data = fif.read()) != -1) {
				zos.write(data);
			}
		} catch (FileNotFoundException fnfe) {
			// If the file does not exists, write an error entry instead of file
			// contents
			zos.write(("ERROR: Could not find file " + filename).getBytes());
		}
		zos.closeEntry();
	}

	/**
	 * Returns all subfiles of this {@code file} with the name adapted to go to the
	 * user.
	 * <p>
	 * After this method is called, it's necessary to treat the name that goes
	 * within BinamedFile, removing the part that corresponds to the name of the
	 * user's repository.
	 * 
	 * @param file The file to search
	 * @return All subfiles of this {@code file}
	 */
	public static List<BinamedFile> getAllSubfiles(File file) {
		List<BinamedFile> binFiles = new ArrayList<>();
		if (file.isDirectory()) {
			binFiles = getSubfiles(file);
		} else {
			// TODO: para o caso de ser só um ficheiro
		}
		return binFiles;
	}

	private static List<BinamedFile> getSubfiles(File file) {
		List<BinamedFile> subfiles = new ArrayList<>();
		return getSubfiles(file, subfiles);
	}

	private static List<BinamedFile> getSubfiles(File file, List<BinamedFile> subfiles) {
		if (file.isDirectory()) {
			if (file.getName().contains(PathResolver.MARK)) {
				FoldPathResolver foldPathResolver = new FoldPathResolver(file.toPath());
				Path indexFile = foldPathResolver.getIndexFile();
				int version = getMostRecentVersion(indexFile);

				File subfile = new File(foldPathResolver.getVersionedFilename(version).toString());
				Path unversionedPath = foldPathResolver.getUnversionedFilename(subfile.toPath());
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
	 * @param file A directory
	 * @requires file.isDirectory()
	 * @return A BooleanResult with several possibilities
	 */
	public static BooleanResult makeDirectory(File file) {
		if (file.exists()) {
			return new BooleanResult(ALREADY_EXISTS);
		} else {
			if (file.mkdirs()) {
				return new BooleanResult(CREATED);
			} else {
				return new BooleanResult(NOT_CREATED);
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
			return NOT_CREATED.equals(message) ? false : true;
		}
	}

	// Path Resolver -----------------------------------------------------------

	interface PathResolver {

		final static String MARK = " # ";

		void resolve(Path path);

		Path getDirectory();

		Path getIndexFile();

		Path getVersionedFilename(final int newVersion);
	}

	/**
	 * Class that will apply file manipulation rules in the remote repository of the
	 * client.
	 * <p>
	 * For each file is created a new folder that will contain an ".index.txt" file
	 * and the file itself with the version number in the name. E.g., the file in
	 * the path client "/folder1/.../folderN/filename.pdf" will be placed on the
	 * server in the following format <br/>
	 * "/rootRepository/repo/folder1/.../folderN/filename - PDF/filename-v
	 * <version_number>.pdf
	 * 
	 * @author Eduardo Amorim
	 *
	 */
	static class UnfoldPathResolver implements PathResolver {

		private String extension;
		private String pureFilename;
		private Path directory;
		private Path indexFile;

		public UnfoldPathResolver(Path path) {
			resolve(path);
		}
		
		public void resolve(Path path) {
			extension = getExtension(path.getFileName().toString());
			pureFilename = removeExtension(path.getFileName().toString());
			Path parent = path.getParent();
			directory = directoryPath(parent, pureFilename.trim(),
					String.format("%s" + MARK + "%s", pureFilename, extension.substring(1).toUpperCase()));
			indexFile = indexFilePath(directory, pureFilename);
			
		}

		public Path getDirectory() {
			return directory;
		}

		public Path getIndexFile() {
			return indexFile;
		}

		public Path getVersionedFilename(final int newVersion) {
			return filePath(directory, pureFilename, newVersion);
		}

		private String getExtension(final String filename) {
			int beginIndex = filename.lastIndexOf('.');
			return filename.substring(beginIndex);
		}

		private String removeExtension(final String filename) {
			int endIndex = filename.lastIndexOf('.');
			return filename.substring(0, endIndex);
		}

		private Path directoryPath(final Path path, final String directory, final String directoryForExtension) {
			return Paths.get(path.toString(), directory, directoryForExtension);
		}

		private Path indexFilePath(final Path path, String name) {
			return Paths.get(path.toString(), (name + ".index.txt"));
		}

		private Path filePath(final Path path, String name, int version) {
			return Paths.get(path.toString(), name + "-v" + version + extension);
		}
	}

	/**
	 * Class that will apply file manipulation rules in the remote repository of the
	 * client.
	 * <p>
	 * For a given path, which is a file, gets the name of the index file, and gets
	 * the file with the corresponding version {@code version} in the name. It also
	 * gets the unversioned filename.
	 *
	 */
	static class FoldPathResolver implements PathResolver {

		private String extension;
		private Path directory;
		private String directoryName;
		private Path indexFile;

		/**
		 * @requires !path.isDirectory()
		 * @param path
		 */
		public FoldPathResolver(Path path) {
			resolve(path);
		}

		public void resolve(Path path) {
			directory = path;
			extension = "." + directory.getFileName().toString().split(MARK)[1].toLowerCase();
			directoryName = directory.getFileName().toString().split(MARK)[0];
			indexFile = directory.resolve(String.format("%s.index.txt", directoryName));
		}

		public Path getDirectory() {
			return directory;
		}

		public Path getIndexFile() {
			return indexFile;
		}

		public Path getVersionedFilename(int newVersion) {
			return directory.resolve(String.format("%s-v%d%s", directoryName, newVersion, extension));
		}

		/**
		 * Converts a versioned name to an unversioned name
		 * 
		 * @param path The path to convert
		 * @requires path must be a versioned file name
		 * @return an unversioned name
		 */
		public Path getUnversionedFilename(Path path) {
			String complete = path.getFileName().toString();
			String[] parts = complete.split("-v\\d+");
			Path fname = Paths.get(parts[0].concat(parts[1]));
			Path dir = path.getParent();
			if (dir != null) {
				dir = dir.getParent();
				return Paths.get(dir.toString(), parts[0].concat(parts[1]));
			}
			return fname;
		}
	}
}
