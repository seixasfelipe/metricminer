package org.metricminer.tasks.metric.cc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.MetricResult;

@Entity
public class CCResult implements MetricResult {

	@Id
	@GeneratedValue
	private Long id;
	@OneToOne
	private SourceCode sourceCode;

	private int cc;
	private double avgCc;

	public CCResult() {
	}

	public CCResult(SourceCode sourceCode, int cc, double avgCc) {
		this.sourceCode = sourceCode;
		this.cc = cc;
		this.avgCc = avgCc;
	}

	public Long getId() {
		return id;
	}

	public SourceCode getSourceCode() {
		return sourceCode;
	}

	public int getCc() {
		return cc;
	}

	public double getAvgCc() {
		return avgCc;
	}

}
