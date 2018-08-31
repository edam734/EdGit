package com.edgit.server.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.edgit.server.jpa.GitFile;

public class GitFileDAO {

	private EntityManager entityManager;

	public GitFileDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public GitFile findByParentIdAndFilename(Long parentId, String filename) {
		Query query = entityManager.createNamedQuery(GitFile.QUERY_NAME_FIND_BY_PARENT_ID_AND_FILENAME, GitFile.class);
		query.setParameter(GitFile.QUERY_PARAM_PARENT_ID, parentId);
		query.setParameter(GitFile.QUERY_PARAM_USER_REPOSITORY, filename);

		try {
			return (GitFile) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

//	public List<GitFile> findSubfilesOfFolder(String repositoryName) {
//		TypedQuery<GitFile> query = entityManager.createNamedQuery(GitFile.QUERY_NAME_FIND_SUBFILES_OF_FOLDER,
//				GitFile.class);
//		query.setParameter(GitFile.QUERY_PARAM_REPOSITORY, repositoryName);
//
//		return query.getResultList();
//	}

	public List<GitFile> findSubfilesOfFolder(Long parentId) {
		TypedQuery<GitFile> query = entityManager.createNamedQuery(GitFile.QUERY_NAME_FIND_SUBFILES_OF_FOLDER,
		GitFile.class);
		query.setParameter(GitFile.QUERY_PARAM_PARENT_ID, parentId);

		return query.getResultList();
	}
	
	public GitFile findFileByNameAndParentId(String name, Long parentId) {
		Query query = entityManager.createNamedQuery(GitFile.QUERY_NAME_FIND_BY_NAME_AND_PARENTID, GitFile.class);
		query.setParameter(GitFile.QUERY_PARAM_NAME, name);
		query.setParameter(GitFile.QUERY_PARAM_PARENT_ID, parentId);

		try {
			return (GitFile) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
