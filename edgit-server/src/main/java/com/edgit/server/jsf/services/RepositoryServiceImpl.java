package com.edgit.server.jsf.services;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RepositoryServiceImpl implements RepositoryService {

	private Map<String, List<Repository>> repositories;

	public RepositoryServiceImpl() {
		// Finge que vai buscar os dados a uma base de dados.....
		List<Repository> list1 = new ArrayList<>();
		List<Repository> list2 = new ArrayList<>();
		list1.add(new Repository("Projecto Mocreia", Paths.get("edam//Mocreia"), "Um projecto dedicado às mocreias.",
				LocalDate.of(2018, 06, 24)));
		list1.add(new Repository("Projecto Couve", Paths.get("edam//Couve"), "Um projecto dedicado às couves.",
				LocalDate.of(2018, 03, 10)));
		list2.add(new Repository("Projecto Cenoura", Paths.get("zemanel//Cenoura"), "Um projecto dedicado às cenouras",
				LocalDate.of(2017, 02, 03)));

		repositories = new HashMap<>();
		repositories.put("edam", list1);
		repositories.put("zemanel", list2);
	}

	public List<Repository> getRepositoryByUsername(String username) {
		return repositories.get(username);
	}
}
