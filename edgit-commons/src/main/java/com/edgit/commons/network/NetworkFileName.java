package com.edgit.commons.network;

import java.io.Serializable;

import com.edgit.commons.network.exceptions.IllegalLengthNameException;

/**
 * The objects of this class are to be persisted over the web and provide the
 * absolute name of a file.
 * 
 * @author Eduardo
 *
 */
public class NetworkFileName implements Serializable {

	private static final long serialVersionUID = -5784591141692669267L;
	/*
	 * Maximum Path Length Limitation on Windows is 260 characters (including
	 * extension)
	 */
	private static final int MAX_SIZE = 260; // 1 char = 1 byte

	private byte[] filename;

	/**
	 * Constructor that creates an object that holds the name of a file
	 * 
	 * @param filename
	 *            the name of a file
	 * @throws IllegalLengthNameException
	 */
	public NetworkFileName(String filename) throws IllegalLengthNameException {
		if (filename.length() > MAX_SIZE) {
			throw new IllegalLengthNameException();
		}
		this.filename = filename.getBytes();
	}

	/**
	 * @return the filename
	 */
	public byte[] getFilename() {
		return filename;
	}
}
