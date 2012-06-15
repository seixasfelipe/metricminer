package org.metricminer.tasks.metric.lines;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.MetricResult;

@Entity(name = "LinesOfCodeResult")
public class LinesOfCodeResult implements MetricResult {

	@Id
	@GeneratedValue
	private Long id;
	@OneToOne
	private SourceCode sourceCode;

	@Column(length=1024)
	private String methodName;
	private int linesOfCode;

	public LinesOfCodeResult(SourceCode sourceCode, String method, int lines) {
		this.sourceCode = sourceCode;
		this.methodName = method;
		this.linesOfCode = lines;
	}

	public Long getId() {
		return id;
	}

	public SourceCode getSourceCode() {
		return sourceCode;
	}

	public String getMethodName() {
		return methodName;
	}

	public int getLinesOfCode() {
		return linesOfCode;
	}

}
