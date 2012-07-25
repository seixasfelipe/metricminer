package org.metricminer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

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
	private String twitterUrl;

	public User() {
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

	public String getTwitterUrl() {
		return twitterUrl;
	}

}
