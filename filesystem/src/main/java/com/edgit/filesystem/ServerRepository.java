package com.edgit.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class ServerRepository {

	public static final File REPOSITORY_ROOT_LOCATION = new File("EdGitServerRepository");
//
//	static {
//		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
//				.getContext();
//		REPOSITORY_ROOT_LOCATION = new File(servletContext.getInitParameter("upload.location"));
//	}

	public static File getRepositoryRootLocation() {
		return REPOSITORY_ROOT_LOCATION;
	}
	
	public static void readProperties() throws FileNotFoundException, IOException {
//		try (InputStream input = new FileInputStream("src\\main\\resources\\filesystem.properties")) {
		try (InputStream input = ServerRepository.class.getClassLoader().getResourceAsStream("filesystem.properties")) {
			Properties props = new Properties();
			
			props.load(input);
			
			System.out.println(props.getProperty("upload.location"));
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		readProperties();
		System.out.println(REPOSITORY_ROOT_LOCATION.getAbsolutePath());
	}

}