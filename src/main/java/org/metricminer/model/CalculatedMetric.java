package org.metricminer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CalculatedMetric {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Project project;
	private Class<?> metricFactoryClass;
	
	public CalculatedMetric() {
	}

	public CalculatedMetric(Project project, Class<?> metricFactoryClass) {
		this.project = project;
		this.metricFactoryClass = metricFactoryClass;
	}
	
	public Class<?> getMetricFactoryClass() {
		return metricFactoryClass;
	}
	
	public Project getProject() {
		return project;
	}
	
	public Long getId() {
		return id;
	}

}
