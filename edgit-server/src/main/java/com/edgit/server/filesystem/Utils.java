package com.edgit.server.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Utils {

	final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public static void makeDirectory(File file) {
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public static boolean updateFile(final Path dir, final File file, final String updateIndex) throws IOException {
		final File directory = dir.toFile();
		String remoteIndex = getRemoteIndex(directory);
		if (isFromSameUser(updateIndex, remoteIndex)) {
			createNewVersion(dir, file);
			return true;
		} else {
			return false;
		}
	}

	private static String getRemoteIndex(final File directory) throws IOException {
		Path remoteIndexFile = getRemoteIndexFilepath(directory);
		try (Scanner fileReader = new Scanner(remoteIndexFile)) {
			return fileReader.nextLine();
		}
	}

	private static void createNewVersion(final Path dir, File file) throws IOException {
		int version = getMostRecentVersion(dir);
		file.renameTo(Paths.get(file.getAbsolutePath() + "." + version).toFile());
		if (file.createNewFile()) {
			// write file to disk
		}
	}

	private static boolean isFromSameUser(final String upIndex, String reIndex) {
		return reIndex.equals(upIndex);
	}

	private static Path getRemoteIndexFilepath(File dir) {
		return Paths.get(dir.getAbsolutePath(), dir.getName() + ".index");
	}

	public static int getMostRecentVersion(final Path dir) {
		final File directory = dir.toFile();
		if (!directory.exists()) {
			return -1;
		}

		int mostRecentVersion = 0;
		for (final File file : directory.listFiles(filter())) {
			String filename = file.getName();
			int posLastToken = filename.lastIndexOf(".");
			int version = Integer.parseInt(filename.substring(posLastToken + 1));
			if (mostRecentVersion < version) {
				mostRecentVersion = version;
			}
		}

		return mostRecentVersion;
	}

	private static FilenameFilter filter() {

		FilenameFilter textFilter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				// accepts only names finished in '.<number>'
				Pattern p = Pattern.compile("(\\.\\d+)$");
				return p.matcher(name).find();
			}
		};
		return textFilter;
	}
}