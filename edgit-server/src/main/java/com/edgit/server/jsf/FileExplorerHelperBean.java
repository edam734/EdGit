package com.edgit.server.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.edgit.server.jsf.handlers.PersistenceService;

@Named
@SessionScoped
public class FileExplorerHelperBean implements Serializable { 

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UserManager userManager;

	private Long userRepositoryId;
	private Long currentFolderId;
	
	@EJB
	private PersistenceService persistenceService;

	@PostConstruct
	private void init() {
		this.userRepositoryId = userManager.getCurrentUser().getRootRepository().getFileId();
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
		this.currentFolderId = persistenceService.getCurrentFolderId(id, fname);
	}
}
