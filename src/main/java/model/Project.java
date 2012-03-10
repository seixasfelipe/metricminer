package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Project {

	@Id
	@GeneratedValue
	private Long id;
	private String name;

	private String scmUrl;

	public Project() {
	}

	public Project(String name, String scmUrl) {
		this.name = name;
		this.scmUrl = scmUrl;
	}

	public String getName() {
		return name;
	}

	public String getScmUrl() {
		return scmUrl;
	}

	public Long getId() {
		return id;
	}
}
