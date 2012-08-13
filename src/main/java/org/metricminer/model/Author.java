package org.metricminer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Author {
	public static final String EMAIL_COLUMN = "secret_email";
	public static final String NAME_COLUMN = "secret_name";
	@Id
	@GeneratedValue
	private int id;
	@Column(name=NAME_COLUMN) @Index(name = "author_name")
	private String name;
	@Column(name=EMAIL_COLUMN)
	private String email;

	public Author(String author, String email) {
		name = author;
		this.email = email;
	}

	public Author() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

}
