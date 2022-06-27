package com.edgit.server.jsf.handlers;

import java.io.File;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class ServerRepositoryHandler {
	
	public static final File REPOSITORY_ROOT_LOCATION;
	
	static {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
				.getContext();
		REPOSITORY_ROOT_LOCATION = new File(servletContext.getInitParameter("upload.location"));
	}
}