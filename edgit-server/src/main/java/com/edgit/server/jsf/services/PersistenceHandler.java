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
		return create(parent, filename, description, false);
	}

	public GitFile create(GitFile parent, String filename, String description, boolean isFile) {
		EntityManager em = getEntityManagerFactory().createEntityManager();

		GitFile gitFile = new GitFile();
		try {
			em.getTransaction().begin();
			gitFile.setFilename(filename);
			gitFile.setInceptionDate(LocalDate.now());
			gitFile.setDescription(description);
			gitFile.setFolder(parent);
			gitFile.setIsFile(isFile);

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

	/**
	 * Creates an entry in the GitFile table of the database (if it does not
	 * already exist)
	 * 
	 * @param path
	 *            The path to this entry
	 * @param filename
	 *            The file name if this entry is a file (null otherwise)
	 * @param repo
	 *            The root repository of this user
	 * @param description
	 *            The description for this entry
	 * @return
	 */
	public boolean createEntry(Path path, String filename, GitFile repo, String description) {
		PathWrapper pathWrapper = getReminderPath(path, repo);
		Path pathToCreate = pathWrapper.getPath();
		GitFile parent = pathWrapper.getParent();
		if (pathToCreate != null) {
			Iterator<Path> it = pathToCreate.iterator();
			while (it.hasNext()) {
				String directoryName = it.next().getFileName().toString();
				parent = create(parent, directoryName, description);
			}
		}
		if (filename != null && !entryAlreadyExists(filename, parent.getFileId())) {
			// lastly, creates the file entry
			create(parent, filename, description, true);
		}
		return true;
	}
	
	private boolean entryAlreadyExists(String entryName, Long parentId) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		GitFileDAO dao = new GitFileDAO(em);
		try {
			return dao.findFileByNameAndParentId(entryName, parentId) != null;
		} finally {
			em.close();
		}	
	}

	/**
	 * Returns a PathWrapper with the Path of the entry yet to be created and
	 * the parent to which this path will connect.
	 * 
	 * @param path
	 *            The Path yet to be created
	 * @param parent
	 *            The parent where the path will connect
	 * @return a PathWrapper
	 */
	private PathWrapper getReminderPath(Path path, GitFile parent) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		GitFileDAO dao = new GitFileDAO(em);

		Iterator<Path> it = path.iterator();
		try {
			return getReminderPath(dao, it, parent);
		} finally {
			em.close();
		}
	}

	/**
	 * A recursive method that returns a PathWrapper with the path yet to be
	 * created in the GitFile table of the database, and with the "parent" to
	 * which this path should connect. <br/>
	 * For example, we have the path a/b/c/d/e/f. If there are entries in the
	 * database that correspond to the path a/b/c, the PathWrapper will contain
	 * the Path d/e/f with c as the parent. If the Path is null, then there is
	 * no path to be persisted, and the parent will be the equivalent of the
	 * root repository of this user.
	 * 
	 * @param dao
	 *            The dao responsible of finding a particular entry in the
	 *            GitFile table
	 * @param iterator
	 *            The iterator of the path that advances one step by each
	 *            recursion
	 * @param parent
	 *            The last existing father who was found
	 * @return a PathWrapper
	 */
	private PathWrapper getReminderPath(GitFileDAO dao, Iterator<Path> iterator, GitFile parent) {
		if (iterator.hasNext()) {
			String dir = iterator.next().toString();
			GitFile file = dao.findFileByNameAndParentId(dir, parent.getFileId());
			if (file != null) {
				parent = file; // this is the NEW parent!
				return getReminderPath(dao, iterator, parent);
			} else {
				// create reminder path
				StringBuilder sb = new StringBuilder();
				sb.append(dir);
				sb.append(File.separator);
				while (iterator.hasNext()) {
					sb.append(iterator.next());
					sb.append(File.separator);
				}
				Path remainderPath = Paths.get(sb.toString());
				return new PathWrapper(remainderPath, parent);
			}
		}
		return new PathWrapper(null, parent); // path already exists!
	}

	private class PathWrapper {
		private Path path;
		private GitFile parent;

		public PathWrapper(Path path, GitFile parent) {
			this.path = path;
			this.parent = parent;
		}

		public Path getPath() {
			return path;
		}

		public GitFile getParent() {
			return parent;
		}
	}
}