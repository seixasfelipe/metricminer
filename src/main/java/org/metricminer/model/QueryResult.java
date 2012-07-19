package org.metricminer.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class QueryResult {
	@Id
	@GeneratedValue
	private Long id;
	private String csvFilename;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar executedDate;
	
	public QueryResult() {
	}

	public QueryResult(String csvFilename) {
		this.csvFilename = csvFilename;
		this.executedDate = Calendar.getInstance();
	}
	
	public String getCsvFilename() {
		return csvFilename;
	}
	
	public Long getId() {
		return id;
	}
	
	public Calendar getExecutedDate() {
		return executedDate;
	}

}
