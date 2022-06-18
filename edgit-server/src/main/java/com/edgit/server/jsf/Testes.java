package com.edgit.server.jsf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class Testes {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String test = "efs56.a.454r.44";
		Pattern p = Pattern.compile("(\\.\\d+)$");
		System.out.println(p.matcher(test).find());
		
		Path path1 = Paths.get("C://ze//maria");
		Path path2 = Paths.get(path1.toString(), "joaquim.txt");
		File directory = path1.toFile();
		File file = path2.toFile();
		System.out.println("Directory:");
		System.out.println("Absolute path:" + directory.getAbsolutePath());
		System.out.println("Name:" + directory.getName());
		System.out.println("Canonical path:" + directory.getCanonicalPath().toString());
		System.out.println("File:");
		System.out.println("Absolute path:" + file.getAbsolutePath());
		System.out.println("Name:" + file.getName());
		System.out.println("Canonical path:" + file.getCanonicalPath().toString());
		
		Path path3 = Paths.get("C://Users//Eduardo//Desktop//Teste.txt");
		List<String> lista = Files.readAllLines(path3, StandardCharsets.UTF_8);
		for (String f : lista) {
			System.out.println(f);
		} 
	}
	 
	

}
