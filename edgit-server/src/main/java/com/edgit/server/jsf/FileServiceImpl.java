package com.edgit.server.jsf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.edgit.domain.User;
import com.edgit.filesystem.BinamedFile;
import com.edgit.filesystem.FileManager;

@RequestScoped
public class FileServiceImpl implements FileService {

	private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

	@Inject
	private UserManager userManager;

//	@EJB
//	@Inject
	private FileManager fileManager;

//	@EJB
	@Inject
	private RepositoryManager repositoryManager;
	
	@PostConstruct
	public void init() {
		fileManager = new FileManager(Configuration.getRepositoryRootLocation(), userManager.getCurrentUser());
	}
	
	private User getCurrentUser() {
		return userManager.getCurrentUser();
	}

	public String upload(Part part) throws ServletException, IOException {
		File userRepository = fileManager.getUserRepository();

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
			fileManager.push(input, file.toPath());
		}
	}

	private void persist(String filepath) {
		Path path = Paths.get(filepath);
		Path directory = fileManager.getDirectory(path);
		String filename = fileManager.getFilename(path);
		repositoryManager.createEntry(directory, filename, "(Todo)");
	}

	private static Collection<Part> getAllParts(Part part) throws ServletException, IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return request.getParts().stream().filter(p -> part.getName().equals(p.getName())).collect(Collectors.toList());
	}

	public String download(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		List<BinamedFile> subfiles = fileManager.getAllSubfiles(getCurrentUser(), name);

		// To clean headers in the buffer beforehand
		externalContext.responseReset();

		// Prepare the response and set the necessary headers
		externalContext.setResponseContentType("Content-type: text/zip");
		externalContext.setResponseHeader("Content-Disposition", "attachment; filename=download.zip");

		try (OutputStream os = externalContext.getResponseOutputStream();
				ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(os));) {

			for (BinamedFile binamedFile : subfiles) {
				fileManager.pull(zos, binamedFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		facesContext.responseComplete();

		return "";
	}

//	private static String getMimeType(FacesContext context, String name) {
//		String mimeType = context.getExternalContext().getMimeType(name);
//
//		if (mimeType == null) {
//			mimeType = DEFAULT_MIME_TYPE;
//		}
//
//		return mimeType;
//	}
}
