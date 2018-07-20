package com.edgit.commons.network;

import java.io.Serializable;

/**
 * The objects in this class are to persist through the web and provide the
 * number of parts that a file contains.
 * 
 * @author Eduardo
 *
 */
public class NumberOfParts implements Serializable {

	private static final long serialVersionUID = 1L;

	private int numberOfParts;

	/**
	 * Constructor that calculates the number of parts a file contains through
	 * the file size and the size of each part.
	 * 
	 * @param fileSize
	 *            the file size
	 * @param partSize
	 *            the size of each part
	 */
	public NumberOfParts(long fileSize, int partSize) {
		numberOfParts = (int) fileSize / partSize;
		if (fileSize % partSize != 0) {
			numberOfParts += 1;
		}
	}

	/**
	 * @return the numberOfParts
	 */
	public int getNumberOfParts() {
		return numberOfParts;
	}

}
