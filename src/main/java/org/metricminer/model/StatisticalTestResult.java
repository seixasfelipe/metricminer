package org.metricminer.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class StatisticalTestResult {

	@Id @GeneratedValue
	private int id;
	@ManyToOne
	private QueryResult q1;
	@ManyToOne
	private QueryResult q2;
	@ManyToOne
	private StatisticalTest test;
	private String output;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	public StatisticalTestResult(QueryResult q1, QueryResult q2,
			StatisticalTest test, String output) {
		this.q1 = q1;
		this.q2 = q2;
		this.test = test;
		this.output = output;
		this.date = Calendar.getInstance();
	}

	public StatisticalTestResult(int id) {
		this.id = id;
	}
	
	protected StatisticalTestResult() {
		this(0);
	}
	
	public int getId() {
		return id;
	}
	public QueryResult getQ1() {
		return q1;
	}
	public void setQ1(QueryResult q1) {
		this.q1 = q1;
	}
	public QueryResult getQ2() {
		return q2;
	}
	public void setQ2(QueryResult q2) {
		this.q2 = q2;
	}
	public StatisticalTest getTest() {
		return test;
	}
	public void setTest(StatisticalTest test) {
		this.test = test;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}

	
}
