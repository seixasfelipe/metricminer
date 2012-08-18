package org.metricminer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class CommitMessage {
	@Id @GeneratedValue
	private int id;
	@Type(type = "text")
	private String message;

	protected CommitMessage() {}

	public CommitMessage(int id, String message) {
		this.id = id;
		this.message = message;
	}
	
	public CommitMessage(String message) {
		this(0, message);
	}
	
	public int getId() {
		return id;
	}
	
	public String getMessage() {
		return message;
	}

}
