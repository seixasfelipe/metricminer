package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProjectConfigurationEntry {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Project project;
	private String key;
	private String value;

}
