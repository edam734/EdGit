package com.edgit.commons.network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class NetworkFileUtils {

	private static int numOfFiles = 0;

	public static void send(/* ObjectOutputStream outStream */ OutputStream outStream, BinamedFile binamedFile,
			ZipOutputStream zos) throws IOException {

		File file = binamedFile.getFile();
		String filename = binamedFile.getPath().toString();

		zos.putNextEntry(new ZipEntry(filename));

//		NetworkFile networkFile = new NetworkFile(file, filename);

		// try (FileInputStream fis = new FileInputStream(file);
		// BufferedInputStream fif = new BufferedInputStream(fis);) {
		// // send the file content
		// while (!networkFile.hasReadingFinished()) {
		// NetworkFilePart part = networkFile.readFilePart();
		// // Write the contents of the file
		// byte[] bytes = part.getContent().toString().getBytes();
		// zos.write(bytes);
		// }
		// }

		try (FileInputStream fis = new FileInputStream(file); BufferedInputStream fif = new BufferedInputStream(fis);) {
			// Write the contents of the file
			int data = 0;
			while ((data = fif.read()) != -1) {
				zos.write(data);
			}
		}

		zos.closeEntry();

		System.out.println("Finished adding file " + filename);
		numOfFiles++;
		System.out.println("Numero de ficheiros adicionados até ao momento: " + numOfFiles);
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
