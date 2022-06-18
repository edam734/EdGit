package com.edgit.server.jsf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.edgit.server.domain.User;
import com.edgit.server.jsf.handlers.FilePathHandler;

@RequestScoped
public class FileUploader {

	@Inject
	private UserManager userManager;

	@EJB
	private FilePathHandler filepathHandler;

	@EJB
	private RepositoryManager repositoryManager;

	private User getCurrentUser() {
		return userManager.getCurrentUser();
	}

	public String upload(Part part) throws ServletException, IOException {
		File userRepository = filepathHandler.getUserRepository(getCurrentUser());

		for (Part p : getAllParts(part)) {
			String path = p.getSubmittedFileName();
			File file = new File(userRepository, path);

			writeOnDisk(p, file);
			persist(path);
		}
		// No redirection anymore. Just using AJAX to refresh a component
		return "homepage"; /* ?faces-redirect=true */
	}

	private void writeOnDisk(Part p, File file) throws IOException {
		try (InputStream input = p.getInputStream()) {
			filepathHandler.push(getCurrentUser(), input, file.toPath());
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