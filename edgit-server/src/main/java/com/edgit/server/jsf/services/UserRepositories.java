package com.edgit.server.jsf.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.edgit.server.jpa.GitFile;

@Named
@SessionScoped
public class UserRepositories implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserManager userManager;

	@Inject
	private RepositoryServiceImpl repositoryService;

	private List<GitFile> repositories = new ArrayList<>();
	private List<GitFile> subfiles;
	private String username;
	private String filenameToSearch;
	private String repositoryName;
	private String description;

	@PostConstruct
	private void init() {
		username = userManager.getCurrentUser().getUsername();
//		loadUserRepositories(username);
	}

	private void loadUserRepositories(String username) {
		repositories = repositoryService.getFilesByParentName(username);
	}

	public List<GitFile> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<GitFile> repositories) {
		this.repositories = repositories;
	}

	/***************************************************/
	/* -------------- Search Operations -------------- */
	/***************************************************/

	public String getFilenameToSearch() {
		return filenameToSearch;
	}

	public void setFilenameToSearch(String filenameToSearch) {
		this.filenameToSearch = filenameToSearch;
	}

	public List<GitFile> getSubfiles() {
		return subfiles;
	}

	public void setSubfiles(List<GitFile> subfiles) {
		this.subfiles = subfiles;
	}

	public void searchForSubfiles() {
		subfiles = getSubfiles(filenameToSearch);
	}

	private List<GitFile> getSubfiles(String dir) {
		return repositoryService.getFilesByParentName(dir);
	}

	/***************************************************/
	/* ------------ Creation Operations -------------- */
	/***************************************************/

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
		repositoryService.createRepository(repositoryName, description);
		return "homepage.xhtml";
	}

}
