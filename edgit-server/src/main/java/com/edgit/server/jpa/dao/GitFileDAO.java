package com.edgit.server.jpa.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.edgit.server.jpa.GitFile;
import com.edgit.server.jpa.HibernateUtilities;


public class GitFileDAO {

	public List<GitFile> findSubfilesOfFolder(String repositoryName) {
		Session session = HibernateUtilities.getSessionFactory().openSession();
		@SuppressWarnings("unchecked")
		Query<GitFile> query = session.getNamedQuery("HQL_GET_SUBFILES_OF_FOLDER_BY_NAME");
		query.setParameter(0, repositoryName);
		List<GitFile> subfiles = query.getResultList();
		session.close();
		return subfiles; 
	}
}
