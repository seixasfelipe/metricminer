package org.metricminer.model;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Query implements Comparable<Query> {
	private String sqlQuery;
	private String csvFilename;
	private String name;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar submitDate;
	@Id
	@GeneratedValue
	private Long id;

	public Query() {
		submitDate = new GregorianCalendar();
	}

	public Query(String query) {
		this();
		this.sqlQuery = query;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sql) {
		this.sqlQuery = sql;
	}

	public Long getId() {
		return id;
	}

	public void executed(String outputFileName) {
		csvFilename = outputFileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getCSV() {
		return new File(csvFilename);
	}

	public String getCsvFilename() {
		return csvFilename;
	}

	public Calendar getSubmitDate() {
		return submitDate;
	}

	@Override
	public int compareTo(Query otherQuery) {
		return -submitDate.compareTo(otherQuery.submitDate);
	}
	
}
