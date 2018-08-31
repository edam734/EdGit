package com.edgit.server.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.edgit.server.jsf.handlers.PersistenceHandler;

@Named
@SessionScoped
public class FileExplorerHelperBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UserManager userManager;

	private Long userRepositoryId;
	private Long currentFolderId;
	private PersistenceHandler persistenceHandler;

	@PostConstruct
	private void init() {
		this.userRepositoryId = userManager.getCurrentUser().getRootRepository().getFileId();
		this.persistenceHandler = new PersistenceHandler();
	}

	public Long getUserRepositoryId() {
		return userRepositoryId;
	}

	public void setUserRepositoryId(Long userRepositoryId) {
		this.userRepositoryId = userRepositoryId;
	}

	public Long getCurrentFolderId() {
		return currentFolderId;
	}

	public void setCurrentFolderId(Long currentFolderId) {
		this.currentFolderId = currentFolderId;
	}

	public void stepUp() {
		// TODO
	}

	public void stepDown() {
		Long id = Long.valueOf(
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("currentFolderId"));
		String fname = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("selectedFileName");
		this.currentFolderId = persistenceHandler.getCurrentFolderId(id, fname);
	}
}
