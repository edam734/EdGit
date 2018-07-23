package com.edgit.server.jsf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

import com.edgit.server.jsf.services.FilePathHandler;

@Named
@SessionScoped
public class UploadFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private FilePathHandler filepathHandler;
	
	private Part part;

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public String upload() throws IOException {
		// file.write("\\repo\\" + getFileName(file)); //<-- isto é para
		// escrever na pasta temporária

		File file = new File(filepathHandler.getUserRepository(), getFileName(part));

		try (InputStream input = part.getInputStream()) {
			Files.copy(input, file.toPath());
		}
		// deve ser um redirect / deverá também aparecer uma mensagem
		// de sucesso (implementar mais tarde)
		return "";
	}

	/*
	 * Method as described in the java EE 7 tutorial
	 */
	private String getFileName(final Part part) {
		final String partHeader = part.getHeader("content-disposition");
		for (String content : partHeader.split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}