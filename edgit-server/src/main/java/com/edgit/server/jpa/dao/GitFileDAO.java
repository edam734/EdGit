package com.edgit.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.edgit.server.jpa.GitFile;

public class GitFileDAO {

	private EntityManager entityManager;

	public GitFileDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<GitFile> findSubfilesOfFolder(String repositoryName) {
		TypedQuery<GitFile> query = entityManager.createNamedQuery(GitFile.QUERY_NAME_FIND_SUBFILES_OF_FOLDER,
				GitFile.class);
		query.setParameter(GitFile.QUERY_PARAM_REPOSITORY, repositoryName);

		return query.getResultList();
	}
}