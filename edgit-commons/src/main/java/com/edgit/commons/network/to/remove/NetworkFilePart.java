package com.edgit.commons.network.to.remove;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;

/**
 * The objects in this class represent a part of a file that contains an array
 * of chars with some of its content.
 * 
 * @author Eduardo
 *
 */
public class NetworkFilePart implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Maximum size of each NetworkFilePart's contents.
	 */
	public static final int PART_SIZE = 1048576; // 1 MiB

	private char[] content;

	/**
	 * This constructor constructs an object by defining the size of its array
	 * of chars
	 * 
	 * @param leftToRead
	 *            The number of chars yet to be read. If this value is greater
	 *            than this object can save, then it stores only what it can.
	 */
	public NetworkFilePart(long leftToRead) {
		content = (leftToRead < PART_SIZE) ? new char[(int) leftToRead] : new char[PART_SIZE];
	}

	/**
	 * This constructor constructs an object by defining the size of its array
	 * of chars and a reader linked to an input stream.
	 * 
	 * @param reader
	 *            a reader linked to an input stream of a file.
	 * @param leftToRead
	 *            The number of chars yet to be read. If this value is greater
	 *            than this object can save, then it stores only what it can.
	 * @throws IOException
	 */
	public NetworkFilePart(BufferedReader reader, long leftToRead) throws IOException {
		this(leftToRead);
		reader.read(content);
	}

	/**
	 * Returns the read content of the file.
	 * 
	 * @return the read content from the file
	 */
	public char[] getContent() {
		return content;
	}
}
