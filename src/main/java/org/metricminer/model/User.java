package org.metricminer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;
import org.metricminer.infra.encryptor.Encryptor;

@Entity
public class User {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@NotEmpty
	@Column(unique = true)
	private String email;
	private String password;
	private String university;
	private String cvUrl;
	private String twitter;
	@Transient
	private String passwordConfirmation;

	public User() {
	}

	public boolean isValid() {
		return password.equals(passwordConfirmation);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getUniversity() {
		return university;
	}

	public String getCvUrl() {
		return cvUrl;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email
				+ ", password=" + password + ", university=" + university
				+ ", cvUrl=" + cvUrl + ", twitter=" + twitter
				+ ", passwordConfirmation=" + passwordConfirmation + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCvUrl(String cvUrl) {
		this.cvUrl = cvUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public void setUniversity(String university) {
		this.university = university;
	}
	
	public void encryptPassword(Encryptor encryptor) {
		this.password = encryptor.encrypt(password);
	}
}
