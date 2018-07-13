package com.edgit.server.jsf.services;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.edgit.server.jpa.GitFile;
import com.edgit.server.jpa.dao.GitFileDAO;

public class PersistenceHandler {

	public static final String PUNIT_NAME = "edgitserverPU";

	// @PersistenceContext(unitName = "edgitserverPU")
	// private EntityManager em;

	private static final EntityManagerFactory entityManagerFactory;

	static {
		entityManagerFactory = Persistence.createEntityManagerFactory(PUNIT_NAME);
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void closeConnection() {
		// Closes the database connection
		entityManagerFactory.close();
	}

	public void create(GitFile parent, GitFile gitFile, String filename, String description) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			gitFile.setFilename(filename);
			gitFile.setInceptionDate(LocalDate.now());
			gitFile.setDescription(description);
			gitFile.setFolder(parent);
			gitFile.setIsFile(false);

			em.persist(gitFile);

			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
	}

	public List<GitFile> getSubfiles(String name) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		List<GitFile> subfiles;

		try {
			GitFileDAO dao = new GitFileDAO(em);
			subfiles = dao.findSubfilesOfFolder(name);
		} finally {
			em.close();
		}

		return subfiles;
	}
}
