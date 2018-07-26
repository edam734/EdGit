package com.edgit.server.jsf.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.edgit.server.jpa.GitFile;
import com.edgit.server.jpa.dao.GitFileDAO;

public class PersistenceHandler {

	public static final String PUNIT_NAME = "edgitserverPU";

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

	public GitFile create(GitFile parent, String filename, String description) {
		EntityManager em = getEntityManagerFactory().createEntityManager();

		GitFile gitFile = new GitFile();
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
		return gitFile;
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

	public boolean createEntry(Path path, String filename, GitFile repo, String description) {
		Path pathToCreate = getReminderPath(path, repo.getFileId());
		GitFile parent = repo;
		if (pathToCreate != null) {
			Iterator<Path> it = pathToCreate.iterator();
			while (it.hasNext()) {
				String directoryName = it.next().getFileName().toString();
				parent = create(parent, directoryName, description);
			}
		}
		if (filename != null) {
			create(parent, filename, description); // lastly, creates the file
													// entry
		}
		return true;
	}

	private Path getReminderPath(Path path, Long parentId) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		GitFileDAO dao = new GitFileDAO(em);

		Iterator<Path> it = path.iterator();
		try {
			return getReminderPath(dao, it, parentId);
		} finally {
			em.close();
		}
	}

	private Path getReminderPath(GitFileDAO dao, Iterator<Path> iterator, Long parentId) {
		if (iterator.hasNext()) {
			String dir = iterator.next().toString();
			GitFile file = dao.findFileByNameAndParentId(dir, parentId);
			if (file != null) {
				return getReminderPath(dao, iterator, file.getFileId());
			} else {
				// create reminder path
				StringBuilder sb = new StringBuilder();
				sb.append(dir);
				sb.append(File.separator);
				while (iterator.hasNext()) {
					sb.append(iterator.next());
					sb.append(File.separator);
				}
				return Paths.get(sb.toString());
			}
		}
		return null; // path already exists!
	}
}