package org.metricminer.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class CalculatedMetric {
	@Id
	@GeneratedValue
	private Long id;
	private Project project;
	private Class metricClass;

	public CalculatedMetric(Project project, Class metricClass) {
		this.project = project;
		this.metricClass = metricClass;
	}
	
	public Class getMetricClass() {
		return metricClass;
	}

}
