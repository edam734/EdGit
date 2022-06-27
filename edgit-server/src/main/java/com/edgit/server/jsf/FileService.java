package com.edgit.server.jsf;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

public interface FileService {
	
	String upload(Part part) throws ServletException, IOException;

	String download(String name);
}
