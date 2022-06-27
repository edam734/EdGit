package com.edgit.server.jsf;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

//import com.edgit.server.jpa.GitFile;
import com.edgit.domain.GitFile;

@Named
@ViewScoped
public class FileContentPage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private RepositoryManager repositoryManager;
	
	@Inject
	private FileExplorerHelperBean fileExplorerHelperBean;

	private List<GitFile> subfiles;

	public void searchSubfiles() {
		Long currentFolder = fileExplorerHelperBean.getCurrentFolderId();
		subfiles = repositoryManager.search(currentFolder);
	}

	public List<GitFile> getSubfiles() {
		return subfiles;
	}

	public void setSubfiles(List<GitFile> subfiles) {
		this.subfiles = subfiles;
	}
}