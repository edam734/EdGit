package com.edgit.server.jsf.services;

import java.nio.file.Path;
import java.time.LocalDate;

public class Repository {

	private String name;
	private Path path;
	private String description;
	private LocalDate inceptionDate;

	public Repository(String name, Path path, String description, LocalDate inceptionDate) {
		this.name = name;
		this.path = path;
		this.description = description;
		this.inceptionDate = inceptionDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(LocalDate inceptionDate) {
		this.inceptionDate = inceptionDate;
	}

}
