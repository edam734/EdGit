package com.edgit.server.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Digest {

	private String password = "pass";
	private String algorithm = "SHA";

	public Digest(String password, String algorithm) {
		this.password = password;
		this.algorithm = algorithm;
	}

	public String getBase64Encoding() throws NoSuchAlgorithmException {

		// Calculate hash value
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update(password.getBytes());
		byte[] bytes = md.digest();
		String hash = Base64.getEncoder().encodeToString(bytes);

		return "{" + algorithm + "}" + hash;
	}
}
