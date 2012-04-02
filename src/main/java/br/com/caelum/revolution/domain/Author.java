package br.com.caelum.revolution.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

@Entity
public class Author {
	@Id
	@GeneratedValue
	private int id;
	@Index(name = "author_name")
	private String name;
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
