package com.edgit.server.jpa;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "GITFILE")
public class GitFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GITFILE_ID", unique = true, nullable = false)
	private Long fileId;

	@ManyToOne
	@JoinColumn(name = "GITFILE_FOLDER")
	private GitFile folder;

	@Column(name = "GITFILE_NAME", nullable = false, length = 30)
	private String filename;

	@Column(name = "DESCRIPTION", nullable = true, length = 100)
	private String description;

	@Column(name = "DATE")
	private LocalDate inceptionDate;

	@Column(name = "IS_A_FILE")
	private Boolean isFile;

	@OneToMany(targetEntity = GitFile.class, mappedBy = "folder", cascade = CascadeType.ALL)
	private List<GitFile> subfiles = new ArrayList<>();

	public Long getFileId() {
		return fileId;
	}

	public GitFile getFolder() {
		return folder;
	}

	public void setFolder(GitFile folder) {
		this.folder = folder;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public Boolean getIsFile() {
		return isFile;
	}

	public void setIsFile(Boolean isFile) {
		this.isFile = isFile;
	}

	public List<GitFile> getSubfiles() {
		return subfiles;
	}

	public void setSubfiles(List<GitFile> subfiles) {
		this.subfiles = subfiles;
	}

}
