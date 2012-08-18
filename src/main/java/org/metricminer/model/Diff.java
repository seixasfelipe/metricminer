package org.metricminer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class Diff {

	@Id @GeneratedValue
	private int id;
	@Type(type = "text")
	private String diff;

	protected Diff() {}

	public Diff(int id, String diff) {
		this.id = id;
		this.diff = diff;
	}
	
	public Diff(String diff) {
		this(0, diff);
	}
	
	public String getDiff() {
		return diff;
	}
	
	public int getId() {
		return id;
	}
	
}
