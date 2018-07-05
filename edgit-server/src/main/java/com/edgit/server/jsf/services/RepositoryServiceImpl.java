package com.edgit.server.jsf.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.hibernate.Session;

import com.edgit.server.jpa.GitFile;
import com.edgit.server.jpa.HibernateUtilities;

@ApplicationScoped
public class RepositoryServiceImpl implements RepositoryService {

	private Map<String, List<Repository>> repositories1;
	private Map<String, List<GitFile>> repositories2;

	private GitFile root;

	// public RepositoryServiceImpl() {
	// // Finge que vai buscar os dados a uma base de dados.....
	// List<Repository> list1 = new ArrayList<>();
	// List<Repository> list2 = new ArrayList<>();
	// list1.add(new Repository("Projecto Mocreia", Paths.get("edam//Mocreia"),
	// "Um projecto dedicado às mocreias.",
	// LocalDate.of(2018, 06, 24)));
	// list1.add(new Repository("Projecto Couve", Paths.get("edam//Couve"), "Um
	// projecto dedicado às couves.",
	// LocalDate.of(2018, 03, 10)));
	// list2.add(new Repository("Projecto Cenoura",
	// Paths.get("zemanel//Cenoura"), "Um projecto dedicado às cenouras",
	// LocalDate.of(2017, 02, 03)));
	//
	// repositories1 = new HashMap<>();
	// repositories1.put("edam", list1);
	// repositories1.put("zemanel", list2);
	// }

	public RepositoryServiceImpl() {
		repositories2 = new HashMap<>();

		List<GitFile> subfiles1 = new ArrayList<>();
		GitFile repository = new GitFile();
		repository.setFilename("edam");
		repository.setInceptionDate(LocalDate.of(2018, 01, 01));
		repository.setFolder(null);
		repository.setIsFile(false);

		GitFile file1 = new GitFile();
		file1.setFilename("Projecto Mocreia");
		file1.setDescription("Um projecto dedicado às mocreias");
		file1.setInceptionDate(LocalDate.of(2018, 06, 24));
		file1.setFolder(repository);
		file1.setIsFile(false);

		GitFile file2 = new GitFile();
		file2.setFilename("codigo");
		file2.setInceptionDate(LocalDate.of(2018, 06, 24));
		file2.setFolder(file1);
		file2.setIsFile(false);

		List<GitFile> subfiles2 = new ArrayList<>();
		subfiles2.add(file2);
		repositories2.put("Projecto Mocreia", subfiles2);

		List<GitFile> subfiles3 = new ArrayList<>();
		GitFile file = new GitFile();
		file.setFilename("read me.txt");
		file.setInceptionDate(LocalDate.of(2018, 06, 25));
		file.setFolder(file2);
		file.setIsFile(true);

		subfiles3.add(file);
		repositories2.put("codigo", subfiles3);

		GitFile file3 = new GitFile();
		file3.setFilename("Projecto Couve");
		file3.setDescription("Um projecto dedicado às couves");
		file3.setInceptionDate(LocalDate.of(2018, 03, 10));
		file3.setFolder(repository);
		file3.setIsFile(false);

		GitFile file4 = new GitFile();
		file4.setFilename("le-me.txt");
		file4.setInceptionDate(LocalDate.of(2018, 03, 10));
		file4.setFolder(repository);
		file4.setIsFile(true);

		List<GitFile> subfiles4 = new ArrayList<>();
		subfiles4.add(file4);
		repositories2.put("Projecto Couve", subfiles4);

		subfiles1.add(file1);
		subfiles1.add(file3);
		repositories2.put("edam", subfiles1);

	}

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

	public void createRepo() {
		Session session = HibernateUtilities.getSessionFactory().openSession();
		session.beginTransaction();

		GitFile parent = new GitFile();
		parent.setFilename("edam");
		parent.setInceptionDate(LocalDate.of(2018, 01, 01));
		parent.setFolder(null);
		parent.setIsFile(false);

		GitFile file1 = new GitFile();
		file1.setFilename("Projecto Mocreia");
		file1.setDescription("Um projecto dedicado às mocreias");
		file1.setFolder(parent);
		file1.setIsFile(false);

		GitFile file2 = new GitFile();
		file2.setFilename("Projecto Couve");
		file2.setDescription("Um projecto dedicado às couves");
		file2.setInceptionDate(LocalDate.of(2018, 03, 10));
		file2.setFolder(parent);
		file2.setIsFile(false);

		GitFile file3 = new GitFile();
		file3.setFilename("read me.txt");
		file3.setInceptionDate(LocalDate.of(2018, 06, 25));
		file3.setFolder(file1);
		file3.setIsFile(true);

		session.save(parent);
		session.save(file1);
		session.save(file2);
		session.save(file3);
		session.getTransaction().commit();
		session.close();
	}

	public List<Repository> getRepositoryByUsername(String username) {
		return repositories1.get(username);
	}

	public List<GitFile> getFilesByParent(GitFile folder) {
		String repoName = folder.getFilename();
		return getFilesByParentName(repoName);
	}

	public List<GitFile> getFilesByParentName(String name) {
		return repositories2.get(name);
	}
}
