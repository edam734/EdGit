package com.edgit.server.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class Configuration {

	public static final String REPOSITORY_ROOT_LOCATION;

	static {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
				.getContext();
		REPOSITORY_ROOT_LOCATION = servletContext.getInitParameter("upload.location");
	}

	public static String getRepositoryRootLocation() {
		return REPOSITORY_ROOT_LOCATION;
	}

}
