package com.edgit.server.domain;

import java.util.Date;

import com.edgit.server.jpa.GitFile;

public class User {

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String phoneNumber;
	private Date birthDate;
	private GitFile rootRepository;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public GitFile getRootRepository() {
		return rootRepository;
	}

	public void setRootRepository(GitFile rootRepository) {
		this.rootRepository = rootRepository;
	}

}
