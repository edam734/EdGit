package com.edgit.commons.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

import com.edgit.commons.network.exceptions.IllegalLengthNameException;

/**
 * Objects of this class define a file, store the name of that file as a
 * NetworkFileName, and return portions of the contents of the file as
 * NetworkFilePart objects.
 * 
 * @author Eduardo
 *
 */
public class NetworkFile implements Serializable {

	private static final long serialVersionUID = 119376898498462454L;

	private File file;
	private NetworkFileName filename;
	private BufferedReader reader;
	private BufferedWriter writer;
	private long leftToRead;
	private NumberOfParts numberOfParts;

	/**
	 * Constructor that creates a NetworkFile object, defining what file it
	 * contains
	 * 
	 * @param file
	 *            The file of this NetworkFile
	 * @throws IllegalLengthNameException
	 * @requires file != null
	 */
	public NetworkFile(File file) throws IllegalLengthNameException {
		long fileLengh = this.file.length();
		this.file = file;
		this.filename = new NetworkFileName(file.getAbsolutePath());
		leftToRead = fileLengh;
		this.numberOfParts = new NumberOfParts(fileLengh, NetworkFilePart.PART_SIZE);
	}

	/**
	 * Returns the file name of this NetworkFile
	 */
	public NetworkFileName getFilename() {
		return filename;
	}

	/**
	 * The number of NetworkFilePart that this file has
	 */
	public NumberOfParts getNumberOfParts() {
		return numberOfParts;
	}

	/**
	 * Reads a part of this file and returns it as a NetworkFilePart
	 * 
	 * @return the part of this file as a NetworkFilePart
	 * @throws IOException
	 */
	public NetworkFilePart readFilePart() throws IOException {
		NetworkFilePart part = new NetworkFilePart(getReader(), leftToRead);
		leftToRead -= NetworkFilePart.PART_SIZE;
		return part;
	}

	/**
	 * Verifies if the file of this NetworkFile has been read
	 * 
	 * @return true if the file of this NetworkFile has been read
	 */
	public boolean hasReadingFinished() {
		return leftToRead <= 0;
	}

	/*
	 * For the same instance of this object, the BufferedReader should always be
	 * the same.
	 */
	private BufferedReader getReader() throws IOException {
		if (reader == null) {
			reader = Files.newBufferedReader(file.toPath());
		}
		return reader;
	}

	/**
	 * Writes the full array of characters stored in the NetworkFilePart.
	 * 
	 * @param partToWrite
	 *            the NetworkFilePart to write
	 * @throws IOException
	 *             if failed or interrupted I/O operations occurs.
	 */
	public void writeFilePart(NetworkFilePart partToWrite) throws IOException {
		int len = partToWrite.getContent().length;
		getWriter().write(partToWrite.getContent(), 0, len);
	}

	/**
	 * Returns a writer that writes text to a character-output stream, buffering
	 * characters so as to provide for the efficient writing of single
	 * characters, arrays, and strings.
	 * 
	 * @throws IOException
	 *             if failed or interrupted I/O operations occurs.
	 */
	private BufferedWriter getWriter() throws IOException {
		if (writer == null) {
			setWriter();
		}
		return writer;
	}

	/**
	 * Opens or creates a file for writing, setting a BufferedWriter to write
	 * text to the file in an efficient manner. The text is encoded into bytes
	 * for writing using the UTF-8 charset.
	 * 
	 * @param path
	 *            The path of the file
	 * @throws IOException
	 *             if failed or interrupted I/O operations occurs.
	 */
	public void setWriter(Path path) throws IOException {
		writer = Files.newBufferedWriter(path);
	}

	/**
	 * Default setter
	 * <p>
	 * Opens or creates a file with this NetworkFile path for writing, setting a
	 * BufferedWriter to write text to the file in an efficient manner. The text
	 * is encoded into bytes for writing using the UTF-8 charset.
	 * 
	 * @throws IOException
	 *             if failed or interrupted I/O operations occurs.
	 */
	public void setWriter() throws IOException {
		setWriter(this.file.toPath());
	}
}
