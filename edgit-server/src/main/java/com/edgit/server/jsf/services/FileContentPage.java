package com.edgit.server.jsf.services;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.edgit.server.jpa.GitFile;

@Named
@ViewScoped
public class FileContentPage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private RepositoryManager repositoryManager;

	private String filename;
	private List<GitFile> subfiles;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void searchSubfiles() {
		subfiles = repositoryManager.search(filename);
	}

	public List<GitFile> getSubfiles() {
		return subfiles;
	}

	public void setSubfiles(List<GitFile> subfiles) {
		this.subfiles = subfiles;
	}

}
