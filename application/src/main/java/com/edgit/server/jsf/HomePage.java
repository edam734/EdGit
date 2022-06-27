package com.edgit.server.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import com.edgit.domain.GitFile;

@Named
@ViewScoped
public class HomePage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserManager userManager;

	@Inject
	private RepositoryManager repositoryManager;
	
	@Inject
	private FileService fileService;

	private List<GitFile> repositories = new ArrayList<>();

	private Part part;

	public Part getPart() {
		return null;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	@PostConstruct
	private void loadUserRepositories() {
		// repositories =
		// repositoryManager.search(userManager.getCurrentUser().getUsername());
		repositories = repositoryManager.search(userManager.getCurrentUser().getRootRepository().getFileId());
	}

	public List<GitFile> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<GitFile> repositories) {
		this.repositories = repositories;
	}

	public String upload() throws ServletException, IOException {
		 return fileService.upload(part);
	}

	public String download() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		String filename = params.get("repositoryName");
		return fileService.download(filename);
	}	
}