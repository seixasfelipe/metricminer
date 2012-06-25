package org.metricminer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class QueryResult {
	@Id
	@GeneratedValue
	private Long id;
	private String csvFilename;
	
	public QueryResult() {
	}

	public QueryResult(String csvFilename) {
		this.csvFilename = csvFilename;
	}
	
	public String getCsvFilename() {
		return csvFilename;
	}
	
	public Long getId() {
		return id;
	}

}
