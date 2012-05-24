package org.metricminer.tasks.metric.testedmethods;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.metricminer.model.SourceCode;
import org.metricminer.tasks.metric.common.MetricResult;

@Entity
public class TestedMethodFinderResult implements MetricResult {

	@Id
	@GeneratedValue
	private Long id;
	@OneToOne
	private SourceCode sourceCode;

	private String testMethod;
	private String productionMethod;

	public TestedMethodFinderResult(SourceCode sourceCode, String testMethod,
			String productionMethod) {
		this.sourceCode = sourceCode;
		this.testMethod = testMethod;
		this.productionMethod = productionMethod;
	}

	public Long getId() {
		return id;
	}

	public SourceCode getSourceCode() {
		return sourceCode;
	}

	public String getTestMethod() {
		return testMethod;
	}

	public String getProductionMethod() {
		return productionMethod;
	}

}
