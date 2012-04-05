package model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TaskConfigurationEntry {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Task task;
	private String key;
	private String value;

	public TaskConfigurationEntry() {
	}

	public TaskConfigurationEntry(String key, String value, Task task) {
		this.key = key;
		this.value = value;
		this.task = task;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
