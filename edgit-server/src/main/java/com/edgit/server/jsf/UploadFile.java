package com.edgit.server.jsf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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

@Named
@SessionScoped
public class UploadFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private FilePathHandler filepathHandler;

	private Part part;

	public Part getPart() {
		return null;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public String upload() throws ServletException, IOException {
		for (Part p : getAllParts(part)) {
			String fileName = p.getSubmittedFileName();
			File file = new File(filepathHandler.getUserRepository(), fileName);

			try (InputStream input = p.getInputStream()) {
				filepathHandler.copyFile(input, file.toPath());
			}
		}
		return "";
	}

	private static Collection<Part> getAllParts(Part part) throws ServletException, IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return request.getParts().stream().filter(p -> part.getName().equals(p.getName())).collect(Collectors.toList());
	}
}