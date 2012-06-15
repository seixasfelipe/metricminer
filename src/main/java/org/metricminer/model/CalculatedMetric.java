package org.metricminer.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class CalculatedMetric {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Project project;
	private Class metricFactoryClass;

	public CalculatedMetric(Project project, Class metricFactoryClass) {
		this.project = project;
		this.metricFactoryClass = metricFactoryClass;
	}
	
	public Class getMetricFactoryClass() {
		return metricFactoryClass;
	}

}
