package br.com.caelum.revolution.config;

import model.Project;

public class MetricMinerProjectConfig implements Config {

	Project project;

	public MetricMinerProjectConfig(Project project) {
		this.project = project;
	}

	@Override
	public String asString(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int asInt(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

}
