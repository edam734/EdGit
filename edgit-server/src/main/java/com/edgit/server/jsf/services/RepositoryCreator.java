package com.edgit.server.jsf.services;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class RepositoryCreator implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private RepositoryManager repositoryManager;

	private String repositoryName;
	private String description;

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String createRepository() {
		repositoryManager.createRepository(repositoryName, description);
		return "homepage.xhtml";
	}

}
