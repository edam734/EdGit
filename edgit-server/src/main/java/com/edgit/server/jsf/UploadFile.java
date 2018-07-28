package com.edgit.server.jsf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.edgit.server.jsf.services.FilePathHandler;
import com.edgit.server.jsf.services.RepositoryManager;

@Named
@SessionScoped
public class UploadFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private FilePathHandler filepathHandler;

	@EJB
	private RepositoryManager repositoryManager;

	private Part part;

	public Part getPart() {
		return null;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public String upload() throws ServletException, IOException {
		File userRepository = filepathHandler.getUserRepository();

		for (Part p : getAllParts(part)) {
			String path = p.getSubmittedFileName();
			File file = new File(userRepository, path);

			writeOnDisk(p, file);
			persist(path);
		}
		return "";
	}

	private void writeOnDisk(Part p, File file) throws IOException {
		try (InputStream input = p.getInputStream()) {
			filepathHandler.uploadFile(input, file.toPath());
		}
	}

	private void persist(String filepath) {
		Path path = Paths.get(filepath);
		Path directory = filepathHandler.getDirectory(path);
		String filename = filepathHandler.getFilename(path);
		repositoryManager.createEntry(directory, filename, "(Todo)");
	}

	private static Collection<Part> getAllParts(Part part) throws ServletException, IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return request.getParts().stream().filter(p -> part.getName().equals(p.getName())).collect(Collectors.toList());
	}
}