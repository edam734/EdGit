package com.edgit.commons.network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NetworkFileUtils {

	public static void send(ObjectOutputStream outStream, BinamedFile binamedFile) throws IOException {
		File file = binamedFile.getFile();
		String filename = binamedFile.getRawPath().toString();
		NetworkFile networkFile = new NetworkFile(file, filename);
		// send the file name
		NetworkFileName networkFilename = networkFile.getFilename();
		outStream.writeObject(networkFilename);
		// send the file number of parts
		NumberOfParts nParts = networkFile.getNumberOfParts();
		outStream.writeObject(nParts);
		// send the file content
		while (!networkFile.hasReadingFinished()) {
			NetworkFilePart part = networkFile.readFilePart();
			outStream.writeObject(part);
		}
	}

	public static void receive(ObjectInputStream inSteam) throws ClassNotFoundException, IOException {
		// receive the file name
		NetworkFileName networkFilename = (NetworkFileName) inSteam.readObject();
		if (networkFilename == null)
			return;
		// receive the file number of parts
		NumberOfParts nParts = (NumberOfParts) inSteam.readObject();
		if (nParts == null)
			return;
		// creates a network file to write to the same path
		NetworkFile networkFile = new NetworkFile(new File(networkFilename.getFilename().toString()));
		// receives and write all parts of the file
		for (int i = 0; i < nParts.getNumberOfParts(); i++) {
			NetworkFilePart partToWrite = (NetworkFilePart) inSteam.readObject();
			networkFile.writeFilePart(partToWrite);
		}
	}
}
