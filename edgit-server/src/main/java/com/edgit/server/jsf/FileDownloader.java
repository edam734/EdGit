package com.edgit.server.jsf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.edgit.commons.network.BinamedFile;
import com.edgit.server.domain.User;
import com.edgit.server.jsf.handlers.FilePathHandler;

@RequestScoped
public class FileDownloader {

	private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

	@Inject
	private UserManager userManager;
	
	@Inject
	private FilePathHandler filepathHandler;
	
	private User getCurrentUser() {
		return userManager.getCurrentUser();
	}

	public String download(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		List<BinamedFile> subfiles = filepathHandler.getAllSubfiles(getCurrentUser(), name);
		String filename = subfiles.get(0).getPath().toString();

		// To clean headers in the buffer beforehand
		externalContext.responseReset();

		// Prepare the response and set the necessary headers
		externalContext.setResponseContentType(getMimeType(facesContext, filename));
		externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

		try (OutputStream os = externalContext.getResponseOutputStream()) {
			for (BinamedFile binamedFile : subfiles) {
				filepathHandler.pull(os, binamedFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		facesContext.responseComplete();
		
		return "";
	}

	private static String getMimeType(FacesContext context, String name) {
		String mimeType = context.getExternalContext().getMimeType(name);

		if (mimeType == null) {
			mimeType = DEFAULT_MIME_TYPE;
		}

		return mimeType;
	}
}
