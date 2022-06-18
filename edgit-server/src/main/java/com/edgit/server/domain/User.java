package com.edgit.server.domain;

import java.util.Date;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.edgit.server.jpa.GitFile;
import com.edgit.server.jsf.util.EmailExtendedValidation;

public class User {

	@Pattern(regexp = "[A-Za-z0-9]{2,20}", message = "Please enter a username consisting of only letters and digits, between 2 and 20 characters long.")
	private String username;

	// Nota: alterar o tamanho mínimo para 8. Por enquanto é só para não me
	// chatear muito a escrever 8 caracteres no campo
	@Size(min = 1, message = "Your password must consist of at least 1 character.")
	private String password;

	@Size(min = 1, max = 30, message = "Please enter a first name between 1 and 30 characters long.")
	private String firstName;

	@Size(min = 1, max = 30, message = "Please enter a last name between 1 and 30 characters long.")
	private String lastName;

	@EmailExtendedValidation
	private String emailAddress;

	private String phoneNumber;

	@Past(message = "Your birth date must be in the past.")
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

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", emailAddress=" + emailAddress + ", phoneNumber=" + phoneNumber + ", birthDate="
				+ birthDate + ", rootRepository=" + rootRepository + "]";
	}
}
