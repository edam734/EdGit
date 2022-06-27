package com.edgit.domain;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "GITFILE")
@NamedQueries({
		@NamedQuery(name = GitFile.QUERY_NAME_FIND_SUBFILES_OF_FOLDER, query = GitFile.QUERY_FIND_SUBFILES_OF_FOLDER),
		@NamedQuery(name = GitFile.QUERY_NAME_FIND_BY_NAME_AND_PARENTID, query = GitFile.QUERY_FIND_BY_NAME_AND_PARENTID),
		@NamedQuery(name = GitFile.QUERY_NAME_FIND_BY_PARENT_ID_AND_FILENAME, query = GitFile.QUERY_FIND_BY_PARENT_ID_AND_FILENAME) })
public class GitFile implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String QUERY_PARAM_REPOSITORY = "repository";

	public static final String QUERY_PARAM_NAME = "name";

	public static final String QUERY_PARAM_PARENT_ID = "parentId";

	public static final String QUERY_NAME_FIND_SUBFILES_OF_FOLDER = "GitFile.findSubfilesOfFolder";

	// public static final String QUERY_FIND_SUBFILES_OF_FOLDER = "select file
	// from GitFile as file where file.folder.fileId = (select fileId from
	// GitFile where filename = :"
	// + GitFile.QUERY_PARAM_REPOSITORY + ")";

	public static final String QUERY_FIND_SUBFILES_OF_FOLDER = "select file from GitFile as file where file.folder.fileId = :"
			+ GitFile.QUERY_PARAM_PARENT_ID;

	public static final String QUERY_NAME_FIND_BY_NAME_AND_PARENTID = "GitFile.findByNameAndParentId";

	public static final String QUERY_FIND_BY_NAME_AND_PARENTID = "select file from GitFile as file where file.filename = :"
			+ GitFile.QUERY_PARAM_NAME + " and file.folder.fileId = :" + GitFile.QUERY_PARAM_PARENT_ID;

	public static final String QUERY_PARAM_USER_REPOSITORY = "userRepository";

	public static final String QUERY_NAME_FIND_BY_PARENT_ID_AND_FILENAME = "GitFile.findByParentIdAndFilename";

	public static final String QUERY_FIND_BY_PARENT_ID_AND_FILENAME = "select file from GitFile as file where file.folder.fileId =:"
			+ GitFile.QUERY_PARAM_PARENT_ID + " and file.filename =:" + GitFile.QUERY_PARAM_USER_REPOSITORY;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GITFILE_ID", unique = true, nullable = false)
	private Long fileId;

	@ManyToOne
	@JoinColumn(name = "GITFILE_FOLDER")
	private GitFile folder;

	@Column(name = "GITFILE_NAME", nullable = false, length = 80)
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

	public void setFileId(Long fileId) {
		this.fileId = fileId;
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

	@Override
	public String toString() {
		return "GitFile [fileId=" + fileId + ", folder=" + folder + ", filename=" + filename + ", isFile=" + isFile
				+ "]";
	}

}
