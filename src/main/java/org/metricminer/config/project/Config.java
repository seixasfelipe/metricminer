package org.metricminer.config.project;

public interface Config {
	String asString(String key);
	boolean contains(String key);
	int asInt(String key);
}
