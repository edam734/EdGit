package com.edgit.server.jsf.services;

import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.hibernate.Session;

import com.edgit.server.jpa.GitFile;
import com.edgit.server.jpa.HibernateUtilities;
import com.edgit.server.jpa.dao.GitFileDAO;

@ApplicationScoped
public class RepositoryServiceImpl implements RepositoryService {

	private GitFile root;

	public void createRoot(String repositoryName) {
		Session session = HibernateUtilities.getSessionFactory().openSession();
		session.beginTransaction();

		root = new GitFile();
		root.setFilename(repositoryName);
		root.setInceptionDate(LocalDate.now());
		root.setFolder(null);
		root.setIsFile(false);

		session.save(root);
		session.getTransaction().commit();
		session.close();
	}

	public void createRepository(String repositoryName, String description) {
		Session session = HibernateUtilities.getSessionFactory().openSession();
		session.beginTransaction();

		GitFile repository = new GitFile();
		repository.setFilename(repositoryName);
		repository.setDescription(description);
		repository.setInceptionDate(LocalDate.now());
		repository.setFolder(root);
		repository.setIsFile(false);

		session.save(repository);
		session.getTransaction().commit();
		session.close();
	}
	
	public List<GitFile> getSubfiles(String name) {
		GitFileDAO dao = new GitFileDAO();
		List<GitFile> subfiles =  dao.findSubfilesOfFolder(name);
		
		return subfiles;
	}

}
